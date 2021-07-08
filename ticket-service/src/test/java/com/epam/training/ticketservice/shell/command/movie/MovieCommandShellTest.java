package com.epam.training.ticketservice.shell.command.movie;

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
class MovieCommandShellTest {

    @Autowired
    private Shell shell;

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotCreateMovies() {
        commandIsNotAvailable(shell, "create movie");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotCreateMovies() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "create movie");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanCreateAndListMovies() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create movie Sátántangó drama 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list movies")
                .expectOutput("Sátántangó (drama, 450 minutes)");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotUpdateMovies() {
        commandIsNotAvailable(shell, "update movie");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotUpdateMovies() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "update movie");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanUpdateAMovie() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create movie Sátántangó dram 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("update movie Sátántangó drama 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list movies")
                .expectOutput("Sátántangó (drama, 450 minutes)");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotDeleteMovies() {
        commandIsNotAvailable(shell, "delete movie");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotDeleteMovies() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "delete movie");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanDeleteAMovie() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create movie Sátántangó drama 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("delete movie Sátántangó")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list movies")
                .expectOutput("There are no movies at the moment");
    }

    @Test
    @DirtiesContext
    void anUnauthenticatedUserCanListMovies() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create movie Sátántangó drama 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign out")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list movies")
                .expectOutput("Sátántangó (drama, 450 minutes)");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedNonPrivilegedUserCanListMovies() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create movie Sátántangó drama 450")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign out")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list movies")
                .expectOutput("Sátántangó (drama, 450 minutes)");
    }
}
