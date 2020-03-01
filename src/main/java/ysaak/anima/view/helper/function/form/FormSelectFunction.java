package ysaak.anima.view.helper.function.form;

import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.view.dto.KeyValueItem;
import ysaak.anima.view.helper.HtmlUtils;
import ysaak.anima.view.helper.ViewHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ViewHelper(name = "formSelect")
public class FormSelectFunction extends AbstractFormHelper {
    protected static final String ITEM_LIST_ARG = "itemList";

    private static final String OPTIONS_ATTR = "options";

    public FormSelectFunction() {
        super("select", false);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(
                NAME_ARG, ITEM_LIST_ARG, VALUE_ARG, PARAMS_ARG
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void registerAttributes(Map<String, Object> attributeMap, Map<String, Object> argMap, Map<String, Object> variableMap) {
        final List<KeyValueItem> itemList = (List<KeyValueItem>) argMap.get(ITEM_LIST_ARG);
        attributeMap.put(OPTIONS_ATTR, itemList);

        if (variableMap.containsKey("multiple")) {
            attributeMap.put("multiple", null);
        }
    }

    @Override
    protected void registerClass(Set<String> classSet) {
        classSet.remove("form-control");
        classSet.add("custom-select");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String renderFormTag(Map<String, Object> attributeMap, Object value) {
        final List<KeyValueItem> itemList = (List<KeyValueItem>) attributeMap.get(OPTIONS_ATTR);
        attributeMap.remove(OPTIONS_ATTR);

        final List<Object> valueList = new ArrayList<>();
        if (value != null) {
            if (value instanceof Collection) {
                valueList.addAll((Collection<Object>) value);
            }
            else {
                valueList.add(value);
            }
        }

        final StringBuilder optionsBuilder = new StringBuilder();

        if (CollectionUtils.isNotEmpty(itemList)) {
            for (KeyValueItem item : itemList) {

                Map<String, Object> optAttrMap = new HashMap<>();
                optAttrMap.put("value", item.getKey());

                if (valueList.contains(item.getKey())) {
                    optAttrMap.put("selected", null);
                }

                optionsBuilder.append(HtmlUtils.createHtmlTag("option", optAttrMap, item.getValue()));
            }
        }

        return super.renderFormTag(attributeMap, optionsBuilder.toString());
    }
}
