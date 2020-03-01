package ysaak.anima.view.helper.function.form;

import ysaak.anima.view.helper.ViewHelper;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ViewHelper(name = "formTextArea")
public class FormTextAreaFunction extends AbstractFormHelper {

    private static final String SIZE_PARAM = "size";
    private static final Pattern SIZE_PATTERN = Pattern.compile("([1-9][0-9]*)x([1-9][0-9]*)");

    public FormTextAreaFunction() {
        super("textarea", false);
    }

    @Override
    protected void registerAttributes(final Map<String, Object> attributeMap, final Map<String, Object> argMap, final Map<String, Object> variableMap) {
        if (variableMap.containsKey(SIZE_PARAM)) {
            Matcher sizeMatcher = SIZE_PATTERN.matcher((String) variableMap.get(SIZE_PARAM));
            if (sizeMatcher.matches()) {
                attributeMap.put("cols", sizeMatcher.group(1));
                attributeMap.put("rows", sizeMatcher.group(2));
            }
        }
    }
}
