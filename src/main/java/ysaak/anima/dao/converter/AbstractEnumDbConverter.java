package ysaak.anima.dao.converter;

import com.google.common.collect.ImmutableBiMap;

import javax.persistence.AttributeConverter;

public class AbstractEnumDbConverter<E extends Enum<?>> implements AttributeConverter<E, String> {

    private final ImmutableBiMap<E, String> convertMap;

    public AbstractEnumDbConverter(ImmutableBiMap<E, String> convertMap) {
        this.convertMap = convertMap;
    }

    @Override
    public String convertToDatabaseColumn(E e) {
        return this.convertMap.get(e);
    }

    @Override
    public E convertToEntityAttribute(String s) {
        return this.convertMap.inverse().get(s);
    }
}
