package ysaak.anima.exception.error;

import org.springframework.http.HttpStatus;
import ysaak.anima.exception.ErrorCode;

public enum PlaylistErrorCode implements ErrorCode {
    ELEMENT_NOT_FOUND("PLAYL-GEN-001", "Element[id=%s] not found", HttpStatus.NOT_FOUND),
    NOT_IN_PROGRESS("PLAYL-GEN-002", "Element[id=%s] not in progress for user[id=%s]", HttpStatus.BAD_REQUEST),

    ALREADY_IN_LIST("PLAYL-ADD-001", "Element already in user playlist", HttpStatus.NOT_FOUND),
    NO_PROGRESS_FOR_ELEMENT("PLAYL-PRO-001", "Element[id=%s] has no episode for progression"),
    CANNOT_CHANGE_STATUS("PLAYL-MAA-001", "Cannot change status from %s to %s")
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    PlaylistErrorCode(String code, String message) {
        this(code, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    PlaylistErrorCode(String code, String message, HttpStatus status) {
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

