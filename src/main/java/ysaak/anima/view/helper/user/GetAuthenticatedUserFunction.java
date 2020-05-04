package ysaak.anima.view.helper.user;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.data.User;
import ysaak.anima.service.UserService;
import ysaak.anima.utils.AuthenticationHolder;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ViewHelper(name = "get_auth_user")
public class GetAuthenticatedUserFunction implements Function {

    private final UserService userService;

    @Autowired
    public GetAuthenticatedUserFunction(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {

        Optional<String> authUserId = AuthenticationHolder.getAuthenticatedUserId();
        User user = null;

        if (authUserId.isPresent()) {
            user = userService.findById(authUserId.get()).orElse(null);
        }

        return user;
    }
}
