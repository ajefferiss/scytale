package com.openmoments.scytale.utils;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class X509CertificateGenerator {
    protected static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA256WithRSAEncryption";
    protected static final int DEFAULT_CERT_AGE_DAYS = 365;

    private String issuerDN = "";
    private String signatureAlgorithm = DEFAULT_SIGNATURE_ALGORITHM;
    private Date fromDate = new Date();
    private Date untilDate = Date.from(LocalDate.now().plus(DEFAULT_CERT_AGE_DAYS, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));

    public X509CertificateGenerator() {}

    public X509Certificate createCertificate(KeyPair pair) throws CertificateException, OperatorCreationException {
        return generateCertificate(pair);
    }

    public X509CertificateGenerator issuerDN(String issuerDN) {
        this.issuerDN = issuerDN;
        return this;
    }

    public X509CertificateGenerator signatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        return this;
    }

    public X509CertificateGenerator fromDate(Date fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public X509CertificateGenerator untilDate(Date untilDate) {
        this.untilDate = untilDate;
        return this;
    }

    private X509Certificate generateCertificate(KeyPair pair) throws OperatorCreationException, CertificateException, IllegalArgumentException {
        Security.addProvider(new BouncyCastleProvider());

        final X500Name name = new X500Name(issuerDN);
        final SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(
                pair.getPublic().getEncoded()
        );

        final X509v3CertificateBuilder certificateGenerator = new X509v3CertificateBuilder(
                name,
                new BigInteger(60, new SecureRandom()),
                fromDate,
                untilDate,
                name,
                subjectPublicKeyInfo
        );

        ContentSigner signer = new JcaContentSignerBuilder(signatureAlgorithm).setProvider("BC").build(pair.getPrivate());
        final X509CertificateHolder holder = certificateGenerator.build(signer);

        X509CertificateHolder certificateHolder = certificateGenerator.build(signer);
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certificateHolder);
    }
}
