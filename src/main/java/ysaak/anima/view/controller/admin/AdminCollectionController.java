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
import ysaak.anima.data.Collection;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.NoDataFoundException;
import ysaak.anima.service.CollectionService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/collection")
@Transactional
public class AdminCollectionController extends AbstractViewController {

    private final CollectionService collectionService;
    private final TranslationService translationService;
    private final RoutingService routingService;

    @Autowired
    public AdminCollectionController(CollectionService collectionService, TranslationService translationService, RoutingService routingService) {
        this.collectionService = collectionService;
        this.translationService = translationService;
        this.routingService = routingService;
    }

    @GetMapping(path = "/", name = "admin.collections.index")
    public String indexAction(ModelMap model) {
        final List<Collection> collectionList = collectionService.findAll();
        model.put("collectionList", collectionList);

        return "admin/collection/index";
    }

    @GetMapping(path = "/new", name = "admin.collections.new")
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("collection")) {
            model.put("collection", new Collection());
        }

        return "admin/collection/edit";
    }

    @PostMapping(path = "/", name = "admin.collections.create")
    public String createAction(@ModelAttribute Collection collection, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.save(collection);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("collection", collection);
            return routingService.redirectUrl("admin.collection.new");
        }

        return routingService.redirectUrl("admin.collections.index");
    }

    @GetMapping(path = "/{id}/edit", name = "admin.collections.edit")
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("collection")) {
            final Collection collection = collectionService.findById(id);
            model.put("collection", collection);
        }

        return "admin/collection/edit";
    }

    @PostMapping(path = "/{id}", name = "admin.collections.update")
    public String updateAction(@ModelAttribute Collection collection, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.save(collection);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("collection", collection);
            return routingService.redirectUrl("admin.collections.edit", Collections.singletonMap("id", collection.getId()));
        }

        return routingService.redirectUrl("admin.collections.index");
    }

    @PostMapping(path = "/{id}/delete", name = "admin.collections.delete")
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.delete(id);
            addFlashInfoMessage(redirectAttributes, translationService.get("collection.action.delete"));
        }
        catch (NoDataFoundException e) {
            addFlashErrorMessage(redirectAttributes, translationService.get("collection.error.not-found"));
        }

        return routingService.redirectUrl("admin.collections.index");
    }
}
