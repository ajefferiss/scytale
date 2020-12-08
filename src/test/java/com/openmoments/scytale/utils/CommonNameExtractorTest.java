package com.openmoments.scytale.utils;

import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;

import static org.junit.jupiter.api.Assertions.*;

class CommonNameExtractorTest {

    @Test
    void shouldReturnIfCNOnlyField() throws InvalidNameException {
        assertEquals("DEF", new CommonNameExtractor().extract("CN=DEF"));
    }

    @Test
    void shouldIgnoreOtherItems() throws InvalidNameException {
        assertEquals("DEF", new CommonNameExtractor().extract("CN=DEF, OU=R&D, O=Company Ltd., L=London, S=London, C=GB"));
    }

    @Test
    void shouldReturnEmptyWhenNotPresent() throws InvalidNameException {
        assertEquals("", new CommonNameExtractor().extract("OU=R&D, O=Company Ltd., L=London, S=London, C=GB"));
    }

    @Test
    void shouldThrowExceptionWhenSubjectInvalid() {
        Exception invalidNameException = assertThrows(InvalidNameException.class, () -> new CommonNameExtractor().extract("CN=DEF, "));

        String expectedMessage = "Invalid name: CN=DEF,";
        String actualMessage = invalidNameException.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}