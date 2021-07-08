package com.epam.training.ticketservice.shell.command.room;

import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@ShellComponent
@ShellCommandGroup("room")
public class RoomCommands  extends SecuredCommand {

    private final RoomService roomService;

    public RoomCommands(SecurityService securityService,
                        RoomService roomService) {
        super(securityService);
        this.roomService = roomService;
    }

    @ShellMethod(
            key = {"list rooms"},
            value = "Lists all rooms")
    public List<String> listRooms() {
        try {
            var list = roomService.list();
            return list.isEmpty() ? Collections.singletonList("There are no rooms at the moment") : list;
        } catch (Exception e) {
            return List.of("Failed due to general error");
        }
    }

    @ShellMethod(
            key = {"create room"},
            value = "Creates a room by the given name, seating row and column count")
    @ShellMethodAvailability("isPrivileged")
    public List<String> createRoom(String name, int rows, int columns) {
        try {
            roomService.create(name, rows, columns);
            return Collections.emptyList();
        } catch (EntityExistsException e) {
            return List.of(String.format("Room by name '%s' already exists", name));
        } catch (Exception e) {
            return List.of("Failed to create room due to general error");
        }
    }

    @ShellMethod(
            key = {"update room"},
            value = "Updates a room by the given name, with new seating row and column count")
    @ShellMethodAvailability("isPrivileged")
    public List<String> updateRoom(String name, int rows, int columns) {
        try {
            roomService.updateByName(name, rows, columns);
            return Collections.emptyList();
        } catch (EntityNotFoundException e) {
            return List.of(String.format("Room by name '%s' does not exists", name));
        } catch (Exception e) {
            return List.of("Failed to update room due to general error");
        }
    }

    @ShellMethod(
            key = {"delete room"},
            value = "Deletes a room by the given name")
    @ShellMethodAvailability("isPrivileged")
    public List<String> deleteRoom(String name) {
        try {
            roomService.deleteByName(name);
            return Collections.emptyList();
        } catch (EntityNotFoundException e) {
            return List.of(String.format("Room by name '%s' does not exists", name));
        } catch (Exception e) {
            return List.of("Failed to delete room due to general error");
        }
    }
}