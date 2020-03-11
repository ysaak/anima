package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "urlFor")
public class UrlForFunction implements Function {
    private static final String ROUTE_NAME_ARG = "routeName";
    private static final String VARIABLE_MAP_ARG = "variables";

    private final RoutingService routingService;

    @Autowired
    public UrlForFunction(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(ROUTE_NAME_ARG, VARIABLE_MAP_ARG);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Map<String, Object> map, PebbleTemplate self, EvaluationContext evaluationContext, int lineNumber) {
        Object object = map.get(ROUTE_NAME_ARG);

        if (object instanceof String) {
            String routeName = (String) object;

            Map<String, Object> variableMap;

            if (map.containsKey(VARIABLE_MAP_ARG)) {
                variableMap = (Map<String, Object>) map.get(VARIABLE_MAP_ARG);
            } else {
                variableMap = new HashMap<>();
            }

            return routingService.getUrlFor(routeName, variableMap);
        }
        else {
            return routingService.getUrlFor(object);
        }
    }
}
