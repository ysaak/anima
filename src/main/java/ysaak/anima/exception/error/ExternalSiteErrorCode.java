package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum ExternalSiteErrorCode implements ErrorCode {
    NOT_FOUND_BY_ID("EXTSI-GET-001", "No external site found with id '%s'", HttpStatus.NOT_FOUND),
    NOT_FOUND_BY_CODE("EXTSI-GET-002", "No external site found with code '%s'", HttpStatus.NOT_FOUND),

    VALIDATE_CODE_FORMAT("EXTSI-VAL-001", "Code length must be between %s and %s characters", HttpStatus.BAD_REQUEST),
    VALIDATE_CODE_UNIQUENESS("EXTSI-VAL-002", "Code must be unique", HttpStatus.BAD_REQUEST),
    VALIDATE_SITE_NAME_FORMAT("EXTSI-VAL-003", "Site name length must be between %d and %d characters", HttpStatus.BAD_REQUEST),
    VALIDATE_URL_TEMPLATE_FORMAT("EXTSI-VAL-004", "URL template must be a valid URL", HttpStatus.BAD_REQUEST),
    VALIDATE_URL_TEMPLATE_PLACEHOLDER("EXTSI-VAL-005", "URL template must contain the placeholder %s", HttpStatus.BAD_REQUEST),

    CANNOT_DELETE_SITE("EXTSI-DEL-001", "Cannot delete importer site", HttpStatus.UNAUTHORIZED)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ExternalSiteErrorCode(String code, String message, HttpStatus status) {
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

