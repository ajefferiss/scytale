package com.openmoments.scytale.authentication;

import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.exceptions.ClientNotFoundException;
import com.openmoments.scytale.repositories.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedClient {

    private ClientRepository clientRepository;

    public AuthenticatedClient(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClient() {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

        String clientId = authenticatedUser.getPrincipal().toString();

        return clientRepository.findById(Long.valueOf(clientId)).orElseThrow(
                () -> new ClientNotFoundException(Long.valueOf(clientId))
        );
    }
}
