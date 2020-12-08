package com.openmoments.scytale.authentication;

import com.openmoments.scytale.entities.Client;
import com.openmoments.scytale.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class APIAuthManager implements AuthenticationManager {
    private final List<Client> clients;

    public APIAuthManager(ClientRepository clientRepository) {
        clients = clientRepository.findAll();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String)authentication.getPrincipal();
        String subjectDN = (String)authentication.getCredentials();

        Predicate<Client> isAPIKey = c -> !principal.isEmpty() && principal.equals(c.getApiKey());
        Predicate<Client> isCertificate = c -> !subjectDN.isEmpty() && subjectDN.equals(c.getName());

        Client client = clients.stream()
                .filter(isAPIKey.or(isCertificate))
                .findAny()
                .orElseThrow(() -> new BadCredentialsException("Authentication was not found or not the expected value"));

        List<SimpleGrantedAuthority> grantedAuthorities = client.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(client.getId(), client.getApiKey(), grantedAuthorities);
    }
}
