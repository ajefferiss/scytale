package com.openmoments.scytale.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorTest {

    @Test
    @DisplayName("Keys differ when called subsequently")
    public void keysDiffer() {
        String key = new KeyGenerator().buildKey();
        String key2 = new KeyGenerator().buildKey();
        assertNotEquals(key, key2);
    }

    @Test
    @DisplayName("Keys differ when length reduced")
    public void keysDifferWhenLengthReduced() {
        String key = new KeyGenerator().length(10).buildKey();
        String key2 = new KeyGenerator().length(10).buildKey();
        assertNotEquals(key, key2);
    }
}