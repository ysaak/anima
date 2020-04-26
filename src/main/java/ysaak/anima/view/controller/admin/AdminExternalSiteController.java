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
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.ExternalSiteService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/external-site")
@Transactional
public class AdminExternalSiteController extends AbstractViewController {
    private static final String ROUTE_INDEX = "admin.external-site.index";
    private static final String ROUTE_NEW = "admin.external-site.new";
    private static final String ROUTE_CREATE = "admin.external-site.create";
    private static final String ROUTE_EDIT = "admin.external-site.edit";
    private static final String ROUTE_UPDATE = "admin.external-site.update";
    private static final String ROUTE_DELETE = "admin.external-site.delete";

    private final ExternalSiteService externalSiteService;

    @Autowired
    public AdminExternalSiteController(ExternalSiteService externalSiteService, TranslationService translationService, RoutingService routingService) {
        super(translationService, routingService);
        this.externalSiteService = externalSiteService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        final List<ExternalSite> siteList = externalSiteService.findAll();
        model.put("siteList", siteList);

        return "admin/external-site/index";
    }

    @GetMapping(path = "/new", name = ROUTE_NEW)
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("site")) {
            model.put("site", new ExternalSite());
        }

        return "admin/external-site/edit";
    }

    @PostMapping(path = "/", name = ROUTE_CREATE)
    public String createAction(@ModelAttribute ExternalSite externalSite, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.save(externalSite);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("site", externalSite);
            return redirect(ROUTE_NEW);
        }

        return redirect(ROUTE_INDEX);
    }

    @GetMapping(path = "/{id}/edit", name = ROUTE_EDIT)
    public String editAction(ModelMap model, @PathVariable("id") String id) throws FunctionalException {
        if (!model.containsAttribute("site")) {
            final ExternalSite externalSite = externalSiteService.findById(id);
            model.put("site", externalSite);
        }

        return "admin/external-site/edit";
    }

    @PostMapping(path = "/{id}", name = ROUTE_UPDATE)
    public String updateAction(@ModelAttribute ExternalSite externalSite, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.save(externalSite);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("site", externalSite);
            return redirect(ROUTE_EDIT, Collections.singletonMap("id", externalSite.getId()));
        }

        return redirect(ROUTE_INDEX);
    }

    @PostMapping(path = "/{id}/delete", name = ROUTE_DELETE)
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        try {
            externalSiteService.delete(id);
            addFlashInfoMessage(redirectAttributes, this.translationService.get("external-site.action.delete"));
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
        }

        return redirect(ROUTE_INDEX);
    }
}
