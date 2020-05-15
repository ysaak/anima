package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "currentUri")
public class CurrentUriFunction implements Function {
    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return new SafeString(requestAttributes.getRequest().getRequestURI());
    }
}
