package com.epam.training.ticketservice.core.screening.repository;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.entity.ScreeningId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScreeningRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Test
    void givenEntityExists_whenFindById_thenReturnEntity() {
        // given
        var movieTitle = "Movie";
        var movieGenre = "genreA";
        var movieDuration = 60;
        var movie = Movie.builder()
                .title(movieTitle)
                .genre(movieGenre)
                .durationInMinutes(movieDuration)
                .build();
        movie = testEntityManager.persist(movie);
        var roomName = "RoomA";
        var roomRows = 10;
        var roomColumns = 20;
        var room = Room.builder()
                .name(roomName)
                .rows(roomRows)
                .columns(roomColumns)
                .build();
        room = testEntityManager.persist(room);
        var startDateTime = LocalDateTime.now();
        var screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .build();
        screening = testEntityManager.persist(screening);
        ScreeningId screeningId = new ScreeningId(movie.getId(), room.getId(), startDateTime);
        // when
        var result = screeningRepository.findById(screeningId);
        // then
        assertTrue(result.isPresent());
        var actual = result.get();
        assertEquals(movie, actual.getMovie());
        assertEquals(room, actual.getRoom());
        assertEquals(startDateTime, actual.getId().getStartDateTime());
    }

    @Test
    void givenEntityDoesNotExist_whenFindById_thenReturnEmpty() {
        // given
        var startDateTime = LocalDateTime.now();
        ScreeningId screeningId = new ScreeningId(1L, 1L, startDateTime);
        // when
        var result = screeningRepository.findById(screeningId);
        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenEntityExists_whenFindByMovieTitleAndRoomNameAndIdStartDateTime_thenReturnEntity() {
        // given
        var movieTitle = "Movie";
        var movieGenre = "genreA";
        var movieDuration = 60;

        var movie = Movie.builder()
                .title(movieTitle)
                .genre(movieGenre)
                .durationInMinutes(movieDuration)
                .build();
        movie = testEntityManager.persist(movie);

        var roomName = "RoomA";
        var roomRows = 10;
        var roomColumns = 20;

        var room = Room.builder()
                .name(roomName)
                .rows(roomRows)
                .columns(roomColumns)
                .build();
        room = testEntityManager.persist(room);

        var startDateTime = LocalDateTime.of(2020,12,04,12,10);

        var screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .build();
        screening = testEntityManager.persist(screening);
        // when
        var result = screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        // then
        assertTrue(result.isPresent());
        var actual = result.get();
        assertEquals(startDateTime, actual.getId().getStartDateTime());
    }

    @Test
    void givenEntityDoesNotExists_whenFindByMovieTitleAndRoomNameAndIdStartDateTime_thenReturnEmpty() {
        // given
        var movieTitle = "Movie";
        var roomName = "RoomA";
        var startDateTime = LocalDateTime.now();
        // when
        var result = screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        // then
        assertFalse(result.isPresent());
    }

    @Test
    void givenEntityExists_whenDeleteByMovieTitleAndRoomNameAndIdStartDateTime_thenReturnOne() {
        // given
        var movieTitle = "Movie";
        var movieGenre = "genreA";
        var movieDuration = 60;
        var movie = Movie.builder()
                .title(movieTitle)
                .genre(movieGenre)
                .durationInMinutes(movieDuration)
                .build();
        movie = testEntityManager.persist(movie);
        var roomName = "RoomA";
        var roomRows = 10;
        var roomColumns = 20;
        var room = Room.builder()
                .name(roomName)
                .rows(roomRows)
                .columns(roomColumns)
                .build();
        room = testEntityManager.persist(room);
        var startDateTime = LocalDateTime.of(2012, 12,12,12,12);
        var screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .build();
        screening = testEntityManager.persist(screening);
        // when
        var modifiedRowCount = screeningRepository.deleteByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        // then
        assertEquals(1, modifiedRowCount);
    }

    @Test
    void givenEntityDoesNotExists_whenDeleteByMovieTitleAndRoomNameAndIdStartDateTime_thenReturnZero() {
        // given
        var movieTitle = "Movie";
        var roomName = "RoomA";
        var startDateTime = LocalDateTime.now();
        // when
        var modifiedRowCount = screeningRepository.deleteByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        // then
        assertEquals(0, modifiedRowCount);
    }

    @Test
    void givenEntitiesExists_whenFindByRoom_thenReturnEntities() {
        // given
        var movieTitle = "Movie";
        var movieGenre = "genreA";
        var movieDuration = 60;

        var movie = Movie.builder()
                .title(movieTitle)
                .genre(movieGenre)
                .durationInMinutes(movieDuration)
                .build();
        movie = testEntityManager.persist(movie);

        var roomName = "RoomA";
        var roomRows = 10;
        var roomColumns = 20;

        var room = Room.builder()
                .name(roomName)
                .rows(roomRows)
                .columns(roomColumns)
                .build();
        room = testEntityManager.persist(room);

        var startDateTime0 = LocalDateTime.of(2020,12,14,12,10);
        var screening0 = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime0)
                .build();
        screening0 = testEntityManager.persist(screening0);
        var startDateTime1 = LocalDateTime.of(2020,11,24,12,10);
        var screening1 = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime1)
                .build();
        screening1 = testEntityManager.persist(screening1);
        // when
        var result = screeningRepository.findAllByRoom(room);
        // then
        assertFalse(result.isEmpty());
        assertEquals(Set.of(screening0, screening1), Set.copyOf(result));
    }

    @Test
    void givenNoEntitiesExists_whenFindByRoom_thenReturnEmptyList() {
        // given
        var movieTitle = "Movie";
        var movieGenre = "genreA";
        var movieDuration = 60;

        var movie = Movie.builder()
                .title(movieTitle)
                .genre(movieGenre)
                .durationInMinutes(movieDuration)
                .build();
         testEntityManager.persist(movie);

        var roomName = "RoomA";
        var roomRows = 10;
        var roomColumns = 20;

        var room = Room.builder()
                .name(roomName)
                .rows(roomRows)
                .columns(roomColumns)
                .build();
        room = testEntityManager.persist(room);
        // when
        var result = screeningRepository.findAllByRoom(room);
        // then
        assertTrue(result.isEmpty());
    }
}