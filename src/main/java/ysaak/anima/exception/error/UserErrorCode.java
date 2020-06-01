package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
    VALIDATE_NAME_FORMAT("USER-VAL-001", "Name length must be between %s and %s characters", HttpStatus.BAD_REQUEST),
    VALIDATE_NAME_UNIQUENESS("USER-VAL-002", "User with name '%s' already exists", HttpStatus.BAD_REQUEST),

    NOT_EXISTING_USER("USER-SAV-001", "Cannot update non existing user (id=%s)", HttpStatus.BAD_REQUEST)

    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
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

