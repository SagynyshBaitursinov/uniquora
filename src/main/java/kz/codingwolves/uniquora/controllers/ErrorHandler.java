package kz.codingwolves.uniquora.controllers;

import kz.codingwolves.uniquora.enums.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sagynysh on 12/20/16.
 */
@ControllerAdvice
@RestController
public class ErrorHandler implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public void methodNotSupported(HttpServletResponse response) throws IOException {
        response.setStatus(404);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(Message.notfound.toString());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public void missingParameter(HttpServletResponse response) throws IOException {
        response.setStatus(400);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(Message.fill.toString());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public void conflict(Throwable e, HttpServletResponse response) throws IOException {
        logger.info("An error occured with message {" + e.getMessage() + ", " + e.getStackTrace()[0].toString() + "}");
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
        }
        response.setStatus(500);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(Message.internalerror.toString());
    }

    @RequestMapping(value = "/error")
    public void error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer code = (java.lang.Integer) request.getAttribute("javax.servlet.error.status_code");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        if (code != null && code == 404) {
            response.setStatus(404);
            response.getWriter().write(Message.notfound.toString());
        } else {
            response.setStatus(500);
            response.getWriter().write(Message.internalerror.toString());
        }
    }
}
