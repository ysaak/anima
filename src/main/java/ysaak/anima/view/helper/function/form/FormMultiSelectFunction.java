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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ViewHelper(name = "formMultiSelect")
public class FormMultiSelectFunction implements Function {
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
        final List<String> selectedItem = CollectionUtils.getNotNull((List<String>) args.get("selectedItem"));

        StringBuilder sb = new StringBuilder();
        sb.append("<select multiple id=\"")
                .append(name)
                .append("\" name=\"")
                .append(name)
                .append("\" class=\"custom-select\">");

        if (CollectionUtils.isNotEmpty(itemList)) {
            for (KeyValueItem item : itemList) {
                sb.append("<option value=\"").append(item.getKey()).append("\"");

                if (selectedItem.contains(item.getKey())) {
                    sb.append(" selected");
                }

                sb.append(">").append(item.getValue()).append("</option>");
            }
        }

        sb.append("</select>");

        return new SafeString(sb.toString());
    }
}
