package ysaak.anima.exception;

import ysaak.anima.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataValidationException extends Exception {
    private final Map<String, String> errorMap;

    public DataValidationException(final String key, final String message) {
        super("Error while validating field " + key + " > " + message);
        errorMap = Collections.singletonMap(key, message);
    }

    public DataValidationException(Map<String, String> errorMap) {
        super("Error while validating object ");
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public List<String> getMessageList() {
        return CollectionUtils.toList(errorMap.values());
    }
}
