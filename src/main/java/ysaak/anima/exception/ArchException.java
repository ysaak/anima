package ysaak.anima.exception;

public class ArchException extends Exception {
    private final String code;

    public ArchException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public ArchException(final String code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
