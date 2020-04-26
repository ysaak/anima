package ysaak.anima.view.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.service.importer.anidb.AnidbImporterService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.router.RoutingService;

@Controller
@RequestMapping("/admin/importer/anidb")
@Transactional
public class AdminAnidbImporterController extends AbstractViewController {

    private final AnidbImporterService anidbImporterService;
    private final TranslationService translationService;

    public AdminAnidbImporterController(AnidbImporterService anidbImporterService, TranslationService translationService, RoutingService routingService) {
        super(translationService, routingService);
        this.anidbImporterService = anidbImporterService;
        this.translationService = translationService;
    }

    @GetMapping(path = "/", name = "admin.importer.anidb.index")
    public String indexAction(ModelMap model) {
        model.put("title_count", anidbImporterService.getAvailableTitleCount());
        return "admin/anidb/index";
    }

    @PostMapping(path = "/reload-titles", name = "admin.importer.anidb.reload-titles")
    public String reloadTitlesAction(final RedirectAttributes redirectAttributes) {
        try {
            anidbImporterService.refreshTitleList();
            this.addFlashInfoMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.success"));
        }
        catch (Exception e) {
            this.addFlashErrorMessage(redirectAttributes, translationService.get("importer.anidb.title-list-reloaded.error"));
        }

        return "redirect:/admin/importer/anidb/";
    }
}
