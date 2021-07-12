package com.epam.training.ticketservice.shell.command.booking;

import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingRequest;
import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingResponse;
import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingCommandsTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingCommands bookingCommands;

    @Test
    void givenUsernameDoesNotExist_whenBook_thenThrowIllegalStateException() {
        // given
        var movieTitle = "movie";
        var roomName = "room";
        var startDateTime = LocalDateTime.of(2000,1,1, 1, 1);
        var seat1 = Seat.builder().row(10).column(10).build();
        var seat2 = Seat.builder().row(20).column(20).build();
        var seats = List.of(seat1, seat2);
        when(securityService.username())
                .thenReturn(Optional.empty());
        // when
        assertThrows(IllegalStateException.class, () -> bookingCommands.book(movieTitle, roomName, startDateTime, seats));
        // then
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void givenServiceReturnsNoErrors_whenBook_thenReturnEmptyList() {
        // given
        var username = "user";
        var movieTitle = "movie";
        var roomName = "room";
        var startDateTime = LocalDateTime.of(2000,1,1, 1, 1);
        var seat1 = Seat.builder().row(10).column(10).build();
        var seat2 = Seat.builder().row(20).column(20).build();
        var seats = List.of(seat1, seat2);
        when(securityService.username())
                .thenReturn(Optional.of(username));
        var bookingRequest = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var bookingResponse = MultiSeatBookingResponse.builder()
                .sumPrice(3000)
                .seats(seats)
                .build();
        when(bookingService.book(bookingRequest))
                .thenReturn(bookingResponse);
        // when
        var output = bookingCommands.book(movieTitle, roomName, startDateTime, seats);
        // then
        assertEquals(List.of("Seats booked: (10,10), (20,20); the price for this booking is 3000 HUF"), output);
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verify(bookingService, times(1))
                .book(bookingRequest);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void givenServiceThrowsMultiSeatBookingNotPossibleException_whenBook_thenReturnErrors() {
        // given
        var username = "user";
        var movieTitle = "movie";
        var roomName = "room";
        var startDateTime = LocalDateTime.of(2000,1,1, 1, 1);
        var seat1 = Seat.builder().row(10).column(10).build();
        var seat2 = Seat.builder().row(20).column(20).build();
        var seats = List.of(seat1, seat2);
        when(securityService.username())
                .thenReturn(Optional.of(username));
        var bookingRequest = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var errors = List.of("error1", "error2");
        doThrow(new MultiSeatBookingNotPossibleException(errors))
                .when(bookingService)
                .book(bookingRequest);
        // when
        var output = bookingCommands.book(movieTitle, roomName, startDateTime, seats);
        // then
        assertEquals(errors, output);
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verify(bookingService, times(1))
                .book(bookingRequest);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void givenServiceThrows_whenBook_thenReturnGeneralErrorMessage() {
        // given
        var username = "user";
        var movieTitle = "movie";
        var roomName = "room";
        var startDateTime = LocalDateTime.of(2000,1,1, 1, 1);
        var seat1 = Seat.builder().row(10).column(10).build();
        var seat2 = Seat.builder().row(20).column(20).build();
        var seats = List.of(seat1, seat2);
        when(securityService.username())
                .thenReturn(Optional.of(username));
        var bookingRequest = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        doThrow(RuntimeException.class)
                .when(bookingService)
                .book(bookingRequest);
        // when
        var output = bookingCommands.book(movieTitle, roomName, startDateTime, seats);
        // then
        assertEquals(List.of("Booking seats failed due to general error"), output);
        verify(securityService, times(1))
                .username();
        verifyNoMoreInteractions(securityService);
        verify(bookingService, times(1))
                .book(bookingRequest);
        verifyNoMoreInteractions(bookingService);
    }
}