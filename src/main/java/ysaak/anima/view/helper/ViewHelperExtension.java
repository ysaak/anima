package ysaak.anima.view.helper;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewHelperExtension extends AbstractExtension {

    private final Map<String, Function> functionMap;
    private final Map<String, Filter> filterMap;

    public ViewHelperExtension(final ApplicationContext context) {
        this.functionMap = new HashMap<>();
        this.filterMap = new HashMap<>();

        final Reflections reflections = new Reflections(ViewHelperExtension.class.getPackage().getName());

        final Set<Class<?>> viewHelperClassSet = reflections.getTypesAnnotatedWith(ViewHelper.class);

        for (Class<?> viewHelperClass : viewHelperClassSet) {
            ViewHelper viewHelper = viewHelperClass.getAnnotation(ViewHelper.class);
            Object helperInstance = context.getBean(viewHelperClass);

            if (Function.class.isAssignableFrom(viewHelperClass)) {
                this.functionMap.put(viewHelper.name(), (Function) helperInstance);
            }
            else if (Filter.class.isAssignableFrom(viewHelperClass)) {
                this.filterMap.put(viewHelper.name(), (Filter) helperInstance);
            }
        }
    }

    @Override
    public Map<String, Function> getFunctions() {
        return this.functionMap;
    }

    @Override
    public Map<String, Filter> getFilters() {
        return this.filterMap;
    }
}
