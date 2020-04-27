package ysaak.anima.exception.error;

import ysaak.anima.exception.ErrorCode;

public enum StorageErrorCode implements ErrorCode {
    MISSING_STORAGE_CONFIGURATION("STORA-CON-001", "No size configurer for type=%s and format=%s"),

    FILE_OUTSIDE_DIRECTORY("STORA-STO-001", "Cannot store file with relative path outside current directory  %s"),
    IMAGE_READ_ERROR("STORA-STO-002", "Error while writing image file %s"),
    EMPTY_FILE_NAME("STORA-STO-003", "Cannot store a file with an empty name"),
    IMAGE_WRITE_ERROR("STORA-STO-004", "Error while writing image %s")
    ;

    private final String code;
    private final String message;

    StorageErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
