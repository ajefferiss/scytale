package com.openmoments.scytale.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.utils.KeyGenerator;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientModelAssembler assembler;

    private static final String CLIENT_FILTER = "clientFilter";
    private static final SimpleBeanPropertyFilter AUTH_FILTER = SimpleBeanPropertyFilter.serializeAllExcept("authType", "apiKey");
    private static final SimpleBeanPropertyFilter NO_FILTER = SimpleBeanPropertyFilter.serializeAll();
    private static final FilterProvider AUTH_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(CLIENT_FILTER, AUTH_FILTER);
    private static final FilterProvider NO_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(CLIENT_FILTER, NO_FILTER);

    ClientController(ClientRepository clientRepository, ClientModelAssembler assembler) {
        this.clientRepository = clientRepository;
        this.assembler = assembler;
    }

    @GetMapping("/clients/{id}")
    MappingJacksonValue one(@PathVariable Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        EntityModel<Client> resource = assembler.toModel(client);
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(AUTH_FILTER_PROVIDER);

        return mapping;
    }

    @GetMapping("/clients")
    MappingJacksonValue all() {
        List<EntityModel<Client>> clients = clientRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        MappingJacksonValue mapping = new MappingJacksonValue(clients);
        mapping.setFilters(AUTH_FILTER_PROVIDER);

        return mapping;
    }

    @PostMapping("/clients")
    MappingJacksonValue newClient(@RequestBody Client newClient) {
        // TODO: Protect by master API
        if (newClient.getAuthType().getAuthType().equals(AuthenticationType.API_KEY)) {
            newClient.setApiKey(new KeyGenerator().buildKey());
        }

        EntityModel<Client> resource = assembler.toModel(clientRepository.save(newClient));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(NO_FILTER_PROVIDER);

        return mapping;
    }

    @PutMapping("/clients/{id}")
    MappingJacksonValue replaceClient(@RequestBody Client newClient, @PathVariable Long id) {
        // TODO: Protect by master API or client API
        Client replacedClient = clientRepository.findById(id)
                .map(client -> {
                    client.setName(newClient.getName());
                    client.setAuthType(newClient.getAuthType());
                    client.setApiKey(newClient.getApiKey());
                    return clientRepository.save(client);
                })
                .orElseGet(() -> {
                    newClient.setId(id);
                    return clientRepository.save(newClient);
                });

        EntityModel<Client> resource = assembler.toModel(clientRepository.save(newClient));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(NO_FILTER_PROVIDER);

        return mapping;
    }

    @DeleteMapping("/clients/{id}")
    void deleteClient(@PathVariable Long id) {
        // TODO: Protect by master API or client API
        clientRepository.deleteById(id);
    }
}
