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
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractViewController {
    public static final String ROUTE_HOME = "home";

    private final ElementService elementService;

    @Autowired
    public HomeController(final TranslationService translationService, final RoutingService routingService, final ElementService elementService) {
        super(translationService, routingService);
        this.elementService = elementService;
    }

    @GetMapping(path = "/", name = ROUTE_HOME)
    public String indexAction(ModelMap model) {
        String userId = AuthenticationHolder.getAuthenticatedUserId().orElseThrow(this::notFound);

        return this.forward(UserController.ROUTE_USER_VIEW, Collections.singletonMap("id", userId));
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
                element.getTitle()
        );
    }
}
