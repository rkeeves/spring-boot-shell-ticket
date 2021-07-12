package com.epam.training.ticketservice.shell.command;

import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecuredCommandTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private SecuredCommand securedCommand;

    @Test
    void givenUserIsAuthenticated_whenIsAuthenticated_thenReturnAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(true);
        // when
        var actual = securedCommand.isAuthenticated();
        // then
        assertTrue(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsNotAuthenticated_whenIsAuthenticated_thenReturnNotAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(false);
        // when
        var actual = securedCommand.isAuthenticated();
        // then
        assertFalse(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsAuthenticated_whenIsNotAuthenticated_thenReturnNotAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(true);
        // when
        var actual = securedCommand.isNotAuthenticated();
        // then
        assertFalse(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsNotAuthenticated_whenIsNotAuthenticated_thenReturnAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(false);
        // when
        var actual = securedCommand.isNotAuthenticated();
        // then
        assertTrue(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsAuthenticatedAndPrivileged_whenIsPrivileged_thenReturnAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(true);
        when(securityService.isPrivileged())
                .thenReturn(true);
        // when
        var actual = securedCommand.isPrivileged();
        // then
        assertTrue(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verify(securityService, times(1))
                .isPrivileged();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsNotAuthenticated_whenIsPrivileged_thenReturnNotAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(false);
        // when
        var actual = securedCommand.isPrivileged();
        // then
        assertFalse(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenUserIsAuthenticatedButNotPrivileged_whenIsPrivileged_thenReturnNotAvailable() {
        // given
        when(securityService.isAuthenticated())
                .thenReturn(true);
        when(securityService.isPrivileged())
                .thenReturn(false);
        // when
        var actual = securedCommand.isPrivileged();
        // then
        assertFalse(actual.isAvailable());
        verify(securityService, times(1))
                .isAuthenticated();
        verify(securityService, times(1))
                .isPrivileged();
        verifyNoMoreInteractions(securityService);
    }
}