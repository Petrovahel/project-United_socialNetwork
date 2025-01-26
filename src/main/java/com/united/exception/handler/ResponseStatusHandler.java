package com.united.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ResponseStatusHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ModelAndView testErrorHandler2(HttpServletRequest req, Exception e) {
        return new ModelAndView("profile");
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView handleNotFoundException(Authentication authentication, NotFoundException e){
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.addObject("nickname", authentication.getName());
        return modelAndView;
    }
}



