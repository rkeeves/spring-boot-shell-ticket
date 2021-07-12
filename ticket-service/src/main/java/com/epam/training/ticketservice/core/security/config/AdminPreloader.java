package com.epam.training.ticketservice.core.security.config;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

// We have to come before Spring shell, and Spring Shell comes at 0
@Order(-1)
@RequiredArgsConstructor
public class AdminPreloader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var result = accountRepository.findByUsername("admin");
        if (result.isEmpty()) {
            var admin = Account.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .privileged(true)
                    .build();
            accountRepository.save(admin);
        }
    }
}