package ysaak.anima.converter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;
import ysaak.anima.AnimaApplication;
import ysaak.anima.exception.TechnicalException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterService.class);

    private final Table<Class<?>, Class<?>, Class<?>> discoveredConverterTable = HashBasedTable.create();
    private final Table<Class<?>, Class<?>, ? super AbstractConverter<?,?>> loadedConverterTable = HashBasedTable.create();

    @PostConstruct
    public void init() {
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Converter.class));
        scanner.addIncludeFilter(new AssignableTypeFilter(AbstractConverter.class));

        for (BeanDefinition beanDef : scanner.findCandidateComponents(AnimaApplication.class.getPackage().getName())) {

            final Class<?> cl;
            try {
                cl = Class.forName(beanDef.getBeanClassName());
            }
            catch (ClassNotFoundException e) {
                LOGGER.error("Error while loading class " + beanDef.getBeanClassName());
                continue;
            }

            if (cl.isAnnotationPresent(Converter.class)) {
                Converter metadata = cl.getAnnotation(Converter.class);
                discoveredConverterTable.put(metadata.from(), metadata.to(), cl);
                LOGGER.debug("Converter discovered {} [from={}, to={}]", beanDef.getBeanClassName(), metadata.from().getSimpleName(), metadata.to().getSimpleName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <E, D> AbstractConverter<E, D> getConverter(Class<E> from, Class<D> to) {
        AbstractConverter<E, D> converter = (AbstractConverter<E, D>) loadedConverterTable.get(from, to);

        if (converter == null) {
            // Load converter
            Class<?> converterClass = discoveredConverterTable.get(from, to);
            if (converterClass == null) {
                throw new TechnicalException("No converter found. Requested type : from=" + from.getSimpleName() + " to=" + to.getSimpleName());
            }
            try {
                converter = (AbstractConverter<E, D>) converterClass.newInstance();
                converter.setConverterService(this);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new TechnicalException("Error while creating instance of converter " + converterClass.getCanonicalName(), e);
            }

            loadedConverterTable.put(from, to, converter);
        }

        return converter;
    }

    @SuppressWarnings("unchecked")
    public <F, T> T convert(F object, Class<T> toClass) {
        T convertedObject = null;

        if (object != null) {
            Class<F> fromClass = (Class<F>) object.getClass();
            convertedObject = getConverter(fromClass, toClass).convert(object);
        }

        return convertedObject;
    }

    @SuppressWarnings("unchecked")
    public <F, T> List<T> convert(List<F> objectList, Class<T> toClass) {
        List<T> convertedObjectList = null;

        if (objectList != null) {
            if (!objectList.isEmpty()) {
                Class<F> fromClass = (Class<F>) objectList.get(0).getClass();
                convertedObjectList = getConverter(fromClass, toClass).convert(objectList);
            }
            else {
                convertedObjectList = new ArrayList<>();
            }
        }

        return convertedObjectList;
    }
}
