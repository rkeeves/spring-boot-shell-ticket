package com.epam.training.ticketservice.shell.command.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.epam.training.ticketservice.shell.evaluator.ShellEvaluator.commandIsNotAvailable;
import static com.epam.training.ticketservice.shell.evaluator.ShellEvaluator.usingShell;

@SpringBootTest
@ActiveProfiles({"test"})
class AccountCommandShellTest {

    @Autowired
    private Shell shell;

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotSignOut() {
        commandIsNotAvailable(shell, "sign out");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedUserCannotSignInButMustSignOutFirst() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "sign in");
    }

    @Test
    @DirtiesContext
    void theAdminAccountExistsByDefaultAndCanBeLoggedInWithTheCorrectPassword() {
        usingShell(shell)
            .afterCommand("sign in privileged admin admin")
            .expectOutput();
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("Signed in with privileged account 'admin'",
                        "You have not booked any tickets yet");
    }

    @Test
    @DirtiesContext
    void theAdminAccountCanBeSignedOut() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign out")
                .expectOutput();
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("You are not signed in");
    }

    @Test
    @DirtiesContext
    void theAdminAccountCanNotBeLoggedInWithIncorrectPassword() {
        usingShell(shell)
                .afterCommand("sign in privileged admin asdQWE123")
                .expectOutput("Login failed due to incorrect credentials");
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("You are not signed in");
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanBeCreatedAndLoggedInWithTheCorrectPassword() {
        usingShell(shell)
                .afterCommand("sign up sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("Signed in with account 'sanyi'",
                        "You have not booked any tickets yet");
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanNotBeLoggedInWithTheIncorrectPassword() {
        usingShell(shell)
                .afterCommand("sign up sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi alma")
                .expectOutput("Login failed due to incorrect credentials");
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("You are not signed in");
    }

    @Test
    @DirtiesContext
    void nonPrivilegedAccountsCanBeSignedOut() {
        usingShell(shell)
                .afterCommand("sign up sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign out sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("describe account")
                .expectOutput("You are not signed in");
    }
}
