package ysaak.anima.view.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.ExternalSite;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.exception.NotAllowedOperationException;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;

import java.util.List;

@Controller
@RequestMapping("/admin/external-site")
@Transactional
public class AdminExternalSiteController extends AbstractViewController {

    private final ExternalSiteService externalSiteService;
    private final TranslationService translationService;

    @Autowired
    public AdminExternalSiteController(ExternalSiteService externalSiteService, TranslationService translationService) {
        this.externalSiteService = externalSiteService;
        this.translationService = translationService;
    }

    @GetMapping(path = "/", name = "admin.external-site.index")
    public String indexAction(ModelMap model) {
        final List<ExternalSite> siteList = externalSiteService.findAll();
        model.put("siteList", siteList);

        return "admin/external-site/index";
    }

    @GetMapping(path = "/new", name = "admin.external-site.new")
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("site")) {
            model.put("site", new ExternalSite());
        }

        return "admin/external-site/edit";
    }

    @PostMapping(path = "/", name = "admin.external-site.create")
    public String createAction(@ModelAttribute ExternalSite externalSite, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.save(externalSite);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("site", externalSite);
            return "redirect:/admin/external-site/new";
        }

        return "redirect:/admin/external-site/";
    }

    @GetMapping(path = "/{id}/edit", name = "admin.external-site.edit")
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("site")) {
            final ExternalSite externalSite = externalSiteService.findById(id);
            model.put("site", externalSite);
        }

        return "admin/external-site/edit";
    }

    @PostMapping(path = "/{id}", name = "admin.external-site.update")
    public String updateAction(@ModelAttribute ExternalSite externalSite, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.save(externalSite);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("site", externalSite);
            return "redirect:/admin/external-site/" + externalSite.getId() + "/edit";
        }

        return "redirect:/admin/external-site/";
    }

    @PostMapping(path = "/{id}/delete", name = "admin.external-site.delete")
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.delete(id);
            addFlashInfoMessage(redirectAttributes, translationService.get("external-site.action.delete"));
        }
        catch (NoDataFoundException e) {
            addFlashErrorMessage(redirectAttributes, translationService.get("external-site.error.not-found"));
        }
        catch (NotAllowedOperationException e) {
            addFlashErrorMessage(redirectAttributes, translationService.get("external-site.error.operation-not-allowed"));
        }

        return "redirect:/admin/external-site/";
    }
}
