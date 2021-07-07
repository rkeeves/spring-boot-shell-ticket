package com.epam.training.ticketservice.shell.command;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles({"test"})
class AccountCommandShellTest {

    @Autowired
    private Shell shell;

    @Test
    @DirtiesContext
    void theAdminAccountExistsByDefaultAndCanBeLoggedInWithTheCorrectPassword() {
        eval("sign in privileged admin admin");
        assertEquals(
                List.of("Signed in with privileged account 'admin'",
                        "You have not booked any tickets yet"),
                eval("describe account"));
    }

    @Test
    @DirtiesContext
    void theAdminAccountCanBeSignedOut() {
        eval("sign in privileged admin admin");
        eval("sign out");
        assertEquals(
                List.of("You are not signed in"),
                eval("describe account"));
    }

    @Test
    @DirtiesContext
    void theAdminAccountCanNotBeLoggedInWithIncorrectPassword() {
        assertEquals(
                Optional.of("Login failed due to incorrect credentials"),
                eval("sign in privileged admin asdQWE123"));
        assertEquals(
                List.of("You are not signed in"),
                eval("describe account"));
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanBeCreatedAndLoggedInWithTheCorrectPassword() {
        eval("sign up sanyi asdQWE123");
        eval("sign in sanyi asdQWE123");
        assertEquals(
                List.of("Signed in with account 'sanyi'",
                        "You have not booked any tickets yet"),
                eval("describe account"));
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanNotBeLoggedInWithTheIncorrectPassword() {
        eval("sign up sanyi asdQWE123");
        assertEquals(
                Optional.of("Login failed due to incorrect credentials"),
                eval("sign in sanyi alma"));
        assertEquals(
                List.of("You are not signed in"),
                eval("describe account"));
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanBeSignedOut() {
        eval("sign up sanyi asdQWE123");
        eval("sign in sanyi asdQWE123");
        eval("sign out");
        assertEquals(
                List.of("You are not signed in"),
                eval("describe account"));
    }

    private Object eval(String command) {
        return shell.evaluate(() -> command);
    }
}
