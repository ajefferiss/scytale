package com.openmoments.scytale.authentication;


import com.openmoments.scytale.utils.CommonNameExtractor;
import com.openmoments.scytale.utils.KeyPairCreator;
import com.openmoments.scytale.utils.X509CertificateGenerator;
import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletRequest;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class APIAuthFilterTest {

    public static final String API_KEY = "API_KEY";
    private APIAuthFilter authFilter;
    private final HttpServletRequest testRequest = mock(HttpServletRequest.class);

    @BeforeEach
    public void setup() {
        authFilter = new APIAuthFilter(API_KEY);
    }

    @Test
    void shouldReturnAPIWhenPresent() {
        when(testRequest.getHeader(API_KEY)).thenReturn("ABC");
        assertEquals("ABC", authFilter.getPreAuthenticatedPrincipal(testRequest));
    }

    @Test
    void shouldReturnNullWhenHeaderMissing() {
        when(testRequest.getHeader(API_KEY)).thenReturn(null);
        assertEquals("", authFilter.getPreAuthenticatedPrincipal(testRequest));
    }

    @Test
    void shouldReturnEmptyStringWhenNotEmpty() {
        when(testRequest.getHeader(API_KEY)).thenReturn("");
        assertEquals("", authFilter.getPreAuthenticatedPrincipal(testRequest));
    }

    @Test
    void shouldReturnNullWhenNoCertificates() {
        when(testRequest.getAttribute("javax.servlet.request.X509Certificate"))
                .thenReturn(new X509Certificate[]{});
        assertEquals("", authFilter.getPreAuthenticatedCredentials(testRequest));
    }

    @Test
    void shouldReturnCertificate() throws NoSuchProviderException, NoSuchAlgorithmException, CertificateException, OperatorCreationException {
        KeyPair keyPair = new KeyPairCreator().algorithm("RSA").buildKeyPair();
        X509Certificate certificate = new X509CertificateGenerator()
                .signatureAlgorithm("SHA512WithRSAEncryption")
                .issuerDN("CN=Test Cert")
                .createCertificate(keyPair);

        when(testRequest.getAttribute("javax.servlet.request.X509Certificate"))
                .thenReturn(new X509Certificate[]{certificate});

        assertEquals("Test Cert", authFilter.getPreAuthenticatedCredentials(testRequest));
    }
}