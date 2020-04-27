package ysaak.anima.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();
    String getMessage();

    default HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    default FunctionalException functional() {
        return new FunctionalException(this);
    }

    default FunctionalException functional(Object...args) {
        return new FunctionalException(this, args);
    }

    default FunctionalException functional(Throwable cause) {
        return new FunctionalException(this, cause);
    }

    default FunctionalException functional(Throwable cause, Object...args) {
        return new FunctionalException(this, cause, args);
    }
}
