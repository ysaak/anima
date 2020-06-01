package ysaak.anima.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import ysaak.anima.utils.AuthenticationHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class LoggedUserInterceptor extends HandlerInterceptorAdapter {
    private static final List<String> WHITE_LIST_URI_PATTERN = Arrays.asList(
            "\\/auth\\/(.*)",
            "\\/users\\/(.*)\\/(.*)\\.png"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isResourceUri(handler) || isWhiteListedUri(request.getRequestURI())) {
            return super.preHandle(request, response, handler);
        }

        Optional<String> userId = AuthenticationHolder.getAuthenticatedUserId();
        if (!userId.isPresent()) {
            response.sendRedirect("/auth/select");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    private boolean isResourceUri(final Object handler) {
        return handler instanceof ResourceHttpRequestHandler;
    }

    private boolean isWhiteListedUri(final String uri) {
        return WHITE_LIST_URI_PATTERN.stream().anyMatch(uri::matches);
    }
}
