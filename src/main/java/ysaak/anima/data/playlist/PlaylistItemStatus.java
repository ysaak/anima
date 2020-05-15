package ysaak.anima.data.playlist;

import ysaak.anima.dao.converter.ISerializableEnum;

public enum PlaylistItemStatus implements ISerializableEnum {
    PLAN_TO_WATCH("P"),
    STARTED("S"),
    FINISHED("F"),
    DROPPED("D")
    ;

    private final String code;

    PlaylistItemStatus(String code) {
        this.code = code;
    }

    @Override
    public String serialize() {
        return code;
    }
}
