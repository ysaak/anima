package ysaak.anima.view.helper.function.flash;

import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import ysaak.anima.view.ViewConstants;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Map;

@ViewHelper(name = "getFlashErrorMessage")
public class GetFlashErrorMessageFunction extends AbstractFlashMessageFunction {
    @Override
    public Object execute(Map<String, Object> map, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int i) {
        return getMessageList(evaluationContext, ViewConstants.FLASH_ERROR_ATTRIBUTE_KEY);
    }
}
