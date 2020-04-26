package ysaak.anima.exception;

public class FunctionalException extends Exception {
    private final ErrorCode error;
    private final Object[] args;

    public FunctionalException(ErrorCode error) {
        super(error.getMessage());
        this.error = error;
        this.args = new Object[0];
    }

    public FunctionalException(ErrorCode error, Object...args) {
        super(String.format(error.getMessage(), args));
        this.error = error;
        this.args = args;
    }

    public FunctionalException(ErrorCode error, Throwable cause) {
        super(error.getMessage(), cause);
        this.error = error;
        this.args = new Object[0];
    }

    public FunctionalException(ErrorCode error, Throwable cause, Object...args) {
        super(String.format(error.getMessage(), args), cause);
        this.error = error;
        this.args = args;
    }

    public ErrorCode getError() {
        return error;
    }

    public Object[] getArgs() {
        return args;
    }
}
