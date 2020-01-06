package ysaak.anima.api.dto;

import java.util.StringJoiner;

public class AnidbImportParamDto {
    public final String id;

    public AnidbImportParamDto() {
        id = null;
    }

    public AnidbImportParamDto(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AnidbImportParamDto.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .toString();
    }
}
