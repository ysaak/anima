package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.Element;
import ysaak.anima.data.importer.anidb.AnidbTitle;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.importer.anidb.AnidbConstants;
import ysaak.anima.service.importer.anidb.AnidbImporterService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.dto.PageDto;
import ysaak.anima.view.dto.importer.ImportSearchResultDto;
import ysaak.anima.view.router.RoutingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/importer")
public class ImporterController extends AbstractViewController {
    private static final int MAX_RESULTS = 10;

    private final AnidbImporterService anidbImporterService;
    private final ExternalSiteService externalSiteService;
    private final RoutingService routingService;
    private final TranslationService translationService;

    @Autowired
    public ImporterController(AnidbImporterService anidbImporterService, ExternalSiteService externalSiteService, RoutingService routingService, TranslationService translationService) {
        super(translationService, routingService);
        this.anidbImporterService = anidbImporterService;
        this.externalSiteService = externalSiteService;
        this.routingService = routingService;
        this.translationService = translationService;
    }

    @GetMapping(path = "/", name = "importer.index")
    public String indexAction(ModelMap model) {
        return "importer/index";
    }


    @GetMapping(path = "/anidb/search", name = "importer.anidb.search")
    public String anidbSearchAction(final ModelMap model, String search, Integer page) {
        final List<ImportSearchResultDto> searchResultList;
        final PageDto pageDto;

        final int pageRequest = (page != null) ? page : 1;

        if (StringUtils.isNotBlank(search)) {
            Page<AnidbTitle> resultPage = anidbImporterService.searchByTitle(search, pageRequest, MAX_RESULTS);

            final List<String> idList = resultPage.get().map(AnidbTitle::getAnidbId).collect(Collectors.toList());
            final Map<String, String> urlMap = externalSiteService.getUrlForIdList(AnidbConstants.ANIDB_SITE_CODE, idList);
            final Map<String, String> elementMap = externalSiteService.findElementIdFromSiteAndRemoteId(AnidbConstants.ANIDB_SITE_CODE, idList);


            searchResultList = resultPage.get().map(e -> new ImportSearchResultDto(
                        e.getAnidbId(),
                        e.getTitle(),
                        urlMap.getOrDefault(e.getAnidbId(), "#"),
                        elementMap.get(e.getAnidbId())
                )).collect(Collectors.toList());

            pageDto = new PageDto(
                    pageRequest,
                    resultPage.getTotalPages(),
                    resultPage.hasPrevious(),
                    resultPage.hasNext(),
                    resultPage.getTotalElements()
            );
        }
        else {
            searchResultList = new ArrayList<>();
            pageDto = null;
        }

        model.put("search_text", search);
        model.put("searchResultList", searchResultList);
        model.put("page", pageDto);

        return "importer/anidb_search";
    }

    @PostMapping(path = "/anidb/import", name = "importer.anidb.import")
    public String anidbImportAction(final RedirectAttributes redirectAttributes, @RequestParam("externalId") final String anidbId) {
        final Element element;
        try {
            element = anidbImporterService.importAnime(anidbId);
        }
        catch (Exception e) {
            this.addFlashErrorMessage(redirectAttributes, translationService.get("importer.error"));
            return routingService.redirectUrl("importer.anidb.search");
        }

        return routingService.redirectUrl(element);
    }
}
