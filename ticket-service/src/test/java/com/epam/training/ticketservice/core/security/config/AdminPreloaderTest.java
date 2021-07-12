package com.epam.training.ticketservice.core.security.config;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminPreloaderTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AdminPreloader adminPreloader;

    @Test
    void givenAdminUserExists_whenRun_thenNoSideEffect() {
        // given
        var account = Account.builder()
                .username("admin")
                .password("hashed")
                .privileged(true)
                .build();
        when(accountRepository.findByUsername("admin"))
                .thenReturn(Optional.of(account));
        // when
        assertDoesNotThrow(() -> adminPreloader.run());
        // then
        verify(accountRepository, times(1))
            .findByUsername("admin");
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void givenAdminUserDoesNotExist_whenRun_thenSaveNewAdmin() {
        // given
        var account = Account.builder()
                .username("admin")
                .password("hashed")
                .privileged(true)
                .build();
        when(accountRepository.findByUsername("admin"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin"))
                .thenReturn("hashed");
        when(accountRepository.save(account))
                .thenReturn(account);
        // when
        assertDoesNotThrow(() -> adminPreloader.run());
        // then
        verify(accountRepository, times(1))
                .findByUsername("admin");
        verify(passwordEncoder, times(1))
                .encode("admin");
        verify(accountRepository, times(1))
                .save(account);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(passwordEncoder);
    }
}