package com.epam.training.ticketservice.shell.command.room;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.epam.training.ticketservice.shell.evaluator.ShellEvaluator.*;

@SpringBootTest
@ActiveProfiles({"test"})
class RoomCommandsShellTest {

    @Autowired
    private Shell shell;

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotCreateRooms() {
        commandIsNotAvailable(shell, "create room");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotCreateRooms() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "create room");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanCreateAndListRooms() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create room Pedersoli 20 10")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list rooms")
                .expectOutput("Room Pedersoli with 200 seats, 20 rows and 10 columns");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotUpdateRooms() {
        commandIsNotAvailable(shell, "update room");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotUpdateRooms() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "update room");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanUpdateARoom() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create room Pedersoli 20 10")
                .expectOutput();
        usingShell(shell)
                .afterCommand("update room Pedersoli 10 10")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list rooms")
                .expectOutput("Room Pedersoli with 100 seats, 10 rows and 10 columns");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotDeleteRooms() {
        commandIsNotAvailable(shell, "delete room");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotDeleteRooms() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "delete room");
    }

    @Test
    @DirtiesContext
    void anAdminUserCanDeleteARoom() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create room Pedersoli 20 10")
                .expectOutput();
        usingShell(shell)
                .afterCommand("delete room Pedersoli")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list rooms")
                .expectOutput("There are no rooms at the moment");
    }

    @Test
    @DirtiesContext
    void anUnauthenticatedUserCanListRooms() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create room Pedersoli 20 10")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign out")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list rooms")
                .expectOutput("Room Pedersoli with 200 seats, 20 rows and 10 columns");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedNonPrivilegedUserCanListRooms() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommand("create room Pedersoli 20 10")
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
                .afterCommand("list rooms")
                .expectOutput("Room Pedersoli with 200 seats, 20 rows and 10 columns");
    }
}
