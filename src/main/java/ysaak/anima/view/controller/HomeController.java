package ysaak.anima.view.controller;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.User;
import ysaak.anima.service.ElementService;
import ysaak.anima.service.UserService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.view.dto.elements.list.ElementListDto;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractViewController {
    private static final String ROUTE_HOME = "home";
    private static final String ROUTE_USER_SELECT = "user.select";
    private static final String ROUTE_USER_CHANGE = "user.change";

    private final ElementService elementService;
    private final UserService userService;

    @Autowired
    public HomeController(final TranslationService translationService, final RoutingService routingService, final ElementService elementService, final UserService userService) {
        super(translationService, routingService);
        this.elementService = elementService;
        this.userService = userService;
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

    @GetMapping(path = "/select-user", name = ROUTE_USER_SELECT)
    public String selectUserAction(final ModelMap model) {

        AuthenticationHolder.removeAuthenticatedUser();

        final List<User> userList = userService.findAll();
        userList.sort(Comparator.comparing(User::getName));

        model.put("userList", userList);

        return "select_user";
    }

    @PostMapping(path = "/select-user/{id}", name = ROUTE_USER_CHANGE)
    public String storeSelectedUserAction(@PathVariable("id") final String id, RedirectAttributes redirectAttributes) {
        final User user = userService.findById(id).orElseThrow(this::notFound);

        AuthenticationHolder.setAuthenticatedUser(user);

        return redirect(ROUTE_HOME);
    }
}
