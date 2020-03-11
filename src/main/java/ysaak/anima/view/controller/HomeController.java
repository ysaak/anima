package ysaak.anima.view.controller;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.service.ElementService;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.router.RoutingService;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ElementService elementService;
    private final RoutingService routingService;

    @Autowired
    public HomeController(ElementService elementService, RoutingService routingService) {
        this.elementService = elementService;
        this.routingService = routingService;
    }

    @GetMapping("/")
    public String indexAction(ModelMap model) {
        return "index";
    }

    @GetMapping(path = "/search", name = "search")
    public String searchAction(final ModelMap model, String search) {
        model.put("search_text", search);

        final List<Element> elementList = elementService.searchByTitle(search);
        final Multimap<ElementType, ElementListDto> elementMap = ArrayListMultimap.create();


        for (Element element : elementList) {
            ElementListDto dto = convertToDto(element);
            elementMap.put(element.getType(), dto);
        }

        model.put("animeList", elementMap.get(ElementType.ANIME));

        return "search";
    }

    private ElementListDto convertToDto(Element element) {
        return new ElementListDto(
                element.getId(),
                element.getTitle(),
                routingService.getUrlFor(element)
        );
    }
}
