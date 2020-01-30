package ysaak.anima.view.helper.function.flash;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import ysaak.anima.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

abstract class AbstractFlashMessageFunction implements Function {
    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    protected List<String> getMessageList(final EvaluationContext evaluationContext, final String type) {
        return (List<String>) evaluationContext.getVariable(type);
    }

    protected boolean hasMessages(final EvaluationContext evaluationContext, final String type) {
        return CollectionUtils.isNotEmpty(getMessageList(evaluationContext, type));
    }
}
