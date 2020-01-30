package ysaak.anima.view.helper.function.flash;

import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import ysaak.anima.view.ViewConstants;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Map;

@ViewHelper(name = "hasFlashInfoMessage")
public class HasFlashInfoMessageFunction extends AbstractFlashMessageFunction {
    @Override
    public Object execute(Map<String, Object> map, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int i) {
        return hasMessages(evaluationContext, ViewConstants.FLASH_INFO_ATTRIBUTE_KEY);
    }
}
