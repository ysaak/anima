package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "mainmenu")
public class MainMenuViewFunction implements Function {

    private final RoutingService routingService;

    @Autowired
    public MainMenuViewFunction(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        return null;
    }


/*
    private String createMenuButton(Module module) {
        StringBuilder sb = new StringBuilder("<a href=\"");

        String path = routingService.getUrlFor(module.getIndexRouteName(), new HashMap<>())
                .orElseThrow(() -> new ViewException("No route found for name " + module.getIndexRouteName()));

        sb.append(path)
                .append("\" class=\"lcars-element rounded\">")
                .append(module.getName())
                .append("</a>");

        return sb.toString();
   }*/
}
