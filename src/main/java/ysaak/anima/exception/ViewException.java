package ysaak.anima.exception;

public class ViewException extends TechnicalException {
    public ViewException(String message, String fileName, int lineNumber) {
        super(String.format("%s (%s:%d)", message, fileName, lineNumber));
    }
}
