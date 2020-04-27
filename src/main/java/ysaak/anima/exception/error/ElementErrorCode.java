package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum ElementErrorCode implements ErrorCode {

    SEASON_NOT_FOUND("ELEME-SEA-001", "No season found with id %s"),


    EXTERNAL_SITE_NOT_FOUND("ELEME-REM-001", "No external site found with id %s", HttpStatus.UNAUTHORIZED),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ElementErrorCode(String code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    ElementErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}

