package com.epam.training.ticketservice.core.security.service;

import org.springframework.security.core.AuthenticationException;

public interface SignInSignOutService {

    void signIn(String username, String password) throws AuthenticationException;

    void signOut();
}
