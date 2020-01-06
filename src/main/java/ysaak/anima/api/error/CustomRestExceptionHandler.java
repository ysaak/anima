package ysaak.anima.api.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ysaak.anima.exception.ArchException;

@ControllerAdvice(annotations = RestController.class)
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ArchException.class })
    public ResponseEntity<Object> handleArchException(ArchException ex, WebRequest request) {
        final ApiError apiError = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        final ApiError apiError = new ApiError("UNKOWN_ERROR", ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.I_AM_A_TEAPOT);
    }
}
