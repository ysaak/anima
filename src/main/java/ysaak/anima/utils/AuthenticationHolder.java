package ysaak.anima.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ysaak.anima.data.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public final class AuthenticationHolder {
    private AuthenticationHolder() { /**/ }

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHolder.class);
    private static final String SESSION_USER_KEY = "anima.auth.user";

    public static Optional<String> getAuthenticatedUserId() {
        HttpServletRequest request = getCurrentHttpRequest();

        String userId = null;
        if (request != null) {
            userId = (String) request.getSession().getAttribute(SESSION_USER_KEY);
        }

        return Optional.ofNullable(userId);
    }

    public static void removeAuthenticatedUser() {
        HttpServletRequest request = getCurrentHttpRequest();

        if (request != null) {
            request.getSession().invalidate();
        }
    }

    public static void setAuthenticatedUser(final User user) {
        Preconditions.checkNotNull(user, "user is null");

        HttpServletRequest request = getCurrentHttpRequest();
        if (request != null) {
            request.getSession().setAttribute(SESSION_USER_KEY, user.getId());
        }
    }

    private static HttpServletRequest getCurrentHttpRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes)requestAttributes).getRequest();
        }

        LOGGER.debug("Not called in the context of an HTTP request");
        return null;
    }
}
