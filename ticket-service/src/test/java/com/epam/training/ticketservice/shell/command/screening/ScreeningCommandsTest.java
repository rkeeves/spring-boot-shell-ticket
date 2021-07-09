package com.epam.training.ticketservice.shell.command.screening;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningCommandsTest {

    @Mock
    private ScreeningService screeningService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ScreeningCommands screeningCommands;

    @Test
    void givenServiceReturnEmptyList_whenListScreenings_thenReturnDefaultMessage() {
        // given
        when(screeningService.list())
                .thenReturn(Collections.emptyList());
        // when
        var actual = screeningCommands.listScreenings();
        // then
        assertEquals(List.of("There are no screenings"), actual);
        verify(screeningService, times(1))
                .list();
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceReturnNonEmptyList_whenListScreenings_thenReturnList() {
        // given
        var list = List.of("Something");
        when(screeningService.list())
                .thenReturn(list);
        // when
        var actual = screeningCommands.listScreenings();
        // then
        assertEquals(list, actual);
        verify(screeningService, times(1))
                .list();
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceThrows_whenListScreenings_thenReturnListOfGeneralErrorMessage() {
        // given
        var list = List.of("Failed due to general error");
        doThrow(RuntimeException.class)
                .when(screeningService)
                .list();
        // when
        var actual = screeningCommands.listScreenings();
        // then
        assertEquals(list, actual);
        verify(screeningService, times(1))
                .list();
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceDoesNotThrow_whenCreateScreenings_thenReturnEmptyList() {
        // given
        var movie = "movie";
        var room = "room";
        var dateTime = LocalDateTime.now();
        // when
        var actual = screeningCommands.createScreening(movie, room, dateTime);
        // then
        assertTrue(actual.isEmpty());
        verify(screeningService, times(1))
                .createScreening(movie, room, dateTime);
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceThrowsScreeningTimeClashException_whenCreateScreenings_thenReturnListOfExceptionMessage() {
        // given
        var movie = "movie";
        var room = "room";
        var dateTime = LocalDateTime.now();
        var exceptionMessage = "something";
        var expected = List.of(exceptionMessage);
        doThrow(new ScreeningTimeClashException(exceptionMessage))
                .when(screeningService)
                .createScreening(movie, room, dateTime);
        // when
        var actual = screeningCommands.createScreening(movie, room, dateTime);
        // then
        assertEquals(expected, actual);
        verify(screeningService, times(1))
                .createScreening(movie, room, dateTime);
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceThrowsOtherException_whenCreateScreenings_thenReturnListOfGeneralErrorMessage() {
        // given
        var movie = "movie";
        var room = "room";
        var dateTime = LocalDateTime.now();
        var expected = List.of("Failed to create screening due to general error");
        doThrow(RuntimeException.class)
                .when(screeningService)
                .createScreening(movie, room, dateTime);
        // when
        var actual = screeningCommands.createScreening(movie, room, dateTime);
        // then
        assertEquals(expected, actual);
        verify(screeningService, times(1))
                .createScreening(movie, room, dateTime);
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceDoesNotThrow_whenDeleteScreenings_thenReturnEmptyList() {
        // given
        var movie = "movie";
        var room = "room";
        var dateTime = LocalDateTime.now();
        // when
        var actual = screeningCommands.deleteScreening(movie, room, dateTime);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(screeningService, times(1))
                .deleteScreening(movie, room, dateTime);
        verifyNoMoreInteractions(screeningService);
    }

    @Test
    void givenServiceThrowsException_whenDeleteScreenings_thenReturnListOfGeneralErrorMessage() {
        // given
        var movie = "movie";
        var room = "room";
        var dateTime = LocalDateTime.now();
        var expected = List.of("Failed to delete screening due to general error");
        doThrow(RuntimeException.class)
                .when(screeningService)
                .deleteScreening(movie, room, dateTime);
        // when
        var actual = screeningCommands.deleteScreening(movie, room, dateTime);
        // then
        assertEquals(expected, actual);
        verify(screeningService, times(1))
                .deleteScreening(movie, room, dateTime);
        verifyNoMoreInteractions(screeningService);
    }
}