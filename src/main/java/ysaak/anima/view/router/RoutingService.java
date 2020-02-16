package ysaak.anima.view.router;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ysaak.anima.data.Element;
import ysaak.anima.utils.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoutingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingService.class);

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([0-9a-zA-Z\\-_]+)\\}");

    private static final String PATH_DELIMITER = "/";

    private final Map<String, Route> routeMap;

    public RoutingService() {
        this.routeMap = new HashMap<>();
    }

    @PostConstruct
    private void init() {
        LOGGER.debug("Start of named route analysis");
        final Reflections reflections = new Reflections("ysaak.anima");
        final Set<Class<?>> controllerClassSet = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClassSet) {

            final String basePath;
            if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
                String path = mapping.value()[0];

                if (!path.startsWith(PATH_DELIMITER)) {
                    path = PATH_DELIMITER + path;
                }
                if (!path.endsWith(PATH_DELIMITER)) {
                    path += PATH_DELIMITER;
                }

                basePath = path;
            }
            else {
                basePath = "";
            }

            Arrays.stream(controllerClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(NamedRoute.class))
                    .map(method -> createRoute(basePath, method))
                    .filter(Objects::nonNull)
                    .forEach(route -> routeMap.put(route.name, route));
        }

        LOGGER.debug("End of named route analysis");
    }

    private Route createRoute(String basePath, Method method) {
        final NamedRoute namedRoute = method.getAnnotation(NamedRoute.class);
        final String name = namedRoute.value();

        String path = getPath(method);

        if (path != null) {
            final String fullPath = basePath + (path.startsWith(PATH_DELIMITER) ? path.substring(1) : path);
            final List<String> paramList = new ArrayList<>();

            final Matcher matcher = PATH_VARIABLE_PATTERN.matcher(path);

            while (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    paramList.add(matcher.group(i));
                }
            }

            LOGGER.debug("Found new named route '{}' with path {} and params {}", name, fullPath, paramList);

            return new Route(name, fullPath, paramList);
        }
        else {
            LOGGER.warn("No path found for route {}", name);
            return null;
        }
    }

    private String getPath(Method method) {
        final String path;

        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            path = mapping.value()[0];
        }
        else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            path = mapping.value()[0];
        }
        else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            path = mapping.value()[0];
        }
        else {
            path = null;
        }

        return path;
    }

    public String getElementPath(Element element) {
        Preconditions.checkNotNull(element);

        return "/" + element.getType().getPathName() + "/" + element.getId();
    }

    public Optional<String> getUrlFor(String routeName, Map<String, Object> parameters) {
        String path = null;

        if (routeMap.containsKey(routeName)) {

            final Route route = routeMap.get(routeName);

            path = route.path;

            List<Map.Entry<String, Object>> pathParamList = parameters.entrySet().stream()
                    .filter(e -> route.params.contains(e.getKey()))
                    .collect(Collectors.toList());

            for (Map.Entry<String, Object> param : pathParamList) {
                path = path.replace("{" + param.getKey() + "}", param.getValue().toString());
            }

            final Map<String, Object> queryParamMap = parameters.entrySet().stream()
                    .filter(e -> !route.params.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (CollectionUtils.isNotEmpty(queryParamMap)) {
                String queryParam = Joiner.on("&").withKeyValueSeparator("=").join(queryParamMap);

                path += path + "?" + queryParam;

            }
        }

        return Optional.ofNullable(path);
    }
}
