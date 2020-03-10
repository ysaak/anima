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
import org.springframework.web.bind.annotation.RestController;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementType;
import ysaak.anima.exception.TechnicalException;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RoutingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoutingService.class);

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([0-9a-zA-Z\\-_]+)}");

    private static final String PATH_DELIMITER = "/";

    private final Map<String, Route> routeMap;

    public RoutingService() {
        this.routeMap = new HashMap<>();
    }

    @PostConstruct
    private void init() {
        LOGGER.debug("Start of named route analysis");
        long duration = System.currentTimeMillis();

        final Reflections reflections = new Reflections("ysaak.anima");

        reflections.getTypesAnnotatedWith(Controller.class).forEach(this::loadRouteFromController);
        reflections.getTypesAnnotatedWith(RestController.class).forEach(this::loadRouteFromController);

        if (LOGGER.isDebugEnabled()) {
            routeMap.values().forEach(route -> LOGGER.debug(
                    "Found new named route '{}' with path {} and params {}",
                    route.getName(),
                    route.getPath(),
                    route.getParamList()
            ));
        }

        LOGGER.debug("End of named route analysis ; {} routes found in {} ms", routeMap.size(), (System.currentTimeMillis() - duration));
    }

    private void loadRouteFromController(final Class<?> controllerClass) {
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = controllerClass.getAnnotation(RequestMapping.class);
            String path = mapping.value()[0];

            if (!path.startsWith(PATH_DELIMITER)) {
                path = PATH_DELIMITER + path;
            }
            if (!path.endsWith(PATH_DELIMITER)) {
                path += PATH_DELIMITER;
            }

            final String basePath = path;

            Arrays.stream(controllerClass.getMethods())
                    .map(this::extractAnnotationData)
                    .filter(Objects::nonNull)
                    .map(data -> createRoute(basePath, data))
                    .forEach(route -> routeMap.put(route.getName(), route));
        }
    }

    private Route createRoute(final String basePath, final AnnotationData annotationData) {
        final String fullPath = basePath + (annotationData.path.startsWith(PATH_DELIMITER) ? annotationData.path.substring(1) : annotationData.path);
        final List<String> paramList = new ArrayList<>();

        final Matcher matcher = PATH_VARIABLE_PATTERN.matcher(annotationData.path);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                paramList.add(matcher.group(i));
            }
        }

        return new Route(annotationData.name, fullPath, paramList);
    }

    private AnnotationData extractAnnotationData(Method method) {
        AnnotationData annotationData = null;

        if (method.isAnnotationPresent(GetMapping.class)) {
            final GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (StringUtils.isNotEmpty(mapping.name())) {
                annotationData = new AnnotationData(mapping.name(), mapping.path()[0]);
            }
        }
        else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            if (StringUtils.isNotEmpty(mapping.name())) {
                annotationData = new AnnotationData(mapping.name(), mapping.path()[0]);
            }
        }
        else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            if (StringUtils.isNotEmpty(mapping.name())) {
                annotationData = new AnnotationData(mapping.name(), mapping.path()[0]);
            }
        }

        return annotationData;
    }

    public String getElementPath(Element element) {
        Preconditions.checkNotNull(element);

        return getElementPath(element.getType(), element.getId());
    }

    public String getElementPath(ElementType type, String id) {
        return "/" + type.getPathName() + "/" + id;
    }

    public Optional<String> getUrlFor(String routeName, Map<String, Object> parameters) {
        String path = null;

        if (routeMap.containsKey(routeName)) {

            final Route route = routeMap.get(routeName);

            path = route.getPath();

            List<Map.Entry<String, Object>> pathParamList = parameters.entrySet().stream()
                    .filter(e -> route.getParamList().contains(e.getKey()))
                    .collect(Collectors.toList());

            for (Map.Entry<String, Object> param : pathParamList) {
                path = path.replace("{" + param.getKey() + "}", param.getValue().toString());
            }

            final Map<String, Object> queryParamMap = parameters.entrySet().stream()
                    .filter(e -> !route.getParamList().contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (CollectionUtils.isNotEmpty(queryParamMap)) {
                String queryParam = Joiner.on("&").withKeyValueSeparator("=").join(queryParamMap);

                path += "?" + queryParam;

            }
        }

        return Optional.ofNullable(path);
    }

    public String redirectUrl(final String routeName) {
        return this.redirectUrl(routeName, Collections.emptyMap());
    }

    public String redirectUrl(final String routeName, final Map<String, Object> parameters) {
        Preconditions.checkNotNull(routeName);
        Preconditions.checkNotNull(parameters);

        return "redirect:" + getUrlFor(routeName, parameters)
                .orElseThrow(() -> new TechnicalException("Unknown route " + routeName));
    }

    public String redirectUrl(final Element element) {
        return "redirect:" + this.getElementPath(element);
    }

    private static class AnnotationData {
        private final String name;
        private final String path;

        public AnnotationData(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }
}
