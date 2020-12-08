package com.openmoments.scytale.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APIKeyGeneratorTest {

    @Test
    @DisplayName("Keys differ when called subsequently")
    void keysDiffer() {
        String key = new APIKeyGenerator().buildKey();
        String key2 = new APIKeyGenerator().buildKey();
        assertNotEquals(key, key2);
    }

    @Test
    @DisplayName("Keys differ when length reduced")
    void keysDifferWhenLengthReduced() {
        String key = new APIKeyGenerator().length(10).buildKey();
        String key2 = new APIKeyGenerator().length(10).buildKey();
        assertNotEquals(key, key2);
    }
}