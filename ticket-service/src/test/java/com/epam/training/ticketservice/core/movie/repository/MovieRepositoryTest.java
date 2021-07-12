package com.epam.training.ticketservice.core.movie.repository;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void givenValidEntity_whenSave_thenEntityIsSaved() {
        // given
        var title = "some title";
        var genre = "some genre";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        var savedMovie = movieRepository.save(movie);
        // then
        assertNotNull(savedMovie);
        assertEquals(title, savedMovie.getTitle());
        assertEquals(genre, savedMovie.getGenre());
        assertEquals(durationInMinutes, savedMovie.getDurationInMinutes());
    }

    @Test
    void givenTitleIsNull_whenSave_thenThrows() {
        // given
        var genre = "some genre";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenTitleIsEmpty_whenSave_thenThrows() {
        // given
        var genre = "some genre";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title("")
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenGenreIsNull_whenSave_thenThrows() {
        // given
        var title = "some title";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title(title)
                .genre(null)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenGenreIsEmpty_whenSave_thenThrows() {
        // given
        var title = "some title";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title(title)
                .genre("")
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenDurationInMinutesIsNull_whenSave_thenThrows() {
        // given
        var title = "some title";
        var genre = "some genre";
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(null)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenDurationInMinutesIsZero_whenSave_thenThrows() {
        // given
        var title = "some title";
        var genre = "some genre";
        var durationInMinutes = 0;
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenDurationInMinutesIsNegative_whenSave_thenThrows() {
        // given
        var title = "some title";
        var genre = "some genre";
        var durationInMinutes = -1;
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        // when
        movieRepository.save(movie);
        // then
        assertThrows(ConstraintViolationException.class,
                () -> testEntityManager.flush());
    }

    @Test
    void givenEntityByTitleExists_whenFindByTitle_thenProjectionIsReturned() {
        // given
        var title = "some title";
        var genre = "some genre";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        movieRepository.save(movie);
        // when
        var movieViewProjectionResult = movieRepository.findByTitle(title);
        // then
        assertTrue(movieViewProjectionResult.isPresent());
        var movieViewProjection = movieViewProjectionResult.get();
        assertEquals(title, movieViewProjection.getTitle());
        assertEquals(genre, movieViewProjection.getGenre());
        assertEquals(durationInMinutes, movieViewProjection.getDurationInMinutes());
    }

    @Test
    void givenEntityByTitleDoesNotExists_whenFindByTitle_thenEmptyIsReturned() {
        // given
        var otherTitle = "some other title";
        var title = "some title";
        var genre = "some genre";
        var durationInMinutes = 10;
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        movieRepository.save(movie);
        // when
        var movieViewProjectionResult = movieRepository.findByTitle(otherTitle);
        // then
        assertTrue(movieViewProjectionResult.isEmpty());
    }
}