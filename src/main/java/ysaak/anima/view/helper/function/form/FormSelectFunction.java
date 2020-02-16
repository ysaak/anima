package ysaak.anima.view.helper.function.form;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.dto.KeyValueItem;
import ysaak.anima.view.helper.ViewHelper;
import ysaak.anima.view.router.RoutingService;

import java.util.*;

@ViewHelper(name = "formSelect")
public class FormSelectFunction implements Function {

    private final RoutingService routingService;

    @Autowired
    public FormSelectFunction(RoutingService routingService) {
        this.routingService = routingService;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(
                "name", "itemList", "selectedItem"
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {

        final String name = (String) args.get("name");
        final List<KeyValueItem> itemList = (List<KeyValueItem>) args.get("itemList");
        final String selectedItem = StringUtils.getNotNull((String) args.get("selectedItem"));

        StringBuilder sb = new StringBuilder();
        sb.append("<select name=\"").append(name).append("\" class=\"custom-select\">");

        if (CollectionUtils.isNotEmpty(itemList)) {
            for (KeyValueItem item : itemList) {
                sb.append("<option value=\"").append(item.getKey()).append("\"");

                if (Objects.equals(selectedItem, item.getKey())) {
                    sb.append(" selected");
                }

                sb.append(">").append(item.getValue()).append("</option>");
            }
        }

        sb.append("</select>");

        return new SafeString(sb.toString());
    }
}
