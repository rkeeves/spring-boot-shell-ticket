package com.epam.training.ticketservice.core.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultSecurityService implements SecurityService {

    private final CurrentAuthenticationService currentAuthenticationService;

    @Override
    public boolean isAuthenticated() {
        return currentAuthenticationService.getAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }

    @Override
    public boolean isPrivileged() {
        return currentAuthenticationService.getAuthentication()
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch(SecurityService.PRIVILEGED_ROLE_NAME::equals))
                .orElse(false);
    }

    @Override
    public Optional<String> username() {
        return currentAuthenticationService.getAuthentication()
                .map(Authentication::getName);
    }
}
