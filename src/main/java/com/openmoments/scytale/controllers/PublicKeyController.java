package com.openmoments.scytale.controllers;

import com.openmoments.scytale.authentication.AuthenticatedClient;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.entities.Keystore;
import com.openmoments.scytale.entities.PublicKey;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import com.openmoments.scytale.exceptions.PublicKeyNotFoundException;
import com.openmoments.scytale.repositories.KeystoreRepository;
import com.openmoments.scytale.repositories.PublicKeyRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@RestController
public class PublicKeyController {

    private final KeystoreRepository keystoreRepository;
    private final PublicKeyRepository publicKeyRepository;
    private final PublicKeyModelAssembler assembler;
    private final AuthenticatedClient authenticatedClient;

    public PublicKeyController(PublicKeyRepository publicKeyRepository, KeystoreRepository keystoreRepository, PublicKeyModelAssembler assembler, AuthenticatedClient authenticatedClient) {
        this.publicKeyRepository = publicKeyRepository;
        this.keystoreRepository = keystoreRepository;
        this.assembler = assembler;
        this.authenticatedClient = authenticatedClient;
    }

    @GetMapping("/api/v1/keystores/{keystoreId}/keys/{id}")
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue one(@PathVariable Long keystoreId, @PathVariable Long id) {
        Optional<PublicKey> foundPublicKey = getPublicKeyIfExists(keystoreId, id);

        MappingJacksonValue mapping = new MappingJacksonValue(assembler.toModel(foundPublicKey.get()));
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @GetMapping("/api/v1/keystores/{keystoreId}/keys")
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue all(@PathVariable Long keystoreId) {
        Keystore keystore = getKeystoreIfExists(keystoreId);
        PublicKey publicKey = new PublicKey();
        publicKey.setKeystore(keystore);

        ExampleMatcher keysMatcher = ExampleMatcher.matching().withMatcher("keystore", exact());
        List<EntityModel<PublicKey>> publicKeys = publicKeyRepository.findAll(Example.of(publicKey, keysMatcher))
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        MappingJacksonValue mapping = new MappingJacksonValue(publicKeys);
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @PostMapping("/api/v1/keystores/{keystoreId}/keys")
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue newPublicKey(@PathVariable Long keystoreId, @RequestBody PublicKey newPublicKey) {
        Keystore keystore = getKeystoreIfExists(keystoreId);
        newPublicKey.setKeystore(keystore);

        EntityModel<PublicKey> resource = assembler.toModel(publicKeyRepository.save(newPublicKey));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @PutMapping("/api/v1/keystores/{keystoreId}/keys/{id}")
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue replacePublicKey(@PathVariable Long keystoreId, @PathVariable Long id, @RequestBody PublicKey newPublicKey) {
        getKeystoreIfExists(keystoreId);

        PublicKey replacedPublicKey = publicKeyRepository.findById(id)
                .map(publicKey -> {
                    publicKey.setPublicKey(newPublicKey.getPublicKey());
                    return publicKeyRepository.save(publicKey);
                })
                .orElseGet(() -> {
                    newPublicKey.setId(id);
                    return publicKeyRepository.save(newPublicKey);
                });

        MappingJacksonValue mapping = new MappingJacksonValue(assembler.toModel(replacedPublicKey));
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @DeleteMapping("/api/v1/keystores/{keystoreId}/keys/{id}")
    @PreAuthorize("isAuthenticated()")
    void deletePublicKey(@PathVariable Long keystoreId, @PathVariable Long id) {
        getPublicKeyIfExists(keystoreId, id);
        publicKeyRepository.deleteById(id);
    }

    Optional<PublicKey> getPublicKeyIfExists(Long keystoreId, Long id) {
        Keystore keystore = getKeystoreIfExists(keystoreId);

        PublicKey publicKey = new PublicKey();
        publicKey.setId(id);
        publicKey.setKeystore(keystore);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("keystore", exact())
                .withMatcher("id", exact());

        Optional<PublicKey> foundPublicKey = publicKeyRepository.findOne(Example.of(publicKey, matcher));
        if (foundPublicKey.isEmpty()) {
            throw new PublicKeyNotFoundException(id);
        }

        return foundPublicKey;
    }

    Keystore getKeystoreIfExists(Long keystoreId) {
        Client client = authenticatedClient.getClient();

        Keystore keystore = keystoreRepository.findById(keystoreId).orElseThrow(() -> new KeystoreNotFoundException(keystoreId));
        if (!keystore.getClient().getId().equals(client.getId())) {
            throw new ClientNotFoundException(client.getId());
        }

        return keystore;
    }
}
