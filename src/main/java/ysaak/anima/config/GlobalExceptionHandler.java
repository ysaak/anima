package ysaak.anima.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ysaak.anima.exception.FunctionalException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FunctionalException.class)
    public void springHandleNotFound(FunctionalException exception, HttpServletResponse response) throws IOException {
        response.sendError(exception.getError().getStatus().value());
    }
}
