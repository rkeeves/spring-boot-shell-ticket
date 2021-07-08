package com.epam.training.ticketservice.core.security.service;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultSignUpService implements SignUpService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = AccountAlreadyExistsException.class)
    public void signUp(String username, String password) throws AccountAlreadyExistsException {
        var userExists = accountRepository.findByUsername(username).isPresent();
        if (userExists) {
            throw new AccountAlreadyExistsException(String.format("User by username %s exists", username));
        }
        var account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setPrivileged(false);
        accountRepository.save(account);
    }
}
