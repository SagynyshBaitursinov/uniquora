package kz.codingwolves.controllers;

import kz.codingwolves.enums.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sagynysh on 12/20/16.
 */
@ControllerAdvice
@RestController
public class ErrorHandler implements ErrorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String conflict(Throwable e) {
        logger.info("An error occured with message {" + e.getMessage() + ", " + e.getStackTrace()[0].toString() + "}");
        return Messages.internalerror.toString();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public String exception(AccessDeniedException e) {
        return Messages.forbidden.toString();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public String error() {
        return Messages.internalerror.toString();
    }
}
