package ysaak.anima.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ysaak.anima.api.dto.AutoCompleteDto;
import ysaak.anima.data.Element;
import ysaak.anima.service.ElementService;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/autocomplete")
public class AutoCompleteApiController {

    private final ElementService elementService;

    @Autowired
    public AutoCompleteApiController(ElementService elementService) {
        this.elementService = elementService;
    }

    @GetMapping(path = "/elements", name = "api.autocomplete.elements")
    @ResponseBody
    public List<AutoCompleteDto> autocompleteElementsAction(@RequestParam("search") final String searchText) {
        final List<AutoCompleteDto> response = new ArrayList<>();

        if (StringUtils.isNotBlank(searchText)) {
            final List<Element> elementList = elementService.searchByTitle(searchText);

            if (CollectionUtils.isNotEmpty(elementList)) {
                elementList.stream().map(e -> new AutoCompleteDto(e.getId(), e.getTitle())).forEach(response::add);
            }
        }

        return response;
    }
}
