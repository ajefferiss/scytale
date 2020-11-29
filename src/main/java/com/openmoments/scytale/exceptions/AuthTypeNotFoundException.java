package com.openmoments.scytale.exceptions;

public class AuthTypeNotFoundException extends RuntimeException {
    public AuthTypeNotFoundException(Long id) {
        super("Could not find auth type " + id);
    }
}
