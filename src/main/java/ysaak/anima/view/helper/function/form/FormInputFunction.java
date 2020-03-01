package ysaak.anima.view.helper.function.form;

import ysaak.anima.view.helper.ViewHelper;

import java.util.Map;

@ViewHelper(name = "formInput")
public class FormInputFunction extends AbstractFormHelper {

    public FormInputFunction() {
        super("input", true);
    }

    @Override
    protected void registerAttributes(final Map<String, Object> attributeMap, final Map<String, Object> argMap, final Map<String, Object> variableMap) {
        final String type;
        if (variableMap.containsKey("type")) {
            type = (String) variableMap.get("type");
        }
        else {
            type = "text";
        }

        attributeMap.put("type", type);
    }
}
