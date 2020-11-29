package com.openmoments.scytale;

import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.AuthType;
import com.openmoments.scytale.repositories.AuthTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadAuthDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadAuthDatabase.class);

    public void initAuthenticationDatabase(AuthTypeRepository repository) {
        if (!repository.exists(AuthTypeRepository.API_KEY_MATCHER)) {
            log.info("Preloading " + repository.save(new AuthType(AuthenticationType.API_KEY)));
        }
        if (!repository.exists(AuthTypeRepository.CERT_MATCHER)) {
            log.info("Preloading " + repository.save(new AuthType(AuthenticationType.CERTIFICATE)));
        }
    }
}
