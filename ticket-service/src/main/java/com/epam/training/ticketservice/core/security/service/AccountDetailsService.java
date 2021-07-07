package com.epam.training.ticketservice.core.security.service;

import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import com.epam.training.ticketservice.core.security.adapter.AccountToUserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .map(AccountToUserDetailsAdapter::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
