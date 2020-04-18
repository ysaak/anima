package ysaak.anima.view.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final ErrorAttributes errorAttributes;

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, HttpServletResponse response) {
        final int statusCode = response.getStatus();

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("statusCode", statusCode);

        if(statusCode == HttpStatus.NOT_FOUND.value()) {
            modelAndView.setViewName("errors/error-404");
        }
        //else if(response.getStatus() == HttpStatus.FORBIDDEN.value()) {
        //    modelAndView.setViewName("error-403");
        //}
        else {
            WebRequest req = new ServletWebRequest(request);
            Throwable exception = errorAttributes.getError(req);
            String errorClass = null;
            String errorMessage = null;
            if (exception != null) {
                if (exception.getCause() != null) {
                    exception = exception.getCause();
                }
                errorClass = exception.getClass().getSimpleName();
                errorMessage = exception.getMessage();
            }

            modelAndView.setViewName("errors/error");
            modelAndView.addObject("errorClass", errorClass);
            modelAndView.addObject("errorMessage", errorMessage);
        }

        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
