package com.openmoments.scytale.authentication;

import com.openmoments.scytale.entities.AuthType;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.utils.X509CertificateGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class APIAuthManagerTest {

    private Authentication mockAuthenication;
    private ClientRepository mockClientRepository;
    private Client mockAPIClient;
    private Client mockAPIClient2;
    private Client mockAPICert;

    @BeforeEach
    void setup() {
        mockAuthenication = mock(Authentication.class);
        mockClientRepository = mock(ClientRepository.class);

        mockAPIClient = mock(Client.class);
        mockAPIClient2 = mock(Client.class);
        mockAPICert = mock(Client.class);

        when(mockClientRepository.findAll()).thenReturn(List.of(mockAPIClient, mockAPIClient2, mockAPICert));
    }

    @Test
    void shouldAuthenticateWithAPIKey() {
        when(mockAPIClient.getApiKey()).thenReturn("An API Key is Quite Long");
        when(mockAPIClient2.getApiKey()).thenReturn("API_KEYS_ARE_LONG");
        when(mockAPIClient2.getId()).thenReturn(1L);
        when(mockAPICert.getApiKey()).thenReturn("");

        when(mockAuthenication.getPrincipal()).thenReturn("API_KEYS_ARE_LONG");
        when(mockAuthenication.getCredentials()).thenReturn("");

        Authentication authentication = new APIAuthManager(mockClientRepository).authenticate(mockAuthenication);

        assertEquals(1L, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void shouldAuthenticateWithAPICertificate() {
        when(mockAPIClient.getApiKey()).thenReturn("An API Key is Quite Long");
        when(mockAPIClient2.getApiKey()).thenReturn("API_KEYS_ARE_LONG");
        when(mockAPICert.getApiKey()).thenReturn("");
        when(mockAPICert.getId()).thenReturn(1L);
        when(mockAPICert.getName()).thenReturn("Certificate Client");

        when(mockAuthenication.getPrincipal()).thenReturn("");
        when(mockAuthenication.getCredentials()).thenReturn("Certificate Client");

        Authentication authentication = new APIAuthManager(mockClientRepository).authenticate(mockAuthenication);

        assertEquals(1L, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void shouldThrowWhenUnableToAuthenticate() {
        when(mockAPIClient.getApiKey()).thenReturn("An API Key is Quite Long");
        when(mockAPIClient2.getApiKey()).thenReturn("API_KEYS_ARE_LONG");
        when(mockAPICert.getApiKey()).thenReturn("");

        when(mockAuthenication.getPrincipal()).thenReturn("Something Unknown");
        when(mockAuthenication.getCredentials()).thenReturn("");

        Exception badCredentialsException = assertThrows(BadCredentialsException.class, () -> new APIAuthManager(mockClientRepository).authenticate(mockAuthenication));
        assertTrue(badCredentialsException.getMessage().contains("Authentication was not found or not the expected value"));
    }

}