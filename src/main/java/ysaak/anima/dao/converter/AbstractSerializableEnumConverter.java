package ysaak.anima.dao.converter;

import com.google.common.collect.ImmutableBiMap;

import javax.persistence.AttributeConverter;

public class AbstractSerializableEnumConverter<E extends Enum<?> & ISerializableEnum> implements AttributeConverter<E, String> {

    private final ImmutableBiMap<E, String> convertMap;

    public AbstractSerializableEnumConverter(Class<E> serializableEnumClass) {
        final ImmutableBiMap.Builder<E, String> builder = ImmutableBiMap.<E, String>builder();

        for (E enumConstant : serializableEnumClass.getEnumConstants()) {
            builder.put(enumConstant, enumConstant.serialize());
        }

        this.convertMap = builder.build();
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
