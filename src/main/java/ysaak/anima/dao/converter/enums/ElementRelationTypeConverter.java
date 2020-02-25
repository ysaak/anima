package ysaak.anima.dao.converter.enums;

import ysaak.anima.dao.converter.AbstractSerializableEnumConverter;
import ysaak.anima.data.RelationType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElementRelationTypeConverter extends AbstractSerializableEnumConverter<RelationType> {
    public ElementRelationTypeConverter() {
        super(RelationType.class);
    }
}
