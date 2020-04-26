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
import ysaak.anima.exception.FunctionalException;
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
    private static final String ROUTE_INDEX = "admin.collections.index";
    private static final String ROUTE_NEW = "admin.collections.new";
    private static final String ROUTE_CREATE = "admin.collections.create";
    private static final String ROUTE_EDIT = "admin.collections.edit";
    private static final String ROUTE_UPDATE = "admin.collections.update";
    private static final String ROUTE_DELETE = "admin.collections.delete";

    private final CollectionService collectionService;

    @Autowired
    public AdminCollectionController(CollectionService collectionService, TranslationService translationService, RoutingService routingService) {
        super(translationService, routingService);
        this.collectionService = collectionService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        final List<Collection> collectionList = collectionService.findAll();
        model.put("collectionList", collectionList);

        return "admin/collection/index";
    }

    @GetMapping(path = "/new", name = ROUTE_NEW)
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("collection")) {
            model.put("collection", new Collection());
        }

        return "admin/collection/edit";
    }

    @PostMapping(path = "/", name = ROUTE_CREATE)
    public String createAction(@ModelAttribute Collection collection, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.save(collection);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("collection", collection);
            return redirect(ROUTE_NEW);
        }

        return redirect(ROUTE_INDEX);
    }

    @GetMapping(path = "/{id}/edit", name = ROUTE_EDIT)
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("collection")) {
            final Collection collection = collectionService.findById(id);
            model.put("collection", collection);
        }

        return "admin/collection/edit";
    }

    @PostMapping(path = "/{id}", name = ROUTE_UPDATE)
    public String updateAction(@ModelAttribute Collection collection, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.save(collection);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("collection", collection);
            return redirect(ROUTE_EDIT, Collections.singletonMap("id", collection.getId()));
        }

        return redirect(ROUTE_INDEX);
    }

    @PostMapping(path = "/{id}/delete", name = ROUTE_DELETE)
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        try {
            collectionService.delete(id);
            addFlashInfoMessage(redirectAttributes, translationService.get("collection.action.delete"));
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
        }

        return redirect(ROUTE_INDEX);
    }
}
