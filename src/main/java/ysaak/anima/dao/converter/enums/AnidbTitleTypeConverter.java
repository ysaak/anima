package ysaak.anima.dao.converter.enums;

import ysaak.anima.dao.converter.AbstractSerializableEnumConverter;
import ysaak.anima.data.importer.anidb.AnidbTitleType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class AnidbTitleTypeConverter extends AbstractSerializableEnumConverter<AnidbTitleType> {
    public AnidbTitleTypeConverter() {
        super(AnidbTitleType.class);
    }
}
