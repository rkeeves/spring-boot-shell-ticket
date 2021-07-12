package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
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
class DefaultMovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private DefaultMovieService movieService;

    @Test
    void givenRepositoryThrows_whenList_thenThrow(){
        // given
        doThrow(RuntimeException.class)
                .when(movieRepository)
                .findAll();
        // when
        assertThrows(RuntimeException.class, () -> movieService.list());
        // then
        verify(movieRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenNoMovieExists_whenList_thenReturnEmptyList(){
        // given
        when(movieRepository.findAll())
                .thenReturn(Collections.emptyList());
        // when
        var actual = movieService.list();
        // then
        assertTrue(actual.isEmpty());
        verify(movieRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMoviesExists_whenList_thenReturnListOfMovieDescriptions(){
        // given
        var titleA = "Title A";
        var genreA = "genreA";
        var durationA = 10;
        var movieA = Movie.builder()
                .id(1L)
                .title(titleA)
                .genre(genreA)
                .durationInMinutes(durationA)
                .build();
        var titleB = "Title B";
        var genreB = "genreB";
        var durationB = 100;
        var movieB = Movie.builder()
                .id(2L)
                .title(titleB)
                .genre(genreB)
                .durationInMinutes(durationB)
                .build();
        var existingMovies = List.of(movieA, movieB);
        when(movieRepository.findAll())
                .thenReturn(existingMovies);
        // when
        var actual = movieService.list();
        // then
        assertEquals(List.of("Title A (genreA, 10 minutes)", "Title B (genreB, 100 minutes)"),
                actual);
        verify(movieRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieByTitleDoesNotExist_whenCreate_thenSaveNewEntity(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var moviePreSave = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        var moviePostSave = Movie.builder()
                .id(1L)
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.empty());
        when(movieRepository.save(moviePreSave))
                .thenReturn(moviePostSave);
        // when
        movieService.create(title, genre, duration);
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .save(moviePreSave);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieByTitleDoesExist_whenCreate_thenThrowEntityExistsException(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var existingMovie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(existingMovie));
        // when
        assertThrows(EntityExistsException.class, () -> movieService.create(title, genre, duration));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(0))
                .save(any(Movie.class));
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenRepositoryThrows_whenCreate_thenThrowException(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var moviePreSave = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.empty());
        doThrow(RuntimeException.class)
                .when(movieRepository)
                .save(moviePreSave);
        // when
        assertThrows(RuntimeException.class, () -> movieService.create(title, genre, duration));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .save(moviePreSave);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenValidChange_whenUpdate_thenPerformSaveOfModifiedEntity(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var newGenre = "some genre";
        var newDuration = 10;
        var movieBeforeUpdate = Movie.builder()
                .id(1L)
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        var movieAfterUpdate = Movie.builder()
                .id(1L)
                .title(title)
                .genre(newGenre)
                .durationInMinutes(newDuration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieBeforeUpdate));
       when(movieRepository.save(movieAfterUpdate))
               .thenReturn(movieAfterUpdate);
        // when
        movieService.updateByTitle(title, newGenre, newDuration);
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .save(movieAfterUpdate);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieByTitleDoesNotExist_whenUpdate_thenThrowEntityNotFoundException(){
        // given
        var title = "some title";
        var newGenre = "some genre";
        var newDuration = 10;
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> movieService.updateByTitle(title, newGenre, newDuration));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenRepositoryThrows_whenUpdate_thenThrowException(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var newGenre = "some genre";
        var newDuration = 10;
        var movieBeforeUpdate = Movie.builder()
                .id(1L)
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        var movieAfterUpdate = Movie.builder()
                .id(1L)
                .title(title)
                .genre(newGenre)
                .durationInMinutes(newDuration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieBeforeUpdate));
        doThrow(RuntimeException.class)
                .when(movieRepository)
                .save(movieAfterUpdate);
        // when
        assertThrows(RuntimeException.class, () -> movieService.updateByTitle(title, newGenre, newDuration));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .save(movieAfterUpdate);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieExistsByTitle_whenDelete_thenPerformDeletionOfEntity(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var movie = Movie.builder()
                .id(1L)
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movie));
        // when
        movieService.deleteByTitle(title);
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .delete(movie);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieByTitleDoesNotExist_whenDelete_thenThrowEntityNotFoundException(){
        // given
        var title = "some title";
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> movieService.deleteByTitle(title));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenRepositoryThrows_whenDelete_thenThrowException(){
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var movie = Movie.builder()
                .id(1L)
                .title(title)
                .genre(genre)
                .durationInMinutes(duration)
                .build();
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movie));
        doThrow(RuntimeException.class)
                .when(movieRepository)
                .delete(movie);
        // when
        assertThrows(RuntimeException.class, () -> movieService.deleteByTitle(title));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(movieRepository, times(1))
                .delete(movie);
        verifyNoMoreInteractions(movieRepository);
    }
}