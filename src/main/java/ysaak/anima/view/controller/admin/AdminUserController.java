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
import ysaak.anima.data.User;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.service.UserService;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.AbstractViewController;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
@Transactional
public class AdminUserController extends AbstractViewController {
    private static final String ROUTE_INDEX = "admin.users.index";
    private static final String ROUTE_NEW = "admin.users.new";
    private static final String ROUTE_CREATE = "admin.users.create";
    private static final String ROUTE_EDIT = "admin.users.edit";
    private static final String ROUTE_UPDATE = "admin.users.update";
    private static final String ROUTE_DELETE = "admin.users.delete";

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService, TranslationService translationService, RoutingService routingService) {
        super(translationService, routingService);
        this.userService = userService;
    }

    @GetMapping(path = "/", name = ROUTE_INDEX)
    public String indexAction(ModelMap model) {
        final List<User> collectionList = userService.findAll();
        model.put("userList", collectionList);

        return "admin/user/index";
    }

    @GetMapping(path = "/new", name = ROUTE_NEW)
    public String newAction(ModelMap model) {
        if (!model.containsAttribute("user")) {
            model.put("user", new User());
        }

        return "admin/user/edit";
    }

    @PostMapping(path = "/", name = ROUTE_CREATE)
    public String createAction(@ModelAttribute User user, final RedirectAttributes redirectAttributes) {
        try {
            userService.save(user);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("user", user);
            return redirect(ROUTE_NEW);
        }

        return redirect(ROUTE_INDEX);
    }

    @GetMapping(path = "/{id}/edit", name = ROUTE_EDIT)
    public String editAction(ModelMap model, @PathVariable("id") String id) {
        if (!model.containsAttribute("user")) {
            final User user = userService.findById(id).orElseThrow(this::notFound);
            model.put("user", user);
        }

        return "admin/user/edit";
    }

    @PostMapping(path = "/{id}", name = ROUTE_UPDATE)
    public String updateAction(@ModelAttribute User user, final RedirectAttributes redirectAttributes) {
        try {
            userService.save(user);
        }
        catch (FunctionalException e) {
            handleFunctionalException(redirectAttributes, e);
            redirectAttributes.addFlashAttribute("user", user);
            return redirect(ROUTE_EDIT, Collections.singletonMap("id", user.getId()));
        }

        return redirect(ROUTE_INDEX);
    }

    @PostMapping(path = "/{id}/delete", name = ROUTE_DELETE)
    public String deleteAction(@PathVariable("id") final String id, final RedirectAttributes redirectAttributes) {
        User collectionToDelete = userService.findById(id).orElseThrow(this::notFound);

        userService.delete(collectionToDelete);
        addFlashInfoMessage(redirectAttributes, translationService.get("user.action.delete"));

        return redirect(ROUTE_INDEX);
    }
}
