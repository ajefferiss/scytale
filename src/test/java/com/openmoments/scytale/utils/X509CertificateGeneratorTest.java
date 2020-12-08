package com.openmoments.scytale.utils;

import org.bouncycastle.operator.OperatorCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class X509CertificateGeneratorTest {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    private static final String ISSUER_DN = "CN=Test Cert";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    private KeyPair keyPair;

    @BeforeEach
    void setup() throws NoSuchProviderException, NoSuchAlgorithmException {
         keyPair = new KeyPairCreator().algorithm("RSA").buildKeyPair();
    }

    @Test
    void shouldCreateCertificateWithDN() throws CertificateException, OperatorCreationException {

        X509Certificate certificate = new X509CertificateGenerator().issuerDN(ISSUER_DN).createCertificate(keyPair);
        Date notBefore = new Date();
        Date notAfter = Date.from(LocalDate.now().plus(X509CertificateGenerator.DEFAULT_CERT_AGE_DAYS, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
        String notBeforeFormatted = simpleDateFormat.format(notBefore);
        String notAfterFormatted = simpleDateFormat.format(notAfter);
        String certNotBefore = simpleDateFormat.format(certificate.getNotBefore());
        String certNotAfter = simpleDateFormat.format(certificate.getNotAfter());

        assertEquals(ISSUER_DN, certificate.getSubjectDN().toString());
        assertEquals("sha256withrsa", certificate.getSigAlgName().toLowerCase());
        assertEquals(certNotBefore, notBeforeFormatted);
        assertEquals(certNotAfter, notAfterFormatted);
    }

    @Test
    void shouldAllowNotBeforeDateInPast() throws CertificateException, OperatorCreationException {
        Date notBefore = Date.from(LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
        X509Certificate certificate = new X509CertificateGenerator()
                .fromDate(notBefore)
                .issuerDN(ISSUER_DN)
                .createCertificate(keyPair);
        String notBeforeFormatted = simpleDateFormat.format(notBefore);
        String certNotBeforeFormatted = simpleDateFormat.format(certificate.getNotBefore());

        assertEquals(notBeforeFormatted, certNotBeforeFormatted);
    }

    @Test
    void shouldAllowNotBeforeInFuture() throws CertificateException, OperatorCreationException {
        Date notBefore = Date.from(LocalDate.now().plus(1, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
        X509Certificate certificate = new X509CertificateGenerator()
                .fromDate(notBefore)
                .issuerDN(ISSUER_DN)
                .createCertificate(keyPair);
        String notBeforeFormatted = simpleDateFormat.format(notBefore);
        String certNotBeforeFormatted = simpleDateFormat.format(certificate.getNotBefore());

        assertEquals(notBeforeFormatted, certNotBeforeFormatted);
    }

    @Test
    void shouldAllowNotAfterDateInPast() throws CertificateException, OperatorCreationException {
        Date notAfter = Date.from(LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
        X509Certificate certificate = new X509CertificateGenerator()
                .untilDate(notAfter)
                .issuerDN(ISSUER_DN)
                .createCertificate(keyPair);
        String notAfterFormatted = simpleDateFormat.format(notAfter);
        String certNotAfterFormatted = simpleDateFormat.format(certificate.getNotAfter());

        assertEquals(notAfterFormatted, certNotAfterFormatted);
    }

    @Test
    void shouldAllowSignatureAlgorithms() throws CertificateException, OperatorCreationException {
        X509Certificate certificate = new X509CertificateGenerator()
                .signatureAlgorithm("SHA512WithRSAEncryption")
                .issuerDN(ISSUER_DN)
                .createCertificate(keyPair);

        assertEquals("sha512withrsa", certificate.getSigAlgName().toLowerCase());
    }

    @Test
    void shouldThrowExceptionWhenSubjectMissing() {
        Exception illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new X509CertificateGenerator()
                .signatureAlgorithm("SHA512WithRSAEncryption")
                .issuerDN("")
                .createCertificate(keyPair));

        assertTrue(illegalArgumentException.getMessage().contains("badly formatted"));
    }

    @Test
    void shouldThrowExceptionWhenSubjectInvalid() {
        Exception illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new X509CertificateGenerator()
                .signatureAlgorithm("SHA512WithRSAEncryption")
                .issuerDN("CN=Test Cert OU=R&D,")
                .createCertificate(keyPair));

        assertTrue(illegalArgumentException.getMessage().contains("badly formatted"));
    }
}