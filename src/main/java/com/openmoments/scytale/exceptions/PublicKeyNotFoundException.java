package com.openmoments.scytale.exceptions;

public class PublicKeyNotFoundException extends RuntimeException{
    public PublicKeyNotFoundException(Long id) {
        super("Could not find public key " + id);
    }
}
