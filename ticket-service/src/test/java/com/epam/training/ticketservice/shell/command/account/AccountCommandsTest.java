package com.epam.training.ticketservice.shell.command.account;

import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.core.security.service.SignInSignOutService;
import com.epam.training.ticketservice.core.security.service.SignUpService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountCommandsTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private SignInSignOutService signInSignOutService;

    @Mock
    private SignUpService signUpService;

    @InjectMocks
    private AccountCommands accountCommands;

    @Test
    void givenUserByUsernameDoesNotExist_whenSignIn_thenReturnIncorrectCredentialsErrorString() {
        // given
        var username = "username";
        var password = "password";
        doThrow(new UsernameNotFoundException(username))
                .when(signInSignOutService)
                .signIn(username, password);
        // when
        var result = accountCommands.signIn(username, password);
        // then
        assertEquals(List.of("Login failed due to incorrect credentials"), result);
        verify(signInSignOutService, times(1))
                .signIn(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenBadCredentials_whenSignIn_thenReturnIncorrectCredentialsErrorString() {
        // given
        var username = "username";
        var password = "password";
        doThrow(new BadCredentialsException(username))
                .when(signInSignOutService)
                .signIn(username, password);
        // when
        var result = accountCommands.signIn(username, password);
        // then
        assertEquals(List.of("Login failed due to incorrect credentials"), result);
        verify(signInSignOutService, times(1))
                .signIn(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenNonAuthenticationTypeException_whenSignIn_thenReturnGeneralErrorString() {
        // given
        var username = "username";
        var password = "password";
        doThrow(new RuntimeException())
                .when(signInSignOutService)
                .signIn(username, password);
        // when
        var result = accountCommands.signIn(username, password);
        // then
        assertEquals(List.of("Login failed due to general error"), result);
        verify(signInSignOutService, times(1))
                .signIn(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenValidCredentials_whenSignIn_thenReturnNothing() {
        // given
        var username = "username";
        var password = "password";
        // when
        var result = accountCommands.signIn(username, password);
        // then
        assertTrue(result.isEmpty());
        verify(signInSignOutService, times(1))
                .signIn(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void given_whenSignOut_thenReturnNothing() {
        // given
        // when
        var result = accountCommands.signOut();
        // then
       verify(signInSignOutService, times(1))
                .signOut();
        assertEquals(Collections.emptyList(), result);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenValidInput_whenSignUp_thenReturnNothing() {
        // given
        var username = "username";
        var password = "password";
        // when
        var result = accountCommands.signUp(username, password);
        // then
        assertTrue(result.isEmpty());
        verify(signUpService, times(1))
                .signUp(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenExistingUserCredentials_whenSignUp_thenReturnAlreadyExistsErrorMessage() {
        // given
        var username = "username";
        var password = "password";
        doThrow(new AccountAlreadyExistsException(username))
                .when(signUpService)
                .signUp(username, password);
        // when
        var result = accountCommands.signUp(username, password);
        // then
        assertEquals(List.of("Sign up failed, user by this username already exists"), result);
        verify(signUpService, times(1))
                .signUp(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenExceptionNotOfTypeAccountAlreadyExists_whenSignUp_thenReturnGeneralErrorMessage() {
        // given
        var username = "username";
        var password = "password";
        doThrow(new RuntimeException())
                .when(signUpService)
                .signUp(username, password);
        // when
        var result = accountCommands.signUp(username, password);
        // then
        assertEquals(List.of("Sign up failed due to general error"), result);
        verify(signUpService, times(1))
                .signUp(username, password);
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenUserIsSignedIn_whenDescribeAccount_thenReturnStandardMessage() {
        // given
        when(securityService.username())
                .thenReturn(Optional.empty());
        // when
        var result = accountCommands.describeAccount();
        // then
        assertEquals(List.of("You are not signed in"), result);
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenNonPrivilegedUserIsSignedIn_whenDescribeAccount_thenReturnUserSpecificMessage() {
        // given
        var username = "username";
        var greetLine = String.format("Signed in with account '%s'", username);
        var expected = List.of(greetLine,
                "You have not booked any tickets yet");
        when(securityService.username())
                .thenReturn(Optional.of(username));
        when(securityService.isAuthenticated())
                .thenReturn(true);
        when(securityService.isPrivileged())
                .thenReturn(false);
        // when
        var result = accountCommands.describeAccount();
        // then
        assertEquals(expected, result);
        verify(securityService, times(1))
                .isAuthenticated();
        verify(securityService, times(1))
                .isPrivileged();
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }

    @Test
    void givenPrivilegedUserIsSignedIn_whenDescribeAccount_thenReturnAdminSpecificMessage() {
        // given
        var username = "username";
        var greetLine = String.format("Signed in with privileged account '%s'", username);
        var expected = List.of(greetLine,
                "You have not booked any tickets yet");
        when(securityService.username())
                .thenReturn(Optional.of(username));
        when(securityService.isAuthenticated())
                .thenReturn(true);
        when(securityService.isPrivileged())
                .thenReturn(true);
        // when
        var result = accountCommands.describeAccount();
        // then
        assertEquals(expected, result);
        verify(securityService, times(1))
                .isAuthenticated();
        verify(securityService, times(1))
                .isPrivileged();
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(signInSignOutService);
        verifyNoMoreInteractions(signUpService);
    }
}