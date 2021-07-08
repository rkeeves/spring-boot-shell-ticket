package com.epam.training.ticketservice.shell.command.screening;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@ShellComponent
@ShellCommandGroup("room")
public class ScreeningCommands  extends SecuredCommand {

    private final ScreeningService screeningService;

    public ScreeningCommands(SecurityService securityService,
                             ScreeningService screeningService) {
        super(securityService);
        this.screeningService = screeningService;
    }

    @ShellMethod(
            key = {"list screenings"},
            value = "Lists all screenings")
    public List<String> listScreenings() {
        try {
            var list = screeningService.list();
            return list.isEmpty() ? Collections.singletonList("There are no screenings") : list;
        } catch (Exception e) {
            return List.of("Failed due to general error");
        }
    }

    @ShellMethod(
            key = {"create screening"},
            value = "Creates a screening for the given movie, room and startDateTime")
    @ShellMethodAvailability("isPrivileged")
    public List<String> createScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        try {
            screeningService.createScreening(movieTitle, roomName, startDateTime);
            return Collections.emptyList();
        } catch (ScreeningTimeClashException e) {
            return List.of(e.getMessage());
        } catch (Exception e) {
            return List.of("Failed to screening room due to general error");
        }
    }

    @ShellMethod(
            key = {"delete screening"},
            value = "Deletes a screening by the given movie title, room name and start date time")
    @ShellMethodAvailability("isPrivileged")
    public List<String> deleteScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        try {
            screeningService.deleteScreening(movieTitle, roomName, startDateTime);
            return Collections.emptyList();
        } catch (Exception e) {
            return List.of("Failed to delete screening due to general error");
        }
    }
}
