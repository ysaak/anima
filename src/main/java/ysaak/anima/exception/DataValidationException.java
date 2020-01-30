package ysaak.anima.exception;

import java.util.Collections;
import java.util.List;

public class DataValidationException extends Exception {
    private final List<String> messageList;

    public DataValidationException(String message) {
        super(message);
        messageList = Collections.singletonList(message);
    }

    public DataValidationException(List<String> messageList) {
        super("Error while validating object");
        this.messageList = messageList;
    }

    public List<String> getMessageList() {
        return messageList;
    }
}
