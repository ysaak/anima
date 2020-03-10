package ysaak.anima.exception;

public class FunctionalException extends Exception {
    private final ErrorCode error;

    public FunctionalException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
    }

    public FunctionalException(ErrorCode error, Object...args) {
        super(String.format(error.getMessage(), args));
        this.error = error;
    }

    public FunctionalException(ErrorCode error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
    }

    public FunctionalException(ErrorCode error, Throwable cause, Object...args) {
        super(String.format(error.getMessage(), args), cause);
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }
}
