package com.openmoments.scytale.controllers;

import com.openmoments.scytale.authentication.AuthenticatedClient;
import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.entities.Keystore;
import com.openmoments.scytale.entities.PublicKey;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.exceptions.KeystoreNotFoundException;
import com.openmoments.scytale.exceptions.PublicKeyNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import com.openmoments.scytale.repositories.KeystoreRepository;
import com.openmoments.scytale.repositories.PublicKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicKeyControllerTest {

    PublicKeyController publicKeyController;
    PublicKeyModelAssembler publicKeyModelAssembler;
    AuthenticatedClient authenticatedClient;

    @Mock
    KeystoreRepository keystoreRepository;

    @Mock
    PublicKeyRepository publicKeyRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    Client clientMock;

    @Mock
    Keystore keystoreMock;

    @Mock
    PublicKey publicKeyMock;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("1", "API_KEY")
        );

        publicKeyModelAssembler = new PublicKeyModelAssembler();
        authenticatedClient = new AuthenticatedClient(clientRepository);
        publicKeyController = new PublicKeyController(publicKeyRepository, keystoreRepository, publicKeyModelAssembler, authenticatedClient);
    }

    @Test
    void shouldThrowWhenKeystoreUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreRepository.findById(any())).thenReturn(Optional.empty());

        Exception keystoreNotFound = assertThrows(KeystoreNotFoundException.class, () -> publicKeyController.one(1L, 1L));
        assertEquals("Could not find keystore 1", keystoreNotFound.getMessage());
    }

    @Test
    void shouldThrowWhenClientUnknown() {
        Client validClient = mock(Client.class);
        when(validClient.getId()).thenReturn(1L);

        when(clientRepository.findById(any())).thenReturn(Optional.of(validClient));
        when(clientMock.getId()).thenReturn(2L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));

        Exception clientNotFound = assertThrows(ClientNotFoundException.class, () -> publicKeyController.one(1L, 1L));
        assertEquals("Could not find client 1", clientNotFound.getMessage());
    }

    @Test
    void shouldThrowWhenPublicKeyUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));
        when(publicKeyRepository.findOne(any())).thenReturn(Optional.empty());

        Exception publicKeyNotFound = assertThrows(PublicKeyNotFoundException.class, () -> publicKeyController.one(1L, 1L));
        assertEquals("Could not find public key 1", publicKeyNotFound.getMessage());
    }

    @Test
    void shouldReturnSingle() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(publicKeyMock.getId()).thenReturn(1L);
        when(publicKeyMock.getPublicKey()).thenReturn("Public Key");
        when(publicKeyMock.getKeystore()).thenReturn(keystoreMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));
        when(publicKeyRepository.findOne(any())).thenReturn(Optional.of(publicKeyMock));

        MappingJacksonValue json = publicKeyController.one(1L, 1L);
        EntityModel<PublicKey> singlePublicKey = (EntityModel<PublicKey>)json.getValue();

        assertEquals("Public Key", singlePublicKey.getContent().getPublicKey());
    }

    @Test
    void allShouldThrowWhenKeystoreUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreRepository.findById(any())).thenReturn(Optional.empty());

        Exception keystoreNotFound = assertThrows(KeystoreNotFoundException.class, () -> publicKeyController.all(1L));
        assertEquals("Could not find keystore 1", keystoreNotFound.getMessage());
    }

    @Test
    void allShouldThrowWhenClientUnknown() {
        Client validClient = mock(Client.class);
        when(validClient.getId()).thenReturn(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(validClient));
        when(clientMock.getId()).thenReturn(2L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));

        Exception clientNotFound = assertThrows(ClientNotFoundException.class, () -> publicKeyController.all(1L));
        assertEquals("Could not find client 1", clientNotFound.getMessage());
    }

    @Test
    void shouldReturnListOfPublicKeys() {
        PublicKey publicKeyMock1 = mock(PublicKey.class);

        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(publicKeyMock.getKeystore()).thenReturn(keystoreMock);
        when(publicKeyMock1.getKeystore()).thenReturn(keystoreMock);
        when(keystoreRepository.findById(any())).thenReturn(Optional.of(keystoreMock));
        when(publicKeyRepository.findAll(any(Example.class))).thenReturn(List.of(publicKeyMock, publicKeyMock1));

        MappingJacksonValue json = publicKeyController.all(1L);
        List<EntityModel<Client>> allEntities = (List<EntityModel<Client>>)json.getValue();

        assertEquals(2, allEntities.size());
    }

    @Test
    void shouldAddNewPublicKey() {
        when(clientMock.getId()).thenReturn(1L);
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(1L)).thenReturn(Optional.of(keystoreMock));

        PublicKey publicKey = new PublicKey();
        publicKey.setKeystore(keystoreMock);
        publicKey.setPublicKey("Test Public Key");

        when(publicKeyRepository.save(any())).thenReturn(publicKey);

        MappingJacksonValue json = publicKeyController.newPublicKey(1L, publicKey);
        EntityModel<PublicKey> savedKeystore = (EntityModel<PublicKey>)json.getValue();

        assertEquals("Test Public Key", savedKeystore.getContent().getPublicKey());
    }

    @Test
    void shouldUpdatePublicKeyDetails() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(1L)).thenReturn(Optional.of(keystoreMock));

        PublicKey originalPublicKey = new PublicKey();
        originalPublicKey.setId(1L);
        originalPublicKey.setKeystore(keystoreMock);
        originalPublicKey.setPublicKey("Test Public Key");

        PublicKey newPublicKey = new PublicKey();
        newPublicKey.setId(1L);
        newPublicKey.setKeystore(keystoreMock);
        newPublicKey.setPublicKey("Updated Public Key");

        when(publicKeyRepository.findById(1L)).thenReturn(Optional.of(originalPublicKey));
        when(publicKeyRepository.save(any())).thenReturn(newPublicKey);

        MappingJacksonValue json = publicKeyController.replacePublicKey(1L,1L, newPublicKey);
        EntityModel<PublicKey> savedKeystore = (EntityModel<PublicKey>)json.getValue();

        assertEquals("Updated Public Key", savedKeystore.getContent().getPublicKey());
    }

    @Test
    void shouldAddPublicKeyIfIDUnknown() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(1L)).thenReturn(Optional.of(keystoreMock));

        PublicKey originalPublicKey = new PublicKey();
        originalPublicKey.setId(1L);
        originalPublicKey.setKeystore(keystoreMock);
        originalPublicKey.setPublicKey("Test Public Key");

        when(publicKeyRepository.findById(1L)).thenReturn(Optional.empty());
        when(publicKeyRepository.save(any())).thenReturn(originalPublicKey);

        MappingJacksonValue json = publicKeyController.replacePublicKey(1L, 1L, originalPublicKey);
        EntityModel<PublicKey> savedKeystore = (EntityModel<PublicKey>)json.getValue();

        assertEquals("Test Public Key", savedKeystore.getContent().getPublicKey());
    }

    @Test
    void verifyDeleteKeystore() {
        when(clientRepository.findById(any())).thenReturn(Optional.of(clientMock));
        when(clientMock.getId()).thenReturn(1L);
        when(keystoreMock.getClient()).thenReturn(clientMock);
        when(keystoreRepository.findById(1L)).thenReturn(Optional.of(keystoreMock));
        when(publicKeyRepository.findOne(any(Example.class))).thenReturn(Optional.of(publicKeyMock));

        publicKeyController.deletePublicKey(1L, 1L);
        verify(publicKeyRepository, times(1)).deleteById(1L);
    }
}