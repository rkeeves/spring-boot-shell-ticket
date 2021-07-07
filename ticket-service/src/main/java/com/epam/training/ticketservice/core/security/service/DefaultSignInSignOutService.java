package com.epam.training.ticketservice.core.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultSignInSignOutService implements SignInSignOutService {

    private final CurrentAuthenticationService currentAuthenticationService;

    private final AuthenticationManager authenticationManager;

    @Override
    public void signIn(String username, String password) {
        var validAuthentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        currentAuthenticationService.setAuthentication(validAuthentication);
    }

    @Override
    public void signOut() {
        currentAuthenticationService.setAuthentication(null);
    }
}
