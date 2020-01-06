package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.view.helper.AssetResolver;
import ysaak.anima.view.helper.ViewHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "asset")
public class AssetViewFunction implements Function {
    private final AssetResolver resolver;

    @Autowired
    public AssetViewFunction(AssetResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<String> getArgumentNames() {
        return Collections.singletonList("path");
    }

    @Override
    public Object execute(Map<String, Object> map, PebbleTemplate pebbleTemplate, EvaluationContext evaluationContext, int i) {

        String path = map.get("path").toString();

        return resolver.resolve(path);
    }
}
