package com.sfan.hydro.support;

import com.sfan.hydro.attach.HydroNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class HydroErrorHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String ERROR_PAGE = "error/info";

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public String processException(Model model, RuntimeException exception){
        logger.error("Exception", exception);
        model.addAttribute("exceptionType", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ERROR_PAGE;
    }

    @ExceptionHandler({HydroNotFoundException.class, NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.OK)
    public String processException(Model model, Exception exception){
        logger.error("Not found", exception);
        model.addAttribute("exceptionType", HttpStatus.NOT_FOUND.value());
        return ERROR_PAGE;
    }
}
