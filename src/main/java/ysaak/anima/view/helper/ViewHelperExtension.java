package ysaak.anima.view.helper;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewHelperExtension extends AbstractExtension {
    private final ApplicationContext context;

    private final Map<String, Function> functionMap;

    public ViewHelperExtension(ApplicationContext context) {
        this.context = context;
        this.functionMap = new HashMap<>();

        final Reflections reflections = new Reflections(ViewHelperExtension.class.getPackage().getName());

        final Set<Class<?>> viewHelperClassSet = reflections.getTypesAnnotatedWith(ViewHelper.class);

        for (Class<?> viewHelperClass : viewHelperClassSet) {
            ViewHelper viewHelper = viewHelperClass.getAnnotation(ViewHelper.class);
            Object helperInstance = this.context.getBean(viewHelperClass);

            if (Function.class.isAssignableFrom(viewHelperClass)) {
                this.functionMap.put(viewHelper.name(), (Function) helperInstance);
            }
        }
    }

    @Override
    public Map<String, Function> getFunctions() {
        return this.functionMap;
    }

}
