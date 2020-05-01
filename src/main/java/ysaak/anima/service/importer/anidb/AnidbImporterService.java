package ysaak.anima.service.importer.anidb;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.dao.repository.AnidbTitleRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementRemoteId;
import ysaak.anima.data.ExternalSite;
import ysaak.anima.data.importer.Importer;
import ysaak.anima.data.importer.TagEquivalence;
import ysaak.anima.data.importer.anidb.AnidbTitle;
import ysaak.anima.data.importer.anidb.AnidbTitleType;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.AnidbErrorCode;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.importer.TagEquivalenceService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Service
public class AnidbImporterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnidbImporterService.class);

    private final ElementService elementService;
    private final ExternalSiteService externalSiteService;
    private final TagEquivalenceService tagEquivalenceService;

    private final AnidbApiClient anidbApiClient;

    private final AnidbTitleRepository anidbTitleRepository;

    @Autowired
    public AnidbImporterService(ElementService elementService, ExternalSiteService externalSiteService, TagEquivalenceService tagEquivalenceService, AnidbApiClient anidbApiClient, AnidbTitleRepository anidbTitleRepository) {
        this.elementService = elementService;
        this.externalSiteService = externalSiteService;
        this.tagEquivalenceService = tagEquivalenceService;
        this.anidbApiClient = anidbApiClient;
        this.anidbTitleRepository = anidbTitleRepository;
    }

    public long getAvailableTitleCount() {
        return anidbTitleRepository.count();
    }

    public void refreshTitleList() throws FunctionalException {

        final File titleListFile = downloadAnimeTitlesFileFromServer();
        LOGGER.debug("Data file downloaded");

        final List<AnidbTitle> titleList = new ArrayList<>();

        try (
                InputStream fileStream = new FileInputStream(titleListFile);
                InputStream gzipStream = new GZIPInputStream(fileStream);
                Reader decoder = new InputStreamReader(gzipStream, StandardCharsets.UTF_8);
                BufferedReader buffered = new BufferedReader(decoder)
        ) {
            String line;

            while ((line = buffered.readLine()) != null) {
                if (line.startsWith("#")) { // Comment line
                    continue;
                }
                mapFromString(line).ifPresent(titleList::add);
            }
        }
        catch (IOException e) {
            throw AnidbErrorCode.TITLE_LIST_FILE_READ.functional(e);
        }

        anidbTitleRepository.truncate();
        anidbTitleRepository.saveAll(titleList);
    }

    private File downloadAnimeTitlesFileFromServer() throws FunctionalException {
        final File titleListFile;

        try {
            titleListFile = File.createTempFile("ANIMA_", "");
        }
        catch (IOException e) {
            throw AnidbErrorCode.TITLE_LIST_TEMP_FILE_CREATION.functional(e);
        }

        HttpGet httpGet = new HttpGet(AnidbConstants.ANIDB_TITLE_FILE_URL);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpResponse response = httpclient.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == HttpStatus.SC_OK) {
                ReadableByteChannel readableByteChannel = Channels.newChannel(response.getEntity().getContent());
                FileOutputStream fileOutputStream = new FileOutputStream(titleListFile);
                fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                EntityUtils.consumeQuietly(response.getEntity());
            }
            else {
                EntityUtils.consumeQuietly(response.getEntity());
                throw AnidbErrorCode.TITLE_LIST_INVALID_SERVER_RESPONSE.functional(responseCode);
            }
        }
        catch (IOException e) {
            throw AnidbErrorCode.TITLE_LIST_FILE_DOWNLOAD.functional(e);
        }

        return titleListFile;
    }

    private Optional<AnidbTitle> mapFromString(String line) {
        AnidbTitle title = null;

        try {
            String[] animeData = line.split("\\|", 4);

            if (AnidbConstants.TITLE_LANG_PRIORITY.contains(animeData[2])) {
                title = new AnidbTitle(
                        animeData[0],
                        AnidbTitleType.getFromCode(animeData[1]),
                        animeData[2],
                        animeData[3]
                );
            }
        }
        catch (Throwable t) {
            LOGGER.error("Error while parsing line > " + line);
        }

        return Optional.ofNullable(title);
    }

    public Page<AnidbTitle> searchByTitle(String title, int page, int maxResults) {
        int pageRequest = page;
        if (page < 1) {
            pageRequest = 1;
        }
        pageRequest -= 1;

        final String searchQuery = "%" + title.toLowerCase().replace('*', '%') + "%";

        return anidbTitleRepository.searchByPrimaryTitle(
                searchQuery,
                PageRequest.of(pageRequest, maxResults, Sort.by("ADTI_TITLE"))
        );
    }

    public Element importAnime(String aniDbId) throws Exception {
        final Map<String, String> dataMap = externalSiteService.findElementIdFromSiteAndRemoteId(AnidbConstants.ANIDB_SITE_CODE, Collections.singletonList(aniDbId));

        if (dataMap.size() > 0) {
            throw AnidbErrorCode.IMPORT_ALREADY_IMPORTED.functional(aniDbId);
        }

        final String animeData = anidbApiClient.getAnimeXml(aniDbId);

        final List<TagEquivalence> equivalenceList = tagEquivalenceService.findByImporter(Importer.ANIDB);
        final Map<String, String> equivalenceMap = equivalenceList.stream().collect(Collectors.toMap(TagEquivalence::getEquivalence, TagEquivalence::getTagId));

        final Element element = AnidbAnimeXmlParser.parseDocument(animeData, equivalenceMap);

        final ExternalSite site = externalSiteService.findByCode(AnidbConstants.ANIDB_SITE_CODE)
                .orElseThrow(() -> AnidbErrorCode.IMPORT_SITE_NOT_FOUND.functional(AnidbConstants.ANIDB_SITE_CODE));
        ElementRemoteId remoteId = new ElementRemoteId(element, site, aniDbId);
        element.setRemoteIdList(Collections.singletonList(remoteId));

        return elementService.create(element);
    }
}
