package ysaak.anima.data;

import ysaak.anima.dao.converter.ISerializableEnum;

public enum ElementSubType implements ISerializableEnum {
    UNDETERMINED("UN"),
    TV("TV"),
    MOVIE("MO"),
    OVA("OV"),
    SPECIAL("SP")
    ;

    private final String code;

    ElementSubType(String code) {
        this.code = code;
    }

    @Override
    public String serialize() {
        return this.code;
    }
}
