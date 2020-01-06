package ysaak.anima.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractConverter<D, E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConverter.class);

    protected ConverterService converterService;

    void setConverterService(ConverterService converterService) {
        this.converterService = converterService;
    }

    public E convert(D object) {
        return (object != null) ? safeConvert(object) : null;
    }

    protected abstract E safeConvert(D object);

    public List<E> convert(List<D> objectList) {
        if (objectList == null) {
            return Collections.emptyList();
        }

        List<E> convertedObjectList = new ArrayList<>();

        if (!objectList.isEmpty()) {
            objectList.stream().map(this::convert).filter(Objects::nonNull).forEach(convertedObjectList::add);
        }

        return convertedObjectList;
    }

    protected String fromEnum(Enum<?> enumData) {
        String result = null;

        if (enumData != null) {
            result = enumData.name();
        }

        return result;
    }

    protected <T extends Enum<T>> T toEnum(String data, Class<T> enumType) {
        T result = null;
        try {
            result = Enum.valueOf(enumType, data);
        }
        catch (IllegalArgumentException e) {
            LOGGER.error("Value '" + data + "' is not part of " + enumType.getName(), e);
        }

        return result;
    }
}
