package com.openmoments.scytale.controllers;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.utils.APIKeyGenerator;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClientModelAssembler assembler;

    private static final String CLIENT_FILTER = "clientFilter";
    private static final SimpleBeanPropertyFilter AUTH_FILTER = SimpleBeanPropertyFilter.serializeAllExcept("authType", "apiKey", "roles");
    private static final SimpleBeanPropertyFilter NO_FILTER = SimpleBeanPropertyFilter.serializeAll();
    private static final FilterProvider AUTH_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(CLIENT_FILTER, AUTH_FILTER);
    private static final FilterProvider NO_FILTER_PROVIDER = new SimpleFilterProvider().addFilter(CLIENT_FILTER, NO_FILTER);

    ClientController(ClientRepository clientRepository, ClientModelAssembler assembler) {
        this.clientRepository = clientRepository;
        this.assembler = assembler;
    }

    @RequestMapping(value="{id}", method = RequestMethod.GET)
    @PreAuthorize("#id == authentication.principal or hasRole('ROLE_ADMIN')")
    MappingJacksonValue one(@PathVariable Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        EntityModel<Client> resource = assembler.toModel(client);
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(NO_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(method = RequestMethod.GET)
    MappingJacksonValue all() {
        List<EntityModel<Client>> clients = clientRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        MappingJacksonValue mapping = new MappingJacksonValue(clients);
        mapping.setFilters(AUTH_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    MappingJacksonValue newClient(@RequestBody Client newClient) {
        if (newClient.getAuthType().getAuthType().equals(AuthenticationType.API_KEY)) {
            newClient.setApiKey(new APIKeyGenerator().buildKey());
        }

        EntityModel<Client> resource = assembler.toModel(clientRepository.save(newClient));
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(NO_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("#id == authentication.principal or hasRole('ROLE_ADMIN')")
    MappingJacksonValue replaceClient(@RequestBody Client newClient, @PathVariable Long id) {

        String newAPIKey = new APIKeyGenerator().buildKey();
        boolean addingAPIKey = newClient.getAuthType().getAuthType().equals(AuthenticationType.API_KEY);

        Client replacedClient = clientRepository.findById(id)
                .map(client -> {
                    if (!client.getAuthType().getAuthType().equals(AuthenticationType.API_KEY) && addingAPIKey) {
                        newClient.setApiKey(newAPIKey);
                    }
                    client.setName(newClient.getName());
                    client.setAuthType(newClient.getAuthType());
                    client.setApiKey(newClient.getApiKey());
                    return clientRepository.save(client);
                })
                .orElseGet(() -> {
                    newClient.setId(id);
                    if (addingAPIKey) {
                        newClient.setApiKey(newAPIKey);
                    }
                    return clientRepository.save(newClient);
                });

        EntityModel<Client> resource = assembler.toModel(replacedClient);
        MappingJacksonValue mapping = new MappingJacksonValue(resource);
        mapping.setFilters(NO_FILTER_PROVIDER);

        return mapping;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("#id == authentication.principal or hasRole('ROLE_ADMIN')")
    void deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
    }
}
