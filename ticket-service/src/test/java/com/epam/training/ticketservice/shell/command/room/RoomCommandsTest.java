package com.epam.training.ticketservice.shell.command.room;

import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomCommandsTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomCommands roomCommands;

    @Test
    void givenRoomDescriptionListIsEmpty_whenListRooms_thenReturnListOfDefaultString() {
        // given
        when(roomService.list())
                .thenReturn(Collections.emptyList());
        // when
        var actual = roomCommands.listRooms();
        // then
        assertEquals(List.of("There are no rooms at the moment"), actual);
        verify(roomService, times(1))
                .list();
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenRoomDescriptionListIsNotEmpty_whenListRooms_thenReturnListOfDefaultString() {
        // given
        var roomDescriptionList = List.of("something", "something else");
        when(roomService.list())
                .thenReturn(roomDescriptionList);
        // when
        var actual = roomCommands.listRooms();
        // then
        assertEquals(roomDescriptionList, actual);
        verify(roomService, times(1))
                .list();
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenListRooms_thenReturnListOfGeneralErrorString() {
        // given
        doThrow(RuntimeException.class)
                .when(roomService)
                .list();
        // when
        var actual = roomCommands.listRooms();
        // then
        assertEquals(List.of("Failed due to general error"), actual);
        verify(roomService, times(1))
                .list();
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenRoomDoesNotExistByName_whenCreateRoom_thenReturnEmptyList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        // when
        var actual = roomCommands.createRoom(name, rows, columns);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(roomService, times(1))
                .create(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenRoomDoesExistByName_whenCreateRoom_thenReturnSpecificErrorStringList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        var errorMessage = String.format("Room by name '%s' already exists", name);
        doThrow(EntityExistsException.class)
                .when(roomService)
                .create(name, rows, columns);
        // when
        var actual = roomCommands.createRoom(name, rows, columns);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .create(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenCreateRoom_thenReturnGeneralErrorStringList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        var errorMessage = "Failed to create room due to general error";
        doThrow(RuntimeException.class)
                .when(roomService)
                .create(name, rows, columns);
        // when
        var actual = roomCommands.createRoom(name, rows, columns);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .create(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceSucceeds_whenUpdateRoom_thenReturnEmptyList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        // when
        var actual = roomCommands.updateRoom(name, rows, columns);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(roomService, times(1))
                .updateByName(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenRoomDoesNotExist_whenUpdateRoom_thenReturnSpecificErrorStringList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        var errorMessage = String.format("Room by name '%s' does not exists", name);
        doThrow(EntityNotFoundException.class)
                .when(roomService)
                .updateByName(name, rows, columns);
        // when
        var actual = roomCommands.updateRoom(name, rows, columns);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .updateByName(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenUpdateRoom_thenReturnGeneralErrorStringList() {
        // given
        var name = "some name";
        var rows = 20;
        var columns = 10;
        var errorMessage = "Failed to update room due to general error";
        doThrow(RuntimeException.class)
                .when(roomService)
                .updateByName(name, rows, columns);
        // when
        var actual = roomCommands.updateRoom(name, rows, columns);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .updateByName(name, rows, columns);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceSucceeds_whenDeleteRoom_thenReturnEmptyList() {
        // given
        var name = "some name";
        // when
        var actual = roomCommands.deleteRoom(name);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(roomService, times(1))
                .deleteByName(name);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenRoomDoesNotExist_whenDeleteRoom_thenReturnSpecificErrorStringList() {
        // given
        var name = "some name";
        var errorMessage = String.format("Room by name '%s' does not exists", name);
        doThrow(EntityNotFoundException.class)
                .when(roomService)
                .deleteByName(name);
        // when
        var actual = roomCommands.deleteRoom(name);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .deleteByName(name);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenDeleteRoom_thenReturnGeneralErrorStringList() {
        // given
        var name = "some name";
        var errorMessage = "Failed to delete room due to general error";
        doThrow(RuntimeException.class)
                .when(roomService)
                .deleteByName(name);
        // when
        var actual = roomCommands.deleteRoom(name);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(roomService, times(1))
                .deleteByName(name);
        verifyNoMoreInteractions(roomService);
        verifyNoMoreInteractions(securityService);
    }
}