package com.epam.training.ticketservice;

import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles({"test"})
class TicketApplicationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads(){

    }

    @Test
    void adminExistsForTests(){
        var result = accountRepository.findByUsername("admin");
        assertTrue(result.isPresent());
        assertTrue(result.get().isPrivileged());
    }
}