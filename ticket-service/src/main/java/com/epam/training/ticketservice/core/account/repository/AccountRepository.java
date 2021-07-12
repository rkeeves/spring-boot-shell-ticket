package com.epam.training.ticketservice.core.account.repository;

import com.epam.training.ticketservice.core.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Transactional(readOnly = true)
    Optional<Account> findByUsername(String username);
}
