package com.epam.training.ticketservice.core.account.repository;

import com.epam.training.ticketservice.core.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void givenUserEntityIsValid_whenSave_thenPersist() {
        // given
        var username = "someUsername";
        var password = "somePassword";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        // when
        accountRepository.save(account);
        // then
        assertDoesNotThrow(() -> testEntityManager.flush());
    }

    @Test
    void givenUsernameIsNull_whenSave_thenThrow() {
        // given
        var password = "somePassword";
        var account = Account.builder()
                .username(null)
                .password(password)
                .build();
        // when
        accountRepository.save(account);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenUsernameIsEmpty_whenSave_thenThrow() {
        // given
        var username = "";
        var password = "somePassword";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        // when
        accountRepository.save(account);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenPasswordIsNull_whenSave_thenThrow() {
        // given
        var username = "someUser";
        var account = Account.builder()
                .username(username)
                .password(null)
                .build();
        // when
        accountRepository.save(account);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenPasswordIsEmpty_whenSave_thenThrow() {
        // given
        var username = "someUser";
        var password = "";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        // when
        accountRepository.save(account);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenUsernameAlreadyExists_whenSave_thenThrow() {
        // given
        var username = "someUser";
        var password = "password";
        var otherPassword = "otherPassword";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        accountRepository.save(account);
        var newAccount = Account.builder()
                .username(username)
                .password(otherPassword)
                .build();
        // when
        accountRepository.save(newAccount);
        // then
        assertThrows(PersistenceException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenUserEntityByUsernameExists_whenFindByUsername_thenReturnUser() {
        // given
        var username = "someUsername";
        var password = "somePassword";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        accountRepository.save(account);
        // when
        var result = accountRepository.findByUsername(username);
        // then
        assertTrue(result.isPresent());
        var savedUser = result.get();
        assertEquals(account.getUsername(), savedUser.getUsername());
        assertEquals(account.getPassword(), savedUser.getPassword());
        assertEquals(account.isPrivileged(), savedUser.isPrivileged());
        assertDoesNotThrow(() -> testEntityManager.flush());
    }

    @Test
    void givenUserEntityByUsernameDoesNotExist_whenFindByUsername_thenReturnEmpty() {
        // given
        var username = "someUsername";
        // when
        var result = accountRepository.findByUsername(username);
        // then
        assertTrue(result.isEmpty());
        assertDoesNotThrow(() -> testEntityManager.flush());
    }
}