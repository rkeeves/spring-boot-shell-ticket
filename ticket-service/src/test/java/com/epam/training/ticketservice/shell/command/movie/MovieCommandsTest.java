package com.epam.training.ticketservice.shell.command.movie;

import com.epam.training.ticketservice.core.movie.service.MovieService;
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
class MovieCommandsTest {

    @Mock
    private SecurityService securityService;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieCommands movieCommands;

    @Test
    void givenMovieDescriptionListIsEmpty_whenListMovies_thenReturnListOfDefaultString() {
        // given
        when(movieService.list())
                .thenReturn(Collections.emptyList());
        // when
        var actual = movieCommands.listMovies();
        // then
        assertEquals(List.of("There are no movies at the moment"), actual);
        verify(movieService, times(1))
                .list();
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenMovieDescriptionListIsNotEmpty_whenListMovies_thenReturnListOfDefaultString() {
        // given
        var movieDescriptionList = List.of("something", "something else");
        when(movieService.list())
                .thenReturn(movieDescriptionList);
        // when
        var actual = movieCommands.listMovies();
        // then
        assertEquals(movieDescriptionList, actual);
        verify(movieService, times(1))
                .list();
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenListMovies_thenReturnListOfGeneralErrorString() {
        // given
        doThrow(RuntimeException.class)
                .when(movieService)
                .list();
        // when
        var actual = movieCommands.listMovies();
        // then
        assertEquals(List.of("Failed due to general error"), actual);
        verify(movieService, times(1))
                .list();
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenMovieDoesNotExistByName_whenCreateMovie_thenReturnEmptyList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        // when
        var actual = movieCommands.createMovie(title, genre, duration);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(movieService, times(1))
                .create(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenMovieDoesExistByName_whenCreateMovie_thenReturnSpecificErrorStringList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var errorMessage = String.format("Movie by title '%s' already exists", title);
        doThrow(EntityExistsException.class)
                .when(movieService)
                .create(title, genre, duration);
        // when
        var actual = movieCommands.createMovie(title, genre, duration);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .create(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenCreateMovie_thenReturnGeneralErrorStringList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var errorMessage = "Failed to create movie due to general error";
        doThrow(RuntimeException.class)
                .when(movieService)
                .create(title, genre, duration);
        // when
        var actual = movieCommands.createMovie(title, genre, duration);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .create(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceSucceeds_whenCreateMovie_thenReturnEmptyList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        // when
        var actual = movieCommands.updateMovie(title, genre, duration);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(movieService, times(1))
                .updateByTitle(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenMovieDoesNotExist_whenUpdateMovie_thenReturnSpecificErrorStringList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var errorMessage = String.format("Movie by title '%s' does not exists", title);
        doThrow(EntityNotFoundException.class)
                .when(movieService)
                .updateByTitle(title, genre, duration);
        // when
        var actual = movieCommands.updateMovie(title, genre, duration);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .updateByTitle(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenUpdateMovie_thenReturnGeneralErrorStringList() {
        // given
        var title = "some title";
        var genre = "some genre";
        var duration = 10;
        var errorMessage = "Failed to update movie due to general error";
        doThrow(RuntimeException.class)
                .when(movieService)
                .updateByTitle(title, genre, duration);
        // when
        var actual = movieCommands.updateMovie(title, genre, duration);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .updateByTitle(title, genre, duration);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceSucceeds_whenDeleteMovie_thenReturnEmptyList() {
        // given
        var title = "some title";
        // when
        var actual = movieCommands.deleteMovie(title);
        // then
        assertEquals(Collections.emptyList(), actual);
        verify(movieService, times(1))
                .deleteByTitle(title);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenMovieDoesNotExist_whenDeleteMovie_thenReturnSpecificErrorStringList() {
        // given
        var title = "some title";
        var errorMessage = String.format("Movie by title '%s' does not exists", title);
        doThrow(EntityNotFoundException.class)
                .when(movieService)
                .deleteByTitle(title);
        // when
        var actual = movieCommands.deleteMovie(title);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .deleteByTitle(title);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }

    @Test
    void givenServiceThrows_whenDeleteMovie_thenReturnGeneralErrorStringList() {
        // given
        var title = "some title";
        var errorMessage = "Failed to delete movie due to general error";
        doThrow(RuntimeException.class)
                .when(movieService)
                .deleteByTitle(title);
        // when
        var actual = movieCommands.deleteMovie(title);
        // then
        assertEquals(List.of(errorMessage), actual);
        verify(movieService, times(1))
                .deleteByTitle(title);
        verifyNoMoreInteractions(movieService);
        verifyNoMoreInteractions(securityService);
    }
}