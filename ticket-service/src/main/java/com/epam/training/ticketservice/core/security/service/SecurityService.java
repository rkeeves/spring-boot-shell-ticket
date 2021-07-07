package com.epam.training.ticketservice.core.security.service;

import java.util.Optional;

public interface SecurityService {

    String PRIVILEGED_ROLE_NAME = "ADMIN";

    String NON_PRIVILEGED_ROLE_NAME = "USER";

    boolean isAuthenticated();

    boolean isPrivileged();

    Optional<String> username();
}
