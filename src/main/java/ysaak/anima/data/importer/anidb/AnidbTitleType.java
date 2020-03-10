package ysaak.anima.data.importer.anidb;

import ysaak.anima.dao.converter.ISerializableEnum;

import java.util.stream.Stream;

public enum AnidbTitleType implements ISerializableEnum {
    PRIMARY_TITLE("1"),
    SYNONYM("2"),
    SHORT_TITLE("3"),
    OFFICIAL_TITLE("4"),
    OTHERS("-")
    ;

    private final String code;

    AnidbTitleType(String code) {
        this.code = code;
    }

    @Override
    public String serialize() {
        return code;
    }

    public static AnidbTitleType getFromCode(String code) {
        return Stream.of(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(OTHERS);
    }
}
