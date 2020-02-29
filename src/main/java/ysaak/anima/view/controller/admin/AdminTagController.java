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
import ysaak.anima.IAnimaComponent;
import ysaak.anima.data.Tag;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.ResourceNotFoundException;
import ysaak.anima.service.TagService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.dto.admin.TagEditDto;

import java.util.List;

@Controller
@RequestMapping("/admin/tags")
@Transactional
public class AdminTagController extends AbstractViewController implements IAnimaComponent {

    private final TagService tagService;

    @Autowired
    public AdminTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String indexAction(ModelMap model) {
        List<Tag> tagList = tagService.findAll();
        model.put("tagList", tagList);
        return "admin/tags/index";
    }

    @GetMapping("/new")
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("tag")) {
            model.put("tag", new TagEditDto());
        }

        return "admin/tags/edit";
    }

    @PostMapping("/")
    public String createAction(@ModelAttribute TagEditDto tagDto, final RedirectAttributes redirectAttributes) {
        final Tag tagToSave = converters().convert(tagDto, Tag.class);

        try {
            tagService.save(tagToSave);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("tag", tagDto);
            return "redirect:/admin/tags/new";
        }

        return "redirect:/admin/tags/";
    }

    @GetMapping("/{id}/edit")
    public String editAction(ModelMap model, @PathVariable("id") String id) throws Exception {
        if (!model.containsAttribute("tag")) {
            final Tag tag = tagService.findById(id).orElseThrow(() -> new Exception("Byebye"));

            final TagEditDto tagToEdit = converters().convert(tag, TagEditDto.class);
            model.put("tag", tagToEdit);
        }

        return "admin/tags/edit";
    }

    @PostMapping("/{id}")
    public String updateAction(@ModelAttribute TagEditDto tagDto, final RedirectAttributes redirectAttributes) {
        final Tag tagToSave = converters().convert(tagDto, Tag.class);
        try {
            tagService.save(tagToSave);
        }
        catch (DataValidationException dve) {
            registerValidationErrors(redirectAttributes, dve);
            redirectAttributes.addFlashAttribute("tag", tagDto);
            return "redirect:/admin/tags/new";
        }
        return "redirect:/admin/tags/";
    }

    @PostMapping("/{id}/delete")
    public String deleteAction(ModelMap modelMap, @PathVariable("id") String id) throws ResourceNotFoundException {
        tagService.delete(id);
        return "redirect:/admin/tags/";
    }
}
