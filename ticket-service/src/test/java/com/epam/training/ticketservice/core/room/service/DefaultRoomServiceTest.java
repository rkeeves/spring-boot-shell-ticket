package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DefaultRoomService roomService;

    @Test
    void givenRoomsDontExist_whenList_thenReturnEmptyList() {
        // given
        var rooms = Collections.<Room>emptyList();
        when(roomRepository.findAll())
                .thenReturn(rooms);
        // when
        var actual = roomService.list();
        // then
        assertTrue(actual.isEmpty());
        verify(roomRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomsExist_whenList_thenListOfRoomDescriptions() {
        // given
        var nameA = "RoomA";
        var rowsA = 10;
        var colsA = 20;
        var roomA = Room.builder()
                .id(1L)
                .name(nameA)
                .rows(rowsA)
                .columns(colsA)
                .build();
        var nameB = "RoomB";
        var rowsB = 100;
        var colsB = 200;
        var roomB = Room.builder()
                .id(2L)
                .name(nameB)
                .rows(rowsB)
                .columns(colsB)
                .build();
        var rooms = List.of(roomA, roomB);
        var expected = List.of(
                "Room RoomA with 200 seats, 10 rows and 20 columns",
                "Room RoomB with 20000 seats, 100 rows and 200 columns");
        when(roomRepository.findAll())
                .thenReturn(rooms);
        // when
        var actual = roomService.list();
        // then
        assertEquals(expected, actual);
        verify(roomRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomDoesNotExistByName_whenCreate_thenThrowEntityExistsException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var room = Room.builder()
                .id(null)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        var roomPostSave = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.empty());
        when(roomRepository.save(room))
                .thenReturn(roomPostSave);
        // when
        roomService.create(name, rows, cols);
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .save(room);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomExistsByName_whenCreate_thenThrowEntityExistsException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var room = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.of(room));
        // when
        assertThrows(EntityExistsException.class, () -> roomService.create(name, rows, cols));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrows_whenCreate_thenThrowException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var room = Room.builder()
                .id(null)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.empty());
        doThrow(RuntimeException.class)
                .when(roomRepository)
                .save(room);
        // when
        assertThrows(RuntimeException.class, () -> roomService.create(name, rows, cols));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .save(room);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomDoesNotExist_whenUpdate_thenUpdateEntity() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var newRows = 100;
        var newCols = 200;
        var room = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        var updatedRoom = Room.builder()
                .id(1L)
                .name(name)
                .rows(newRows)
                .columns(newCols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.of(room));
        when(roomRepository.save(updatedRoom))
                .thenReturn(updatedRoom);
        // when
        roomService.updateByName(name, newRows, newCols);
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .save(room);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomDoesNotExistByName_whenUpdate_thenThrowEntityNotFoundException() {
        // given
        var name = "Some name";
        var newRows = 100;
        var newCols = 200;
        when(roomRepository.findByName(name))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> roomService.updateByName(name, newRows, newCols));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrows_whenUpdate_thenThrowException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var newRows = 100;
        var newCols = 200;
        var room = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.of(room));
        doThrow(RuntimeException.class)
                .when(roomRepository)
                .save(room);
        // when
        assertThrows(RuntimeException.class, () -> roomService.updateByName(name, newRows, newCols));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .save(room);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenEntityExists_whenDelete_thenDeleteEntity() {
        // given
        var name = "Some name";
        when(roomRepository.findByName(name))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> roomService.deleteByName(name));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenEntityDoesNotExist_whenDelete_thenThrowEntityNotFoundException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var room = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.of(room));
        // when
        roomService.deleteByName(name);
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .delete(room);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrows_whenDelete_thenThrowException() {
        // given
        var name = "Some name";
        var rows = 10;
        var cols = 20;
        var room = Room.builder()
                .id(1L)
                .name(name)
                .rows(rows)
                .columns(cols)
                .build();
        when(roomRepository.findByName(name))
                .thenReturn(Optional.of(room));
        doThrow(RuntimeException.class)
                .when(roomRepository)
                .delete(room);
        // when
        assertThrows(RuntimeException.class, () -> roomService.deleteByName(name));
        // then
        verify(roomRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .delete(room);
        verifyNoMoreInteractions(roomRepository);
    }
}