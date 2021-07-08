package com.epam.training.ticketservice.shell.command.screening;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.epam.training.ticketservice.shell.evaluator.ShellEvaluator.*;

@SpringBootTest
@ActiveProfiles({"test"})
class ScreeningCommandsShellTest {

    @Autowired
    private Shell shell;

    @BeforeEach
    void init() {
        shell.evaluate(() -> "sign in admin admin");
        shell.evaluate(() -> "create room Pedersoli 20 10");
        shell.evaluate(() -> "create room Girotti 10 10");
        shell.evaluate(() -> "create movie Sátántangó drama 450");
        shell.evaluate(inputOfWords("create", "movie", "Spirited Away", "animation", "125"));
        shell.evaluate(() -> "sign out");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotCreateScreenings() {
        commandIsNotAvailable(shell, "create screening");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotCreateScreenings() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "create screening");
    }

    @Test
    @DirtiesContext
    void theAdminUserCanCreateAndQueryScreenings() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Sátántangó","Pedersoli","2021-03-15 10:45")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Spirited Away","Pedersoli","2021-03-14 16:00")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 10:45",
                        "Spirited Away (animation, 125 minutes), screened in room Pedersoli, at 2021-03-14 16:00");
    }

    @Test
    @DirtiesContext
    void aNotAuthenticatedUserCannotDeleteScreenings() {
        commandIsNotAvailable(shell, "delete screening");
    }

    @Test
    @DirtiesContext
    void anAuthenticatedButNotPrivilegedUserCannotDeleteScreenings() {
        usingShell(shell)
                .afterCommand("create account sanyi asdQWE123")
                .expectOutput();
        usingShell(shell)
                .afterCommand("sign in sanyi asdQWE123")
                .expectOutput();
        commandIsNotAvailable(shell, "delete screening");
    }

    @Test
    @DirtiesContext
    void theAdminCanNotCreateOverlappingScreeningsInTheSameRoom() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Sátántangó", "Pedersoli", "2021-03-15 10:45")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Spirited Away", "Pedersoli", "2021-03-15 10:50")
                .expectOutput("There is an overlapping screening");
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 10:45");
    }

    @Test
    @DirtiesContext
    void theAdminCanCreateOverlappingScreeningsInDifferentRooms() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Sátántangó", "Pedersoli", "2021-03-15 10:45")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Spirited Away", "Girotti", "2021-03-15 10:50")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 10:45",
                        "Spirited Away (animation, 125 minutes), screened in room Girotti, at 2021-03-15 10:50");
    }

    @Test
    @DirtiesContext
    void theAdminCanNotCreateScreeningsDuringA10MinuteLongBreaksAfterAnotherScreeningInTheSameRoom() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Sátántangó", "Pedersoli", "2021-03-15 11:00")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Spirited Away", "Pedersoli", "2021-03-15 18:39")
                .expectOutput("This would start in the break period after another screening in this room");
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 11:00");
    }

    @Test
    @DirtiesContext
    void theAdminCanCreateScreeningsEvenIfThereIsABreakAfterAScreeningInADifferentRoom() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Sátántangó", "Pedersoli", "2021-03-15 11:00")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Spirited Away", "Girotti", "2021-03-15 18:39")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 11:00",
                        "Spirited Away (animation, 125 minutes), screened in room Girotti, at 2021-03-15 18:39");
    }

    @Test
    @DirtiesContext
    void theAdminUserCanDeleteScreenings() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create", "screening", "Spirited Away", "Pedersoli", "2021-03-14 16:00")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("delete", "screening", "Spirited Away", "Pedersoli", "2021-03-14 16:00")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("There are no screenings");
    }

    @Test
    @DirtiesContext
    void deletionOfRoomCausesAssociatedScreeningsToBeDeleted() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Sátántangó","Pedersoli","2021-03-15 10:45")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Spirited Away","Pedersoli","2021-03-14 16:00")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 10:45",
                        "Spirited Away (animation, 125 minutes), screened in room Pedersoli, at 2021-03-14 16:00");
        usingShell(shell)
                .afterCommand("delete room Pedersoli")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("There are no screenings");
    }

    @Test
    @DirtiesContext
    void deletionOfMovieCausesAssociatedScreeningsToBeDeleted() {
        usingShell(shell)
                .afterCommand("sign in privileged admin admin")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Sátántangó","Pedersoli","2021-03-15 10:45")
                .expectOutput();
        usingShell(shell)
                .afterCommandByWords("create","screening","Spirited Away","Pedersoli","2021-03-14 16:00")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Sátántangó (drama, 450 minutes), screened in room Pedersoli, at 2021-03-15 10:45",
                        "Spirited Away (animation, 125 minutes), screened in room Pedersoli, at 2021-03-14 16:00");
        usingShell(shell)
                .afterCommand("delete movie Sátántangó")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("Spirited Away (animation, 125 minutes), screened in room Pedersoli, at 2021-03-14 16:00");
        usingShell(shell)
                .afterCommandByWords("delete", "movie", "Spirited Away")
                .expectOutput();
        usingShell(shell)
                .afterCommand("list screenings")
                .expectOutput("There are no screenings");
    }
}
