package ysaak.anima.view.helper.function.form;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.helper.HtmlUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

abstract class AbstractFormHelper implements Function {
    private static final String NAME_ARG = "name";
    private static final String VALUE_ARG = "value";
    private static final String PARAMS_ARG = "param";

    private static final String CLASS_PARAM = "class";
    private static final String FORM_VALIDITY_PARAM = "validity";

    protected final String tagName;
    protected final boolean valueAsAttribute;

    public AbstractFormHelper(String tagName, boolean valueAsAttribute) {
        this.tagName = tagName;
        this.valueAsAttribute = valueAsAttribute;
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList(
                NAME_ARG, VALUE_ARG, PARAMS_ARG
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        final String name = (String) args.get(NAME_ARG);
        final Object value = args.get(VALUE_ARG);

        final Map<String, Object> variableMap;
        if (args.containsKey(PARAMS_ARG)) {
            variableMap = (Map<String, Object>) args.get(PARAMS_ARG);
        }
        else {
            variableMap = new HashMap<>();
        }

        final Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put("id", name);
        attributeMap.put("name", name);

        final Set<String> classSet = extractClassFromParameterMap(variableMap);
        classSet.add("form-control");

        // Check validity
        final String errorKey = (String) variableMap.getOrDefault(FORM_VALIDITY_PARAM, name);
        Optional<String> invalidationMessage = HtmlUtils.getInvalidationMessage(context, errorKey);

        if (invalidationMessage.isPresent()) {
            classSet.add("is-invalid");
        }
        registerClass(classSet);
        attributeMap.put("class", String.join(" ", classSet));

        registerAttributes(attributeMap, variableMap);

        String tag = renderFormTag(attributeMap, value);

        if (invalidationMessage.isPresent()) {
            tag += HtmlUtils.createHtmlTag(
                    "div",
                    Collections.singletonMap("class", "invalid-feedback"),
                    invalidationMessage.get()
            );
        }

        return new SafeString(tag);
    }

    protected void registerClass(final Set<String> classSet) {
        // Override to use
    }

    protected void registerAttributes(final Map<String, String> attributeMap, final Map<String, Object> variableMap) {
        // Override to use
    }

    protected String renderFormTag(final Map<String, String> attributeMap, final Object value) {

        String renderedValue = StringUtils.getNotNull(value != null ? String.valueOf(value) : "");

        if (this.valueAsAttribute) {
            attributeMap.put("value", renderedValue);
            renderedValue = null;
        }

        return HtmlUtils.createHtmlTag(this.tagName, attributeMap, renderedValue);
    }

    private Set<String> extractClassFromParameterMap(final Map<String, Object> variableMap) {
        final Set<String> classSet = new HashSet<>();

        if (variableMap != null && variableMap.containsKey(CLASS_PARAM)) {
            String classVar = (String) variableMap.get(CLASS_PARAM);

            if (classVar.contains(" ")) {
                classSet.addAll(Arrays.asList(classVar.split(" ")));
            }
            else {
                classSet.add(classVar);
            }
        }

        return classSet;
    }
}
