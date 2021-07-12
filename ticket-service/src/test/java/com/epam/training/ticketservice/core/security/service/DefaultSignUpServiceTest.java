package com.epam.training.ticketservice.core.security.service;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultSignUpServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultSignUpService signUpService;

    @Test
    void givenUserNameDoesNotExistYet_whenSignUp_thenSaveUser(){
        // given
        var username = "username";
        var password = "password";
        var hashedPassword = "hashedPassword";
        var accountPreSave = Account.builder()
                .username(username)
                .password(hashedPassword)
                .build();
        var accountPostSave = Account.builder()
                .id(1L)
                .username(username)
                .password(hashedPassword)
                .build();
        when(passwordEncoder.encode(password))
                .thenReturn(hashedPassword);
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(accountRepository.save(accountPreSave))
                .thenReturn(accountPostSave);
        // when
        signUpService.signUp(username, password);
        // then
        var inOrder = inOrder(accountRepository);
        inOrder.verify(accountRepository, times(1))
                .findByUsername(username);
        inOrder.verify(accountRepository, times(1))
                .save(accountPreSave);
        verifyNoMoreInteractions(accountRepository);
        verify(passwordEncoder, times(1))
                .encode(password);
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void givenUserNameExistsAlready_whenSignUp_thenThrowException(){
        // given
        var id = 1L;
        var username = "username";
        var password = "password";
        var existingAccountsPassword = "somePassword";
        var existingAccount = Account.builder()
                .id(id)
                .username(username)
                .password(existingAccountsPassword)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(existingAccount));
        // when
        assertThrows(AccountAlreadyExistsException.class, () -> signUpService.signUp(username, password));
        // then
        verify(accountRepository, times(1)).findByUsername(username);
        verifyNoMoreInteractions(accountRepository);
        verify(passwordEncoder, times(0)).encode(anyString());
        verifyNoMoreInteractions(passwordEncoder);
    }

    @Test
    void givenUserNameDoesNotExistYetButRepositoryThrows_whenSignUp_thenThrowException(){
        // given
        var username = "username";
        var password = "password";
        var hashedPassword = "hashedPassword";
        var accountPreSave = Account.builder()
                .username(username)
                .password(hashedPassword)
                .build();
        when(passwordEncoder.encode(password))
                .thenReturn(hashedPassword);
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        doThrow(new RuntimeException())
                .when(accountRepository)
                .save(accountPreSave);
        // when
        assertThrows(RuntimeException.class, () -> signUpService.signUp(username, password));
        // then
        verify(passwordEncoder, times(1))
                .encode(password);
        verifyNoMoreInteractions(passwordEncoder);
        var inOrder = inOrder(accountRepository);
        inOrder.verify(accountRepository, times(1))
                .findByUsername(username);
        inOrder.verify(accountRepository, times(1))
                .save(accountPreSave);
        verifyNoMoreInteractions(accountRepository);
    }

}