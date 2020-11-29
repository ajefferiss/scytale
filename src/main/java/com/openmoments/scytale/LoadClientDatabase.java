package com.openmoments.scytale;

import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.AuthType;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.repositories.AuthTypeRepository;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.utils.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LoadClientDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadClientDatabase.class);

    public void initClientDatabase(ClientRepository repository, AuthTypeRepository authTypeRepository) {

        Optional<AuthType> authTypeOptional = authTypeRepository.findOne(AuthTypeRepository.API_KEY_MATCHER);
        if (authTypeOptional.isEmpty()) {
            throw new IllegalArgumentException("Unknown authentication type " + AuthenticationType.API_KEY);
        }
        AuthType keyAuthType = authTypeOptional.get();

        if (!repository.existsByName("Test Key")) {
            log.info("Preloading " +
                    repository.save(new Client("Test Key", keyAuthType, new KeyGenerator().buildKey()))
            );
        }
    }
}
