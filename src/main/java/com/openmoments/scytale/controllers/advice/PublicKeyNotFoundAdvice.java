package com.openmoments.scytale.controllers.advice;

import com.openmoments.scytale.exceptions.PublicKeyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PublicKeyNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(PublicKeyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String publicKeyNotFoundAdvice(PublicKeyNotFoundException ex) {
        return ex.getMessage();
    }
}
