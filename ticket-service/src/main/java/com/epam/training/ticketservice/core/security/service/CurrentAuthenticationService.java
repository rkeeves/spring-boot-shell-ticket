package com.epam.training.ticketservice.core.security.service;

import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface CurrentAuthenticationService {

    void setAuthentication(Authentication authentication);

    Optional<Authentication> getAuthentication();
}
