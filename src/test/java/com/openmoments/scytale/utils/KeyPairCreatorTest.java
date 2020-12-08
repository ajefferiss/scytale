package com.openmoments.scytale.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;

import static org.junit.jupiter.api.Assertions.*;

class KeyPairCreatorTest {

    @Test
    void x509PairShouldUseRSAByDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = new KeyPairCreator().buildKeyPair();
        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
        assertEquals("X.509", keyPair.getPublic().getFormat());
    }

    @Test
    void x509PairShouldGenerateDSA() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = new KeyPairCreator().algorithm("DSA").buildKeyPair();
        assertEquals("DSA", keyPair.getPublic().getAlgorithm());
        assertEquals("X.509", keyPair.getPublic().getFormat());
    }

    @Test
    void pairUse2048KeyLengthByDefault() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = new KeyPairCreator().buildKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
        assertEquals(2048, privateKey.getModulus().bitLength());
    }

    @Test
    void pairUsesSpecifiedKeyLength() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = new KeyPairCreator().length(1024).buildKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

        assertEquals("RSA", keyPair.getPrivate().getAlgorithm());
        assertEquals(1024, privateKey.getModulus().bitLength());
    }
}