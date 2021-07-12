package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.booking.entity.BookingId;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBookingValidatorTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private DefaultBookingValidator bookingValidator;

    @Test
    void givenSeatRowIsLargerThanRoomRowCount_whenValidate_thenThrow() {
        // given
        var row = 11;
        var column = 20;
        var seats = List.of(Seat.of(row,column));
        var movie = createMovie();
        var room = createRoom(10, 20);
        var startDateTime = LocalDateTime.of(2000, 12, 10, 1, 2);
        var screening = createScreening(room, movie, startDateTime);
        when(bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column))
                .thenReturn(Collections.emptyList());
        // when
        assertThrows(MultiSeatBookingNotPossibleException.class, () -> bookingValidator.validate(screening, seats));
        // then
        verify(bookingRepository, times(1))
            .findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
    }

    @Test
    void givenSeatColumnIsLargerThanRoomRowCount_whenValidate_thenThrow() {
        // given
        var row = 10;
        var column = 21;
        var seats = List.of(Seat.of(row,column));
        var movie = createMovie();
        var room = createRoom(10, 20);
        var startDateTime = LocalDateTime.of(2000, 12, 10, 1, 2);
        var screening = createScreening(room, movie, startDateTime);
        when(bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column))
                .thenReturn(Collections.emptyList());
        // when
        assertThrows(MultiSeatBookingNotPossibleException.class, () -> bookingValidator.validate(screening, seats));
        // then
        verify(bookingRepository, times(1))
                .findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
    }

    @Test
    void givenCollisionExists_whenValidate_thenThrow() {
        // given
        var row = 10;
        var column = 20;
        var seats = List.of(Seat.of(row,column));
        var movie = createMovie();
        var room = createRoom(10, 20);
        var startDateTime = LocalDateTime.of(2000, 12, 10, 1, 2);
        var screening = createScreening(room, movie, startDateTime);
        var booking = createBooking(row, column);
        when(bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column))
                .thenReturn(List.of(booking));
        // when
        assertThrows(MultiSeatBookingNotPossibleException.class, () -> bookingValidator.validate(screening, seats));
        // then
        verify(bookingRepository, times(1))
                .findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void givenRequestIsValid_whenValidate_thenReturn() {
        // given
        var row = 10;
        var column = 20;
        var seats = List.of(Seat.of(row,column));
        var movie = createMovie();
        var room = createRoom(10, 20);
        var startDateTime = LocalDateTime.of(2000, 12, 10, 1, 2);
        var screening = createScreening(room, movie, startDateTime);
        when(bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column))
                .thenReturn(Collections.emptyList());
        // when
        bookingValidator.validate(screening, seats);
        // then
        verify(bookingRepository, times(1))
                .findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
        verifyNoMoreInteractions(bookingRepository);
    }


    private Screening createScreening(Room room, Movie movie, LocalDateTime startDateTime) {
        return Screening.builder()
                .room(room)
                .movie(movie)
                .startDateTime(startDateTime)
                .build();
    }

    private Movie createMovie() {
        return Movie.builder()
                .title("movieA")
                .genre("genreA")
                .durationInMinutes(100)
                .build();
    }

    private Room createRoom(int rows, int cols) {
        return Room.builder()
                .name("roomA")
                .rows(rows)
                .columns(cols)
                .build();
    }

    private Booking createBooking(int row, int column) {
        var bookingId = BookingId.builder()
                .row(row)
                .column(column)
                .build();
        var booking = Booking.builder()
                .id(bookingId)
                .price(1500)
                .build();
        return booking;
    }
}