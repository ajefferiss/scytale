package com.openmoments.scytale.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

public class KeyPairCreator {
    private static final int DEFAULT_KEY_LENGTH = 2048;
    private static final String DEFAULT_ALGORITHM = "RSA";

    private int keyLength = DEFAULT_KEY_LENGTH;
    private String algorithm = DEFAULT_ALGORITHM;

    public KeyPairCreator() {}

    public KeyPair buildKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        return generateKeyPair();
    }

    public KeyPairCreator length(int keyLength) {
        this.keyLength = keyLength;
        return this;
    }

    public KeyPairCreator algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    private KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, "BC");
        keyPairGenerator.initialize(keyLength, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }
}
