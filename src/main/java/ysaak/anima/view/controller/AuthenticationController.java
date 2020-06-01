package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ysaak.anima.data.User;
import ysaak.anima.service.UserService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.router.RoutingService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends AbstractViewController {
    private static final String ROUTE_AUTH_SELECT = "auth.select";
    private static final String ROUTE_AUTH_CHANGE = "auth.change";

    private final UserService userService;

    @Autowired
    public AuthenticationController(TranslationService translationService, RoutingService routingService, UserService userService) {
        super(translationService, routingService);
        this.userService = userService;
    }

    @GetMapping(path = "/select", name = ROUTE_AUTH_SELECT)
    public String selectUserAction(final ModelMap model) {

        AuthenticationHolder.removeAuthenticatedUser();

        final List<User> userList = userService.findAll();
        userList.sort(Comparator.comparing(User::getName));

        model.put("userList", userList);

        return "select_user";
    }

    @PostMapping(path = "/change", name = ROUTE_AUTH_CHANGE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String storeSelectedUserAction(@RequestParam Map<String, String> formData, RedirectAttributes redirectAttributes) {

        String selectedUserId = formData.get("id");
        Optional<User> user = Optional.empty();

        if (StringUtils.isNotBlank(selectedUserId)) {
            user = userService.findById(selectedUserId);
        }

        if (user.isPresent()) {
            AuthenticationHolder.setAuthenticatedUser(user.get());
            return redirect(HomeController.ROUTE_HOME);
        }
        else {
            return redirect(ROUTE_AUTH_SELECT);
        }

    }
}
