package ysaak.anima.view.helper.user;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.config.StorageConfig;
import ysaak.anima.data.storage.StorageFormat;
import ysaak.anima.data.storage.StorageType;
import ysaak.anima.exception.ViewException;
import ysaak.anima.view.controller.UserController;
import ysaak.anima.view.helper.HtmlUtils;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "user_image")
public class UserImageFunction implements Function {
    private static final String USER_ID_ARG = "user_id";
    private static final String FORMAT_ARG = "format";
    private static final String CLASS_ARG = "class";

    private static final String IMAGE_ROUTE_NAME = UserController.ROUTE_USER_IMAGE;

    private final RoutingService routingService;
    private final StorageConfig storageConfig;

    @Autowired
    public UserImageFunction(RoutingService routingService, StorageConfig storageConfig) {
        this.routingService = routingService;
        this.storageConfig = storageConfig;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(
            USER_ID_ARG, FORMAT_ARG, CLASS_ARG
        );
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        final String userId = (String) args.get(USER_ID_ARG);
        final String requestFormat = (String) args.get(FORMAT_ARG);
        final String tagClass = (String) args.get(CLASS_ARG);

        final StorageFormat format;
        try {
            format = StorageFormat.valueOf(requestFormat.toUpperCase());
        }
        catch (IllegalArgumentException e) {
            throw new ViewException("Cannot match input format ('" + requestFormat + "') with available list", self.getName(), lineNumber);
        }

        StorageConfig.Size imageSize = storageConfig.get(StorageType.USER, format)
            .orElseThrow(() -> new ViewException("Cannot format ('" + format + "') not available for type '" + StorageType.USER + "'", self.getName(), lineNumber));

        Map<String, Object> imgAttributeMap = new HashMap<>(3);
        imgAttributeMap.put("src", getImageUrl(userId, requestFormat));
        imgAttributeMap.put("width", imageSize.getWidth());
        imgAttributeMap.put("height", imageSize.getHeight());
        imgAttributeMap.put("class", tagClass);

        return new SafeString(HtmlUtils.createHtmlTag("img", imgAttributeMap));
    }

    private String getImageUrl(String id, String format) {
        Map<String, Object> routeArgMap = new HashMap<>(2);
        routeArgMap.put("id", id);
        routeArgMap.put("size", format);

        return routingService.getUrlFor(IMAGE_ROUTE_NAME, routeArgMap);
    }
}
