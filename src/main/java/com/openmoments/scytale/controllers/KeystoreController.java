package com.openmoments.scytale.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.entities.Keystore;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.repositories.KeystoreRepository;
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
public class KeystoreController {

    private final KeystoreRepository keystoreRepository;
    private final ClientRepository clientRepository;
    private final KeystoreModelAssembler assembler;
    private static final String KEYSTORE_FILTER = "keystoreFilter";
    private static final SimpleBeanPropertyFilter CLIENT_FILTER = SimpleBeanPropertyFilter.serializeAllExcept("client");

    protected static final FilterProvider CLIENT_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(KEYSTORE_FILTER, CLIENT_FILTER);

    public KeystoreController(KeystoreRepository keystoreRepository, ClientRepository clientRepository, KeystoreModelAssembler assembler) {
        this.keystoreRepository = keystoreRepository;
        this.clientRepository = clientRepository;
        this.assembler = assembler;
    }

    @GetMapping("/clients/{clientId}/keystores/{id}")
    MappingJacksonValue one(@PathVariable Long clientId, @PathVariable Long id) {
        // TODO: Protect with client API key
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));

        Keystore keystore = new Keystore();
        keystore.setId(id);
        keystore.setClient(client);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("client", exact())
                .withMatcher("id", exact());

        Optional<Keystore> foundKeystore = keystoreRepository.findOne(Example.of(keystore, matcher));
        if (foundKeystore.isEmpty()) {
            throw new KeystoreNotFoundException(id);
        }

        EntityModel<Keystore> resource = assembler.toModel(foundKeystore.get());
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @GetMapping("/clients/{clientId}/keystores")
    MappingJacksonValue all(@PathVariable Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId));
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

    @PostMapping("/clients/{clientId}/keystores")
    MappingJacksonValue newKeystore(@PathVariable Long clientId, @RequestBody Keystore newKeystore) {
        // TODO: Protect by client API key

        EntityModel<Keystore> resource = assembler.toModel(keystoreRepository.save(newKeystore));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(CLIENT_FILTER_PROVIDER);

        return mapping;
    }

    @PutMapping("/clients/{clientId}/keystores/{id}")
    MappingJacksonValue replaceKeystore(@RequestBody Keystore newKeystore, @PathVariable Long clientId, @PathVariable Long id) {
        // TODO: Protect by client API
        one(clientId, id);

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

    @DeleteMapping("/clients/{clientId}/keystores/{id}")
    void deleteKeystore(@PathVariable Long clientId, @PathVariable Long id) {
        // TODO: Protect by client API key
        one(clientId, id);
        keystoreRepository.deleteById(id);
    }
}
