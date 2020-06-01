package ysaak.anima.view.helper;

import com.mitchellbosecke.pebble.template.EvaluationContext;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.view.ViewConstants;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class HtmlUtils {
    private HtmlUtils() { /**/ }

    public static String createHtmlTag(String tagName, Map<String, Object> attributeMap) {
        return createHtmlTag(tagName, attributeMap, null);
    }

    public static String createHtmlTag(String tagName, Map<String, Object> attributeMap, String value) {
        final StringBuilder sb = new StringBuilder("<").append(tagName);

        if (CollectionUtils.isNotEmpty(attributeMap)) {
            String attributes = attributeMap.entrySet().stream()
                    .map(e -> {
                        final String attribute;

                        if (e.getValue() != null) {
                            attribute = e.getKey() + "=\"" + e.getValue() + "\"";
                        }
                        else {
                            attribute = e.getKey();
                        }

                        return attribute;
                    })
                    .collect(Collectors.joining(" "));

            sb.append(" ").append(attributes);
        }

        if (value != null) {
            sb.append(">").append(value).append("</").append(tagName).append(">");
        }
        else {
            sb.append(" />");
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static Optional<String> getInvalidationMessage(final EvaluationContext context, final String errorKey) {
        String errorMessage = null;

        final Object validationErrorVar = context.getVariable(ViewConstants.FLASH_VALIDATION_ERROR_KEY);
        if (validationErrorVar != null) {
            final Map<String, String> validationErrorMap = (Map<String, String>) validationErrorVar;

            if (validationErrorMap.containsKey(errorKey)) {
                errorMessage = validationErrorMap.get(errorKey);
            }
        }

        return Optional.ofNullable(errorMessage);
    }
}
