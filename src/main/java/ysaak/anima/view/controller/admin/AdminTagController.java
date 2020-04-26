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
import ysaak.anima.data.Tag;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.TagService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.dto.admin.TagEditDto;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/tags")
@Transactional
public class AdminTagController extends AbstractViewController {
    private static final String ROUTE_INDEX = "admin.tags.index";
    private static final String ROUTE_NEW = "admin.tags.new";
    private static final String ROUTE_CREATE = "admin.tags.create";
    private static final String ROUTE_EDIT = "admin.tags.edit";
    private static final String ROUTE_UPDATE = "admin.tags.update";
    private static final String ROUTE_DELETE = "admin.tags.delete";

    private final TagService tagService;

    @Autowired
    public AdminTagController(TagService tagService, RoutingService routingService, TranslationService translationService) {
        super(translationService, routingService);
        this.tagService = tagService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        List<Tag> tagList = tagService.findAll();
        model.put("tagList", tagList);
        return "admin/tags/index";
    }

    @GetMapping(path = "/new", name = ROUTE_NEW)
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("tag")) {
            model.put("tag", new TagEditDto());
        }

        return "admin/tags/edit";
    }

    @PostMapping(path = "/", name = ROUTE_CREATE)
    public String createAction(@ModelAttribute TagEditDto tagDto, final RedirectAttributes redirectAttributes) {
        final Tag tagToSave = converters().convert(tagDto, Tag.class);

        try {
            tagService.save(tagToSave);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("tag", tagDto);
            return redirect(ROUTE_NEW);
        }

        return redirect(ROUTE_INDEX);
    }

    @GetMapping(path = "/{id}/edit", name = ROUTE_EDIT)
    public String editAction(ModelMap model, @PathVariable("id") String id) throws FunctionalException {
        if (!model.containsAttribute("tag")) {
            final Tag tag = tagService.findById(id);

            final TagEditDto tagToEdit = converters().convert(tag, TagEditDto.class);
            model.put("tag", tagToEdit);
        }

        return "admin/tags/edit";
    }

    @PostMapping(path = "/{id}", name = ROUTE_UPDATE)
    public String updateAction(@ModelAttribute TagEditDto tagDto, @PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        final Tag tagToSave = converters().convert(tagDto, Tag.class);
        try {
            tagService.save(tagToSave);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("tag", tagDto);
            return redirect(ROUTE_EDIT, Collections.singletonMap("id", tagDto.getId()));
        }

        return redirect(ROUTE_INDEX);
    }

    @PostMapping(path = "/{id}/delete", name = ROUTE_DELETE)
    public String deleteAction(@PathVariable("id") String id, final RedirectAttributes redirectAttributes) throws FunctionalException {
        tagService.delete(id);
        addFlashInfoMessage(redirectAttributes, translationService.get("tag.action.delete"));
        return redirect(ROUTE_INDEX);
    }
}
