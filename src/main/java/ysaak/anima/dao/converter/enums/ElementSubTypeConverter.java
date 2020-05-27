package ysaak.anima.dao.converter.enums;

import ysaak.anima.dao.converter.AbstractSerializableEnumConverter;
import ysaak.anima.data.ElementSubType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElementSubTypeConverter extends AbstractSerializableEnumConverter<ElementSubType> {
    public ElementSubTypeConverter() {
        super(ElementSubType.class);
    }
}
