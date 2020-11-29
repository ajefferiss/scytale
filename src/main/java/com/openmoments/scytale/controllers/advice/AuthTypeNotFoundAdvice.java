package com.openmoments.scytale.controllers.advice;

import com.openmoments.scytale.exceptions.AuthTypeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthTypeNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(AuthTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String authTypeNotFoundHandler(AuthTypeNotFoundException ex) {
        return ex.getMessage();
    }
}
