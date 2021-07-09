package com.epam.training.ticketservice.core.security.service;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDetailsServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountDetailsService accountDetailsService;

    @Test
    void givenNonPrivilegedUserByUsernameExists_whenLoadUserByUsername_thenReturnCorrectUserDetails() {
        // given
        var username = "user";
        var password = "pass";
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password(password)
                .privileged(false)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        // when
        var actual = accountDetailsService.loadUserByUsername(username);
        // then
        assertEquals(username, actual.getUsername());
        assertEquals(password, actual.getPassword());
        var authorities = actual.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(Set.of(SecurityService.NON_PRIVILEGED_ROLE_NAME), authorities);
        verify(accountRepository, times(1))
                .findByUsername(username);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void givenPrivilegedUserByUsernameExists_whenLoadUserByUsername_thenReturnCorrectUserDetails() {
        // given
        var username = "user";
        var password = "pass";
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password(password)
                .privileged(true)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        // when
        var actual = accountDetailsService.loadUserByUsername(username);
        // then
        assertEquals(username, actual.getUsername());
        assertEquals(password, actual.getPassword());
        var authorities = actual.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertEquals(Set.of(SecurityService.NON_PRIVILEGED_ROLE_NAME, SecurityService.PRIVILEGED_ROLE_NAME), authorities);
        verify(accountRepository, times(1))
                .findByUsername(username);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void givenUserByUsernameDoesNotExists_whenLoadUserByUsername_thenThrowUsernameNotFoundException() {
        // given
        var username = "user";
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        // when
        assertThrows(UsernameNotFoundException.class, () -> accountDetailsService.loadUserByUsername(username));
        // then
        verify(accountRepository, times(1))
                .findByUsername(username);
        verifyNoMoreInteractions(accountRepository);
    }
}