package com.openmoments.scytale.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openmoments.scytale.authentication.AuthenticatedClient;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.entities.Keystore;
import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import com.openmoments.scytale.repositories.KeystoreRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@RestController
@RequestMapping("/api/v1/keystores")
public class KeystoreController {

    private final KeystoreRepository keystoreRepository;
    private final AuthenticatedClient authenticatedClient;
    private final KeystoreModelAssembler assembler;
    private static final String KEYSTORE_FILTER = "keystoreFilter";
    private static final SimpleBeanPropertyFilter CLIENT_FILTER = SimpleBeanPropertyFilter.serializeAllExcept("client");

    protected static final FilterProvider CLIENT_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(KEYSTORE_FILTER, CLIENT_FILTER);

    public KeystoreController(KeystoreRepository keystoreRepository, KeystoreModelAssembler assembler, AuthenticatedClient authenticatedClient) {
        this.keystoreRepository = keystoreRepository;
        this.assembler = assembler;
        this.authenticatedClient = authenticatedClient;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue one(@PathVariable Long id) {
        Optional<Keystore> foundKeystore = findKeystoreById(id);

        EntityModel<Keystore> resource = assembler.toModel(foundKeystore.get());
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue all() {
        Client client = authenticatedClient.getClient();
        Keystore keystore = new Keystore();
        keystore.setClient(client);

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("client", exact());

        List<EntityModel<Keystore>> keystores = keystoreRepository.findAll(Example.of(keystore, matcher))
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        MappingJacksonValue mapping = new MappingJacksonValue(keystores);
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue newKeystore(@RequestBody Keystore newKeystore) {
        Client client = authenticatedClient.getClient();
        newKeystore.setClient(client);
        EntityModel<Keystore> resource = assembler.toModel(keystoreRepository.save(newKeystore));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("isAuthenticated()")
    MappingJacksonValue replaceKeystore(@PathVariable Long id, @RequestBody Keystore newKeystore) {
        clientCanAccessKeystore(id);

        Keystore replacedKeystore = keystoreRepository.findById(id)
                .map(keystore -> {
                    keystore.setName(newKeystore.getName());
                    return keystoreRepository.save(keystore);
                })
                .orElseGet(() -> {
                    newKeystore.setId(id);
                    return keystoreRepository.save(newKeystore);
                });

        MappingJacksonValue mapping = new MappingJacksonValue(assembler.toModel(replacedKeystore));
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    void deleteKeystore(@PathVariable Long id) {
        clientCanAccessKeystore(id);
        keystoreRepository.deleteById(id);
    }

    private Optional<Keystore> findKeystoreById(Long id) {
        Keystore keystore = new Keystore();
        keystore.setId(id);
        keystore.setClient(authenticatedClient.getClient());

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("id", exact());

        Optional<Keystore> foundKeystore = keystoreRepository.findOne(Example.of(keystore, matcher));
        if (foundKeystore.isEmpty()) {
            throw new KeystoreNotFoundException(id);
        }

        return foundKeystore;
    }

    private void clientCanAccessKeystore(Long id) {
        Client client = authenticatedClient.getClient();
        Optional<Keystore> keystore = keystoreRepository.findById(id);

        if (keystore.isEmpty() || keystore.get().getClient().getId().equals(client.getId())) {
            return;
        }

        throw new BadCredentialsException("Authentication was not found or not the expected value");
    }
}
