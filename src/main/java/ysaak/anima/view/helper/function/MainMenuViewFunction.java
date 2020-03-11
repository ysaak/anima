package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.data.ElementType;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "main_menu")
public class MainMenuViewFunction implements Function {

    private final RoutingService routingService;
    private final TranslationService translationService;

    @Autowired
    public MainMenuViewFunction(RoutingService routingService, TranslationService translationService) {
        this.routingService = routingService;
        this.translationService = translationService;
    }

    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {

        List<ElementType> typeList = Collections.singletonList(
                ElementType.ANIME
        );

        // TODO check active menu button => routingService.getCurrentRoute();

        StringBuilder menuBuilder = new StringBuilder();

        typeList.stream().map(this::createNavLink).forEachOrdered(menuBuilder::append);

        return new SafeString(menuBuilder.toString());
    }

    private String createNavLink(ElementType elementType) {
        String linkName = translationService.get("main-menu.element." + elementType.name());
        String linkHref = routingService.getUrlFor(elementType.getIndexRoute());
        return "<li class=\"nav-item active\"><a class=\"nav-link\" href=\"" + linkHref + "\">" + linkName + "</a></li>";
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
