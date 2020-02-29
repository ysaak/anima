package ysaak.anima.view.helper.filter;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "nl2br")
public class Nl2BrFilter implements Filter {

    @Override
    public List<String> getArgumentNames() {
        return Collections.emptyList();
    }

    @Override
    public Object apply(Object o, Map<String, Object> map, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int i) throws PebbleException {
        if (o instanceof String) {
            return new SafeString(((String) o).replace("\n", "<br>"));
        }
        else {
            return o;
        }
    }
}
