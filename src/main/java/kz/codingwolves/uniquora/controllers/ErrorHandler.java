package kz.codingwolves.uniquora.controllers;

import kz.codingwolves.uniquora.configurations.SecurityConfigurations;
import kz.codingwolves.uniquora.enums.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String methodNotSupported(HttpServletResponse response) {
        response.setStatus(404);
        response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        return Messages.notfound.toString();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String conflict(Throwable e, HttpServletResponse response) {
        logger.info("An error occured with message {" + e.getMessage() + ", " + e.getStackTrace()[0].toString() + "}");
        if (logger.isDebugEnabled()) {
            e.printStackTrace();
        }
        response.setStatus(500);
        response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        return Messages.internalerror.toString();
    }

    @RequestMapping(value = "/error")
    public String error(HttpServletRequest request, HttpServletResponse response) {
        Integer code = (java.lang.Integer) request.getAttribute("javax.servlet.error.status_code");
        if (code != null && code == 404) {
            response.setStatus(404);
            response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            return Messages.notfound.toString();
        }
        response.setStatus(500);
        response.setHeader(SecurityConfigurations.CORS_HEADER, "*");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        return Messages.internalerror.toString();
    }
}
