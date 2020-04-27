package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum CollectionErrorCode implements ErrorCode {
    VALIDATE_NAME_FORMAT("COLLE-VAL-001", "Name length must be between %s and %s characters", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    CollectionErrorCode(String code, String message, HttpStatus status) {
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

