package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingRequest;
import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.booking.entity.BookingId;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.price.service.PriceService;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBookingServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    ScreeningRepository screeningRepository;

    @Mock
    BookingValidator bookingValidator;

    @Mock
    PriceService priceService;

    @InjectMocks
    DefaultBookingService bookingService;

    @Test
    void givenAccountDoesNotExist_whenBook_thenThrowRuntimeException() {
        // given
        var username = "user";
        var movieTitle = "MovieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,1,1,0);
        var seats = List.of(Seat.of(1,1));
        var request = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> bookingService.book(request));
        // then
        verify(accountRepository, times(1))
                .findByUsername(username);
        verifyNoMoreInteractions(accountRepository);
        verifyNoInteractions(screeningRepository);
        verifyNoInteractions(bookingValidator);
        verifyNoInteractions(priceService);
    }

    @Test
    void givenScreeningDoesNotExist_whenBook_thenThrowRuntimeException() {
        // given
        var username = "user";
        var movieTitle = "MovieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,1,1,0);
        var seats = List.of(Seat.of(1,1));
        var request = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password("pass")
                .privileged(false)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> bookingService.book(request));
        // then
        verify(accountRepository, times(1))
                .findByUsername(username);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoInteractions(bookingValidator);
        verifyNoInteractions(priceService);
    }

    @Test
    void givenValidatorFindsCollisions_whenBook_thenThrowMultiSeatBookingNotPossibleException() {
        // given
        var username = "user";
        var movieTitle = "MovieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,1,1,0);
        var seats = List.of(Seat.of(1,1));
        var request = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password("pass")
                .privileged(false)
                .build();
        var room = Room.builder()
                .name(roomName)
                .rows(10)
                .columns(10)
                .build();
        var movie = Movie.builder()
                .title(movieTitle)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
        var screening = Screening.builder()
                .startDateTime(startDateTime)
                .room(room)
                .movie(movie)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.of(screening));
        var errors = List.of("error 1", "error 2");
        doThrow(new MultiSeatBookingNotPossibleException(errors))
                .when(bookingValidator)
                .validate(screening, seats);
        // when
        var exception = assertThrows(MultiSeatBookingNotPossibleException.class, () -> bookingService.book(request));
        // then
        assertEquals(exception.getErrors(), errors);
        verify(accountRepository, times(1))
                .findByUsername(username);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verify(bookingValidator, times(1))
                .validate(screening, seats);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(bookingValidator);
        verifyNoInteractions(priceService);
    }

    @Test
    void givenScreeningRepositoryThrowsAnyRuntimeException_whenBook_thenThrowSameException() {
        // given
        var username = "user";
        var movieTitle = "MovieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,1,1,0);
        var seats = List.of(Seat.of(1,1));
        var request = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password("pass")
                .privileged(false)
                .build();
        var room = Room.builder()
                .name(roomName)
                .rows(10)
                .columns(10)
                .build();
        var movie = Movie.builder()
                .title(movieTitle)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
        var screening = Screening.builder()
                .startDateTime(startDateTime)
                .room(room)
                .movie(movie)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.of(screening));
        when(priceService.getPerSeatPriceBy(screening))
                .thenReturn(1500);
        doThrow(RuntimeException.class)
                .when(screeningRepository)
                .save(screening);
        // when
         assertThrows(RuntimeException.class, () -> bookingService.book(request));
        // then;
        verify(accountRepository, times(1))
                .findByUsername(username);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verify(bookingValidator, times(1))
                .validate(screening, seats);
        verify(screeningRepository, times(1))
                .save(screening);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(bookingValidator);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenValidRequest_whenBook_thenSaveBookings() {
        // given
        var username = "user";
        var movieTitle = "MovieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,1,1,0);
        var seats = List.of(Seat.of(1,1));
        var request = MultiSeatBookingRequest.builder()
                .username(username)
                .movieTitle(movieTitle)
                .roomName(roomName)
                .startDateTime(startDateTime)
                .seats(seats)
                .build();
        var account = Account.builder()
                .id(1L)
                .username(username)
                .password("pass")
                .privileged(false)
                .build();
        var room = Room.builder()
                .id(2L)
                .name(roomName)
                .rows(10)
                .columns(10)
                .build();
        var movie = Movie.builder()
                .id(3L)
                .title(movieTitle)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
        var screening = Screening.builder()
                .startDateTime(startDateTime)
                .room(room)
                .movie(movie)
                .build();
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.of(account));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.of(screening));
        when(priceService.getPerSeatPriceBy(screening))
                .thenReturn(1500);
        var bookingId = BookingId.builder()
                .screeningId(screening.getId())
                .accountId(account.getId())
                .row(1)
                .column(1)
                .build();
        var booking = Booking.builder()
                .id(bookingId)
                .owner(account)
                .screening(screening)
                .price(1500)
                .build();
        var screeningWithNewBookings = Screening.builder()
                .startDateTime(startDateTime)
                .room(room)
                .movie(movie)
                .build();
        screeningWithNewBookings.getBookings().add(booking);
        when(screeningRepository.save(screening))
            .thenReturn(screeningWithNewBookings);
        // when
        bookingService.book(request);
        // then;
        verify(accountRepository, times(1))
                .findByUsername(username);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verify(bookingValidator, times(1))
                .validate(screening, seats);
        verify(screeningRepository, times(1))
                .save(screening);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(bookingValidator);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenAccountByUsernameDoesNotExist_whenListBookingsByAccount_thenThrow() {
        // given
        var username = "user";
        when(accountRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> bookingService.listBookingsByAccount(username));
        // then
    }
}