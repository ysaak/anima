package ysaak.anima.service.importer.anidb;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import ysaak.anima.cache.Cache;
import ysaak.anima.cache.Cacheable;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.AnidbErrorCode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
public class AnidbApiClient {
    public static final String ANIME_REQUEST = "anime";

    @Cacheable(cache = Cache.ANIDB_ANIME_DATA)
    public String getAnimeXml(String anidbId) throws FunctionalException {
        return doGetRequest(
                AnidbApiClient.ANIME_REQUEST,
                Collections.singletonMap("aid", anidbId),
                entity -> EntityUtils.toString(entity, StandardCharsets.UTF_8)
        );
    }

    private <T> T doGetRequest(String request, Map<String, String> parameterMap, AnidbApiResponseConsumer<T> consumer) throws FunctionalException {
        final URI uri;

        try {
            URIBuilder uriBuilder = new URIBuilder(AnidbConstants.CLIENT_BASE_URL)
                    .addParameter("client", AnidbConstants.CLIENT_NAME)
                    .addParameter("clientver", AnidbConstants.CLIENT_VERSION)
                    .addParameter("protover", "1")
                    .addParameter("request", request);

            parameterMap.forEach(uriBuilder::addParameter);

            uri = uriBuilder.build();
        }
        catch (URISyntaxException e) {
            throw AnidbErrorCode.IMPORT_API_URI_CREATE.functional(e);
        }

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");

        final T data;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpResponse response = httpclient.execute(httpGet);

            int responseCode = response.getStatusLine().getStatusCode();

            if (responseCode == HttpStatus.SC_OK) {
                data = consumer.consume(response.getEntity());
                EntityUtils.consumeQuietly(response.getEntity());
            }
            else {
                EntityUtils.consumeQuietly(response.getEntity());
                throw AnidbErrorCode.IMPORT_API_INVALID_SERVER_RESPONSE.functional(request, responseCode);
            }
        }
        catch (IOException e) {
            throw AnidbErrorCode.IMPORT_API_DATA_READ.functional(e, request);
        }

        return data;
    }

    @FunctionalInterface
    public interface AnidbApiResponseConsumer<T> {
        T consume(HttpEntity entity) throws IOException;
    }
}
