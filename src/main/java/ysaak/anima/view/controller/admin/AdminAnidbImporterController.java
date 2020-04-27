package ysaak.anima.view.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.importer.anidb.AnidbImporterService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.router.RoutingService;

@Controller
@RequestMapping("/admin/importer/anidb")
@Transactional
public class AdminAnidbImporterController extends AbstractViewController {
    private static final String ROUTE_INDEX = "admin.importer.anidb.index";
    private static final String ROUTE_TITLES_RELOAD = "admin.importer.anidb.reload-titles";

    private final AnidbImporterService anidbImporterService;

    public AdminAnidbImporterController(AnidbImporterService anidbImporterService, TranslationService translationService, RoutingService routingService) {
        super(translationService, routingService);
        this.anidbImporterService = anidbImporterService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        model.put("title_count", anidbImporterService.getAvailableTitleCount());
        return "admin/anidb/index";
    }

    @PostMapping(path = "/reload-titles", name = ROUTE_TITLES_RELOAD)
    public String reloadTitlesAction(final RedirectAttributes redirectAttributes) {
        try {
            anidbImporterService.refreshTitleList();
            this.addFlashInfoMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.success"));
        }
        catch (FunctionalException e) {
            this.addFlashErrorMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.error"));
        }

        return redirect(ROUTE_INDEX);
    }
}
