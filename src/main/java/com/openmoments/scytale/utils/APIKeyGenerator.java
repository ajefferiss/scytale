package com.openmoments.scytale.utils;

import java.security.SecureRandom;

public class APIKeyGenerator {
    public static int API_KEY_LENGTH = 40;
    private static final String KEY_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_-";
    private static final SecureRandom RNG = new SecureRandom();

    private int keyLength = API_KEY_LENGTH;


    public APIKeyGenerator() {}

    public String buildKey() {
        return generateKey();
    }

    public APIKeyGenerator length(int keyLength) {
        this.keyLength = keyLength;
        return this;
    }

    private String generateKey() {
        StringBuilder stringBuilder = new StringBuilder(keyLength);
        for (int x = 0 ; x < keyLength ; x++) {
            stringBuilder.append(KEY_CHARACTERS.charAt(RNG.nextInt(KEY_CHARACTERS.length())));
        }
        return stringBuilder.toString();
    }
}
