package ysaak.anima.data.importer;

import ysaak.anima.dao.converter.ISerializableEnum;

public enum Importer implements ISerializableEnum {
    ANIDB("ANIDB")
    ;

    private final String code;

    Importer(String code) {
        this.code = code;
    }

    @Override
    public String serialize() {
        return code;
    }
}
