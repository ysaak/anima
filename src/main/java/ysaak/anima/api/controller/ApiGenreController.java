package ysaak.anima.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ysaak.anima.data.Tag;
import ysaak.anima.service.TagService;

@RestController
@RequestMapping("/genres")
public class ApiGenreController extends AbstractCrudController<Tag, TagService> {

    @Autowired
    public ApiGenreController(TagService tagService) {
        super(tagService);
    }

}
