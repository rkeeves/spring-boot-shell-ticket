package com.epam.training.ticketservice.core.booking.repository;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.booking.entity.BookingId;
import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void givenBookingExists_whenFindAllByScreeningAndIdRowAndIdColumn_thenReturnBooking() {
        // given
        var username = "someUsername";
        var password = "somePassword";
        var account = Account.builder()
                .username(username)
                .password(password)
                .build();
        var startDateTime = LocalDateTime.of(2000,12,1, 1,1);
        var movieTitle = "movieA";
        var movie = Movie.builder()
                .title(movieTitle)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
        movie = testEntityManager.persist(movie);
        var roomName = "roomA";
        var room = Room.builder()
                .name(roomName)
                .rows(100)
                .columns(100)
                .build();
        var screening = Screening.builder()
                .room(room)
                .movie(movie)
                .startDateTime(startDateTime)
                .build();
        screening = testEntityManager.persist(screening);
        var row = 10;
        var column = 15;
        var bookingId = BookingId.builder()
                .screeningId(screening.getId())
                .accountId(account.getId())
                .row(row)
                .column(column)
                .build();
        var booking = Booking.builder()
                .id(bookingId)
                .owner(account)
                .screening(screening)
                .price(1000)
                .build();
        booking = testEntityManager.persist(booking);
        // when
        var existingBookings = bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
        // then
        assertEquals(existingBookings, List.of(booking));
    }

    @Test
    void givenBookingDoesNotExist_whenFindAllByScreeningAndIdRowAndIdColumn_thenReturnEmptyList() {
        // given
        var username = "someUsername";
        var password = "somePassword";
        var startDateTime = LocalDateTime.of(2000,12,1, 1,1);
        var movieTitle = "movieA";
        var movie = Movie.builder()
                .title(movieTitle)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
        movie = testEntityManager.persist(movie);
        var roomName = "roomA";
        var room = Room.builder()
                .name(roomName)
                .rows(100)
                .columns(100)
                .build();
        var screening = Screening.builder()
                .room(room)
                .movie(movie)
                .startDateTime(startDateTime)
                .build();
        screening = testEntityManager.persist(screening);
        var row = 10;
        var column = 15;
        // when
        var existingBookings = bookingRepository.findAllByScreeningAndIdRowAndIdColumn(screening, row, column);
        // then
        assertTrue(existingBookings.isEmpty());
    }
}