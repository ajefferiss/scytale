package com.openmoments.scytale.controllers;

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

    public PublicKeyController(PublicKeyRepository publicKeyRepository, KeystoreRepository keystoreRepository, PublicKeyModelAssembler assembler) {
        this.publicKeyRepository = publicKeyRepository;
        this.keystoreRepository = keystoreRepository;
        this.assembler = assembler;
    }

    @GetMapping("/clients/{clientId}/keystores/{keystoreId}/keys/{id}")
    MappingJacksonValue one(@PathVariable Long clientId, @PathVariable Long keystoreId, @PathVariable Long id) {
        // TODO: Protect with client API key

        Keystore keystore = keystoreRepository.findById(keystoreId).orElseThrow(() -> new KeystoreNotFoundException(keystoreId));
        if (!keystore.getClient().getId().equals(clientId)) {
            throw new ClientNotFoundException(clientId);
        }

        PublicKey publicKey = new PublicKey();
        publicKey.setKeystore(keystore);
        publicKey.setId(id);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("keystore", exact())
                .withMatcher("id", exact());

        Optional<PublicKey> foundPublicKey = publicKeyRepository.findOne(Example.of(publicKey, matcher));
        if (foundPublicKey.isEmpty()) {
            throw new PublicKeyNotFoundException(id);
        }

        MappingJacksonValue mapping = new MappingJacksonValue(assembler.toModel(foundPublicKey.get()));
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @GetMapping("/clients/{clientId}/keystores/{keystoreId}/keys")
    MappingJacksonValue all(@PathVariable Long clientId, @PathVariable Long keystoreId) {
        Keystore keystore = keystoreRepository.findById(keystoreId).orElseThrow(() -> new KeystoreNotFoundException(keystoreId));
        if (!keystore.getClient().getId().equals(clientId)) {
            throw new ClientNotFoundException(clientId);
        }

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

    @PostMapping("/clients/{clientId}/keystores/{keystoreId}/keys")
    MappingJacksonValue newPublicKey(@PathVariable Long clientId, @PathVariable Long keystoreId, @RequestBody PublicKey newPublicKey) {
        // TODO: Protect by client API key
        EntityModel<PublicKey> resource = assembler.toModel(publicKeyRepository.save(newPublicKey));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(KeystoreController.CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @PutMapping("/clients/{clientId}/keystores/{keystoreId}/keys/{id}")
    MappingJacksonValue replacePublicKey(@RequestBody PublicKey newPublicKey, @PathVariable Long clientId, @PathVariable Long keystoreId, @PathVariable Long id) {
        // TODO: Protect by client API
        one(clientId, keystoreId, id);

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

    @DeleteMapping("/clients/{clientId}/keystores/{keystoreId}/keys/{id}")
    void deleteKeystore(@PathVariable Long clientId, @PathVariable Long keystoreId, @PathVariable Long id) {
        // TODO: Protect by client API key
        one(clientId, keystoreId, id);
        publicKeyRepository.deleteById(id);
    }
}
