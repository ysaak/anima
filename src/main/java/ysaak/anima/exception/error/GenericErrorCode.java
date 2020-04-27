package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum GenericErrorCode implements ErrorCode {
    NOT_FOUND("APPLI-GEN-001", "No data found for id %s", HttpStatus.NOT_FOUND),
    OPERATION_NOT_ALLOWED("APPLI-GEN-002", "Operation not allowed", HttpStatus.UNAUTHORIZED),

    NULL_VALUE("APPLI-VAL-001", "%s is null"),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    GenericErrorCode(String code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    GenericErrorCode(String code, String message, HttpStatus status) {
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

