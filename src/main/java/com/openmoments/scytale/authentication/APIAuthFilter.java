package com.openmoments.scytale.authentication;

import com.openmoments.scytale.utils.CommonNameExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.naming.InvalidNameException;
import javax.servlet.http.HttpServletRequest;
import java.security.cert.X509Certificate;

public class APIAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final String headerName;
    private static final Logger LOG = LoggerFactory.getLogger(APIAuthFilter.class);

    public APIAuthFilter(final String headerName) {
        this.headerName = headerName;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String headerValue = request.getHeader(headerName);
        return (headerValue != null) ? headerValue : "";
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            try {
                return new CommonNameExtractor().extract(certificates[0].getSubjectDN().toString());
            } catch (InvalidNameException invalidNameException) {
                LOG.error("Failed to extract common name from certificate subject: {}", certificates[0].getSubjectDN());
            }
        }

        return "";
    }
}
