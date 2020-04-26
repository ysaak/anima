package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum TagErrorCode implements ErrorCode {
    NOT_FOUND("TAG-GET-001", "No tag found with id '%s'", HttpStatus.NOT_FOUND),

    VALIDATE_NAME_FORMAT("TAG-VAL-001", "Name length must be between %s and %s characters", HttpStatus.BAD_REQUEST),
    VALIDATE_DESCRIPTION_FORMAT("TAG-VAL-002", "Description length cannot more than %d characters", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    TagErrorCode(String code, String message, HttpStatus status) {
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

