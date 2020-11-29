package com.openmoments.scytale.exceptions;

public class KeystoreNotFoundException extends RuntimeException{
    public KeystoreNotFoundException(Long id) {
        super("Could not find keystore " + id);
    }
}
