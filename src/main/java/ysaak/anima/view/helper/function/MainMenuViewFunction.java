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

        String currentRoute = routingService.getCurrentRoute().orElse("");

        // TODO check active menu button => routingService.getCurrentRoute();

        StringBuilder menuBuilder = new StringBuilder();

        typeList.stream().map(type -> createNavLink(type, currentRoute)).forEachOrdered(menuBuilder::append);

        return new SafeString(menuBuilder.toString());
    }

    private String createNavLink(ElementType elementType, String currentRoute) {
        String linkName = translationService.get("main-menu.element." + elementType.name());
        String linkHref = routingService.getUrlFor(elementType.getIndexRoute());
        String itemClass= (currentRoute.equals(elementType.getIndexRoute())) ? " active" : "";

        return "<li class=\"nav-item" + itemClass + "\"><a class=\"nav-link\" href=\"" + linkHref + "\">" + linkName + "</a></li>";
    }
}
