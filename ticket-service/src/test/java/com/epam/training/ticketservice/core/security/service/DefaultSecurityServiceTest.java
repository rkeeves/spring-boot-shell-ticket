package com.epam.training.ticketservice.core.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSecurityServiceTest {

    @Mock
    private CurrentAuthenticationService currentAuthenticationService;

    @InjectMocks
    private DefaultSecurityService defaultSecurityService;

    @Test
    void givenCurrentUserIsAuthenticated_whenIsAuthenticated_thenReturnTrue(){
        // given
        var username = "username";
        var password = "password";
        var authentication = new TestingAuthenticationToken(username, password, SecurityService.NON_PRIVILEGED_ROLE_NAME);
        authentication.setAuthenticated(true);
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.of(authentication));
        // when
        var result = defaultSecurityService.isAuthenticated();
        // then
        assertTrue(result);
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenNoCurrentUser_whenIsAuthenticated_thenReturnTrue(){
        // given
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.empty());
        // when
        var result = defaultSecurityService.isAuthenticated();
        // then
        assertFalse(result);
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenNoCurrentUser_whenIsPrivileged_thenReturnFalse(){
        // given
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.empty());
        // when
        var result = defaultSecurityService.isPrivileged();
        // then
        assertFalse(result);
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenCurrentUserOnlyHasNonPrivilegedRole_whenIsPrivileged_thenReturnFalse(){
        // given
        var username = "username";
        var password = "password";
        var authentication = new TestingAuthenticationToken(username, password, SecurityService.NON_PRIVILEGED_ROLE_NAME);
        authentication.setAuthenticated(true);
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.of(authentication));
        // when
        var result = defaultSecurityService.isPrivileged();
        // then
        assertFalse(result);
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenCurrentUserHasPrivilegedRole_whenIsPrivileged_thenReturnTrue(){
        // given
        var username = "username";
        var password = "password";
        var authentication = new TestingAuthenticationToken(username, password,
                SecurityService.NON_PRIVILEGED_ROLE_NAME, SecurityService.PRIVILEGED_ROLE_NAME);
        authentication.setAuthenticated(true);
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.of(authentication));
        // when
        var result = defaultSecurityService.isPrivileged();
        // then
        assertTrue(result);
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenCurrentUserExists_whenUsername_thenReturnUsername(){
        // given
        var username = "username";
        var password = "password";
        var authentication = new TestingAuthenticationToken(username, password,
                SecurityService.NON_PRIVILEGED_ROLE_NAME, SecurityService.PRIVILEGED_ROLE_NAME);
        authentication.setAuthenticated(true);
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.of(authentication));
        // when
        var result = defaultSecurityService.username();
        // then
        assertTrue(result.isPresent());
        assertEquals(username, result.get());
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }

    @Test
    void givenCurrentUserDoesNotExist_whenUsername_thenReturnEmpty(){
        // given
        when(currentAuthenticationService.getAuthentication())
                .thenReturn(Optional.empty());
        // when
        var result = defaultSecurityService.username();
        // then
        assertTrue(result.isEmpty());
        verify(currentAuthenticationService, times(1))
                .getAuthentication();
        verifyNoMoreInteractions(currentAuthenticationService);
    }
}