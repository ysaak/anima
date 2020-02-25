package ysaak.anima.dao.converter.enums;

import com.google.common.collect.ImmutableBiMap;
import ysaak.anima.dao.converter.AbstractEnumDbConverter;
import ysaak.anima.data.ElementSubType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElementSubTypeConverter extends AbstractEnumDbConverter<ElementSubType> {
    public ElementSubTypeConverter() {
        super(
                ImmutableBiMap.<ElementSubType, String>builder()
                            .put(ElementSubType.TV, "TV")
                            .build()
        );
    }
}
