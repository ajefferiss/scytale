package com.openmoments.scytale.controllers.advice;

import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class KeystoreNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(KeystoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String keystoreNotFoundHandler(KeystoreNotFoundException ex) {
        return ex.getMessage();
    }
}
