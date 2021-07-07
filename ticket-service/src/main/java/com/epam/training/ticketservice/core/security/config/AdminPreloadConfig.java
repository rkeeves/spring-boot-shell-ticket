package com.epam.training.ticketservice.core.security.config;

import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"ci", "test"})
public class AdminPreloadConfig {

    @Bean
    public AdminPreloader adminPreloader(PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
        return new AdminPreloader(passwordEncoder,  accountRepository);
    }
}
