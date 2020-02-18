package ysaak.anima.dao.converter.enums;

import com.google.common.collect.ImmutableBiMap;
import ysaak.anima.dao.AbstractEnumDbConverter;
import ysaak.anima.data.ElementType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElementTypeConverter extends AbstractEnumDbConverter<ElementType> {
    public ElementTypeConverter() {
        super(
                ImmutableBiMap.<ElementType, String>builder()
                            .put(ElementType.ANIME, ElementType.ANIME.getDbCode())
                            .build()
        );
    }
}
