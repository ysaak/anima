package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.controller.CollectionController;
import ysaak.anima.view.controller.TitleController;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.Arrays;
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
        final List<MenuItem> itemList = Arrays.asList(
            new MenuItem("title", TitleController.ROUTE_TITLES_INDEX),
            new MenuItem("collection", CollectionController.ROUTE_COLLECTIONS_INDEX)
        );

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String currentUri = requestAttributes.getRequest().getRequestURI();

        StringBuilder menuBuilder = new StringBuilder();

        itemList.stream().map(type -> createNavLink(type, currentUri)).forEachOrdered(menuBuilder::append);

        return new SafeString(menuBuilder.toString());
    }

    private String createNavLink(MenuItem item, final String currentUri) {
        String linkName = translationService.get("main-menu.element." + item.name);
        String linkHref = routingService.getUrlFor(item.route);
        String itemClass= currentUri.startsWith(linkHref) ? " active" : "";

        return "<li class=\"nav-item" + itemClass + "\"><a class=\"nav-link\" href=\"" + linkHref + "\">" + linkName + "</a></li>";
    }

    private static class MenuItem {
        private final String name;
        private final String route;

        public MenuItem(String name, String route) {
            this.name = name;
            this.route = route;
        }
    }
}
