package com.epam.training.ticketservice.core.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSignInSignOutServiceTest {

    @Mock
    private CurrentAuthenticationService currentAuthenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private DefaultSignInSignOutService signInSignOutService;

    @Test
    void givenAuthenticationSucceeds_whenSignIn_UserIsSetAsCurrent(){
        // given
        var username = "username";
        var enteredPassword = "enteredPassword";
        var storedPassword = "storedPassword";
        var preAuthenticationToken = new UsernamePasswordAuthenticationToken(username, enteredPassword);
        var postAuthenticationToken = new TestingAuthenticationToken(username, storedPassword, SecurityService.NON_PRIVILEGED_ROLE_NAME);
        postAuthenticationToken.setAuthenticated(true);
        when(authenticationManager.authenticate(preAuthenticationToken))
                .thenReturn(postAuthenticationToken);
        // when
        signInSignOutService.signIn(username, enteredPassword);
        // then
        verify(authenticationManager, times(1))
                .authenticate(preAuthenticationToken);
        verifyNoMoreInteractions(authenticationManager);
        verify(currentAuthenticationService, times(1))
                .setAuthentication(postAuthenticationToken);
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenUserIsNotRegistered_whenSignIn_ExceptionIsThrown(){
        // given
        var username = "username";
        var enteredPassword = "enteredPassword";
        var preAuthenticationToken = new UsernamePasswordAuthenticationToken(username, enteredPassword);
        doThrow(new UsernameNotFoundException(username))
                .when(authenticationManager)
                .authenticate(preAuthenticationToken);
        // when
        assertThrows(AuthenticationException.class, () -> signInSignOutService.signIn(username, enteredPassword));
        // then
        verify(authenticationManager, times(1))
                .authenticate(preAuthenticationToken);
        verifyNoMoreInteractions(authenticationManager);
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenPasswordDidntMatch_whenSignIn_ExceptionIsThrown(){
        // given
        var username = "username";
        var enteredPassword = "enteredPassword";
        var preAuthenticationToken = new UsernamePasswordAuthenticationToken(username, enteredPassword);
        var errorMessage = "error message";
        doThrow(new BadCredentialsException(errorMessage))
                .when(authenticationManager)
                .authenticate(preAuthenticationToken);
        // when
        assertThrows(AuthenticationException.class, () -> signInSignOutService.signIn(username, enteredPassword));
        // then
        verify(authenticationManager, times(1))
                .authenticate(preAuthenticationToken);
        verifyNoMoreInteractions(authenticationManager);
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenUserIsSignedIn_whenSignOut_CurrentUserInContextIsSetToNull(){
        // given
        // when
        signInSignOutService.signOut();
        // then
        verifyNoMoreInteractions(authenticationManager);
        verify(currentAuthenticationService, times(1))
                .setAuthentication(null);
        verifyNoMoreInteractions(currentAuthenticationService);
    }
}