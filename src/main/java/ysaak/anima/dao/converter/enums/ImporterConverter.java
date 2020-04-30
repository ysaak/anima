package ysaak.anima.dao.converter.enums;

import ysaak.anima.dao.converter.AbstractSerializableEnumConverter;
import ysaak.anima.data.importer.Importer;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ImporterConverter extends AbstractSerializableEnumConverter<Importer> {
    public ImporterConverter() {
        super(Importer.class);
    }
}
