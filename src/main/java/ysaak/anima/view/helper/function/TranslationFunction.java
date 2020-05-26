package ysaak.anima.view.helper.function;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ysaak.anima.exception.ViewException;
import ysaak.anima.service.technical.TranslationService;
import ysaak.anima.view.helper.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ViewHelper(name = "__")
public class TranslationFunction implements Function {

    private final TranslationService translationService;

    @Autowired
    public TranslationFunction(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext evaluationContext, int lineNumber) {
        int i = 1;

        if (args.containsKey("0")) {
            String translationKey = (String) args.get("0");

            List<Object> translationArgs = new ArrayList<>();
            while (args.containsKey(String.valueOf(i))) {
                translationArgs.add(args.get(String.valueOf(i)));
            }

            return translationService.get(translationKey, translationArgs.toArray());
        }
        else {
            throw new ViewException("The translation function require at least 1 argument", self.getName(), lineNumber);
        }
    }
}
