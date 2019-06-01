package com.sfan.hydro.controller.admin;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HydroErrorController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(HydroErrorController.class);

    private final String ERROR_PAGE = "error/info";

    @RequestMapping("/error")
    public String errorPage(Model model, HttpServletRequest request, HttpServletResponse response){
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        model.addAttribute("exceptionType", HttpStatus.valueOf(statusCode));
        return getErrorPath();
    }

    @Override
    public String getErrorPath() {
        return ERROR_PAGE;
    }
}
