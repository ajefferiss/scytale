package com.openmoments.scytale.controllers;

import com.openmoments.scytale.authentication.AuthenticatedClient;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.entities.Keystore;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.repositories.KeystoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeystoreControllerTest {

    KeystoreController keystoreController;
    KeystoreModelAssembler keystoreModelAssembler;
    AuthenticatedClient authenticatedClient;

    @Mock
    ClientRepository clientRepository;

    @Mock
    KeystoreRepository keystoreRepository;

    @Mock
    Client clientMock;

    @Mock
    Keystore keystoreMock;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("1", "API_KEY")
        );
        keystoreModelAssembler = new KeystoreModelAssembler();
        authenticatedClient = new AuthenticatedClient(clientRepository);
        keystoreController = new KeystoreController(keystoreRepository, keystoreModelAssembler, authenticatedClient);
    }

    @Test
    void shouldThrowWhenClientUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        Exception clientNotFound = assertThrows(ClientNotFoundException.class, () -> keystoreController.one(1L));
        assertEquals("Could not find client 1", clientNotFound.getMessage());
    }

    @Test
    void shouldThrowWhenKeystoreUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreRepository.findOne(any())).thenReturn(Optional.empty());

        Exception keystoreNotFound = assertThrows(KeystoreNotFoundException.class, () -> keystoreController.one(1L));
        assertEquals("Could not find keystore 1", keystoreNotFound.getMessage());
    }

    @Test
    void shouldReturnSingleKeystore() {
        when(keystoreMock.getName()).thenReturn("Test Keystore");
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreRepository.findOne(any())).thenReturn(Optional.of(keystoreMock));

        MappingJacksonValue json = keystoreController.one(1L);
        EntityModel<Keystore> singleKeystore = (EntityModel<Keystore>)json.getValue();

        assertEquals("Test Keystore", singleKeystore.getContent().getName());
    }

    @Test
    void allKeystoresShouldThrowWhenClientUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        Exception clientNotFound = assertThrows(ClientNotFoundException.class, () -> keystoreController.all());
        assertEquals("Could not find client 1", clientNotFound.getMessage());
    }

    @Test
    void shouldReturnListOfKeystores() {
        Keystore keystoreMock1 = mock(Keystore.class);

        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreRepository.findAll(any(Example.class))).thenReturn(List.of(keystoreMock, keystoreMock1));

        MappingJacksonValue json = keystoreController.all();
        List<EntityModel<Client>> allEntities = (List<EntityModel<Client>>)json.getValue();

        assertEquals(2, allEntities.size());
    }

    @Test
    void shouldAddANewKeystore() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));

        Keystore newKeystore = new Keystore();
        newKeystore.setClient(clientMock);
        newKeystore.setName("Test Keystore");

        when(keystoreRepository.save(any())).thenReturn(newKeystore);

        MappingJacksonValue json = keystoreController.newKeystore(newKeystore);
        EntityModel<Keystore> savedKeystore = (EntityModel<Keystore>)json.getValue();

        assertEquals("Test Keystore", savedKeystore.getContent().getName());
    }

    @Test
    void shouldUpdateKeystoreDetails() {
        when(clientMock.getId()).thenReturn(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));

        Keystore originalKeyStore = new Keystore();
        originalKeyStore.setId(1L);
        originalKeyStore.setClient(clientMock);
        originalKeyStore.setName("Test Keystore");

        Keystore newKeystore = new Keystore();
        newKeystore.setId(1L);
        newKeystore.setClient(clientMock);
        newKeystore.setName("Updated Keystore");

        when(keystoreRepository.findById(1L)).thenReturn(Optional.of(originalKeyStore));
        when(keystoreRepository.save(any())).thenReturn(newKeystore);

        MappingJacksonValue json = keystoreController.replaceKeystore(originalKeyStore.getId(), newKeystore);
        EntityModel<Keystore> savedKeystore = (EntityModel<Keystore>)json.getValue();

        assertEquals("Updated Keystore", savedKeystore.getContent().getName());
    }

    @Test
    void shouldAddKeystoreIfIDUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));

        Keystore newKeystore = new Keystore();
        newKeystore.setId(1L);
        newKeystore.setClient(clientMock);
        newKeystore.setName("Updated Keystore");

        when(keystoreRepository.findById(1L)).thenReturn(Optional.empty());
        when(keystoreRepository.save(any())).thenReturn(newKeystore);

        MappingJacksonValue json = keystoreController.replaceKeystore(1L, newKeystore);
        EntityModel<Keystore> savedKeystore = (EntityModel<Keystore>)json.getValue();

        assertEquals("Updated Keystore", savedKeystore.getContent().getName());
    }

    @Test
    void verifyDeleteKeystore() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));

        keystoreController.deleteKeystore(1L);
        verify(keystoreRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenTryingToDeleteFromDifferentClient() {
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(5L);

        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));


        Exception clientNotFound = assertThrows(BadCredentialsException.class, () -> keystoreController.deleteKeystore(1L));
        assertEquals("Authentication was not found or not the expected value", clientNotFound.getMessage());

    }
}