package com.openmoments.scytale.controllers;

import com.openmoments.scytale.authentication.AuthenticationType;
import com.openmoments.scytale.entities.AuthType;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    ClientController clientController;
    ClientModelAssembler clientModelAssembler;
    Client userClient;
    Client adminClient;

    @Mock
    ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        userClient = new Client("Test User", new AuthType(AuthenticationType.API_KEY), "API_KEY");
        userClient.setId(1L);
        adminClient = new Client("Test Admin", new AuthType(AuthenticationType.CERTIFICATE), "", Client.ROLE_ADMIN);
        adminClient.setId(2L);
        clientModelAssembler = new ClientModelAssembler();
        clientController = new ClientController(clientRepository, clientModelAssembler);
    }

    @Test
    void shouldReturnSingleClient() {
        when(clientRepository.findById(any())).thenReturn(Optional.ofNullable(userClient));

        MappingJacksonValue json = clientController.one(1L);
        EntityModel<Client> singleClient = (EntityModel<Client>)json.getValue();

        assertEquals(userClient, singleClient.getContent());
    }

    @Test
    void shouldThrowClientNotFoundWhenMissing() {
        Exception clientNotFound = assertThrows(ClientNotFoundException.class, () -> clientController.one(1L));
        assertEquals("Could not find client 1", clientNotFound.getMessage());
    }

    @Test
    void shouldReturnAllClients() {
        when(clientRepository.findAll()).thenReturn(List.of(userClient, adminClient));

        MappingJacksonValue json = clientController.all();
        List<EntityModel<Client>> allEntities = (List<EntityModel<Client>>)json.getValue();
        EntityModel<Client> userEntityModel = clientModelAssembler.toModel(userClient);
        EntityModel<Client> adminEntityModel = clientModelAssembler.toModel(adminClient);

        assertEquals(2, allEntities.size());
        assertTrue(allEntities.contains(userEntityModel));
        assertTrue(allEntities.contains(adminEntityModel));
    }

    @Test
    void shouldAddANewClientWithAPIKey() {
        Client newClient = new Client();
        newClient.setName("Test Add");
        newClient.setAuthType(new AuthType(AuthenticationType.API_KEY));

        when(clientRepository.save(any())).thenReturn(newClient);

        MappingJacksonValue json = clientController.newClient(newClient);
        EntityModel<Client> savedClient = (EntityModel<Client>)json.getValue();

        assertEquals("Test Add", savedClient.getContent().getName());
        // An API key is automatically generated for us when using API keys
        assertTrue(!savedClient.getContent().getApiKey().isEmpty());
    }

    @Test
    void shouldAddANewClientWithCertificate() {
        Client newClient = new Client();
        newClient.setName("Test Add");
        newClient.setAuthType(new AuthType(AuthenticationType.CERTIFICATE));

        when(clientRepository.save(any())).thenReturn(newClient);

        MappingJacksonValue json = clientController.newClient(newClient);
        EntityModel<Client> savedClient = (EntityModel<Client>)json.getValue();

        assertEquals("Test Add", savedClient.getContent().getName());
        // An API key should not be automatically generated for us when using certificates
        assertTrue(savedClient.getContent().getApiKey().isEmpty());
    }

    @Test
    void shouldUpdateClientDetails() {
        Client updatedClient = new Client("Test User Cert", new AuthType(AuthenticationType.CERTIFICATE), "");
        updatedClient.setId(userClient.getId());

        when(clientRepository.findById(userClient.getId())).thenReturn(Optional.ofNullable(userClient));
        when(clientRepository.save(any())).thenReturn(updatedClient);

        MappingJacksonValue json = clientController.replaceClient(updatedClient, userClient.getId());
        EntityModel<Client> savedClient = (EntityModel<Client>)json.getValue();

        Client returnedClient = savedClient.getContent();
        assertEquals("Test User Cert", returnedClient.getName());
        assertTrue(returnedClient.getApiKey().isEmpty());
        assertEquals(AuthenticationType.CERTIFICATE, returnedClient.getAuthType().getAuthType());
    }

    @Test
    void shouldGenerateAPIKeyWhenChangingAuthenticationTypes() {
        Client updatedClient = new Client();
        updatedClient.setId(adminClient.getId());
        updatedClient.setName(adminClient.getName());
        updatedClient.setAuthType(new AuthType(AuthenticationType.API_KEY));

        when(clientRepository.findById(adminClient.getId())).thenReturn(Optional.ofNullable(adminClient));
        when(clientRepository.save(any())).thenReturn(updatedClient);

        MappingJacksonValue json = clientController.replaceClient(updatedClient, adminClient.getId());
        EntityModel<Client> saveClient = (EntityModel<Client>)json.getValue();

        Client returnedClient = saveClient.getContent();
        assertEquals(adminClient.getName(), returnedClient.getName());
        assertEquals(AuthenticationType.API_KEY, returnedClient.getAuthType().getAuthType());
        assertTrue(!returnedClient.getApiKey().isEmpty());
    }

    @Test
    void shouldAddClientWithAPIKeyIfIDUnknown() {
        Client updatedClient = new Client();
        updatedClient.setId(5L);
        updatedClient.setName("New Client Without ID");
        updatedClient.setAuthType(new AuthType(AuthenticationType.API_KEY));


        when(clientRepository.findById(updatedClient.getId())).thenReturn(Optional.empty());
        when(clientRepository.save(any())).thenReturn(updatedClient);

        MappingJacksonValue json = clientController.replaceClient(updatedClient, updatedClient.getId());
        EntityModel<Client> savedClient = (EntityModel<Client>)json.getValue();

        Client returnedClient = savedClient.getContent();
        assertEquals("New Client Without ID", returnedClient.getName());
        assertEquals(5L, returnedClient.getId());
        assertTrue(!returnedClient.getApiKey().isEmpty());
        assertEquals(AuthenticationType.API_KEY, returnedClient.getAuthType().getAuthType());
    }

    @Test
    void verifyDeleteCalled() {
        clientController.deleteClient(adminClient.getId());
        verify(clientRepository, times(1)).deleteById(adminClient.getId());
    }
}