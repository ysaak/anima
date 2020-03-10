package ysaak.anima.exception;

public interface ErrorCode {
    String getCode();
    String getMessage();

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
