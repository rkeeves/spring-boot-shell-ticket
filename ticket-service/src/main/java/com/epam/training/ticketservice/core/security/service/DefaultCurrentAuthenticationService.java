package com.epam.training.ticketservice.core.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultCurrentAuthenticationService implements CurrentAuthenticationService {

    @Override
    public void setAuthentication(Authentication authentication) {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .ifPresent(securityContext -> securityContext.setAuthentication(authentication));
    }

    @Override
    public Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication);
    }
}