package com.epam.training.ticketservice.shell.command;

import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@ShellComponent
@ShellCommandGroup("movie")
public class MovieCommands extends SecuredCommand {

    private final MovieService movieService;

    public MovieCommands(SecurityService securityService,
                           MovieService movieService) {
        super(securityService);
        this.movieService = movieService;
    }

    @ShellMethod(
            key = {"list movies"},
            value = "Lists all movies")
    public List<String> listMovies() {
        try {
            var list = movieService.list();
            return list.isEmpty() ? Collections.singletonList("There are no movies at the moment") : list;
        } catch (Exception e) {
            return List.of("Failed due to general error");
        }
    }

    @ShellMethod(
            key = {"create movie"},
            value = "Creates a movie by the given title, genre and duration (in minutes)")
    public List<String> createMovie(String title, String genre, int durationInMinutes) {
        try {
            movieService.create(title, genre, durationInMinutes);
            return Collections.emptyList();
        } catch (EntityExistsException e) {
            return List.of(String.format("Movie by title '%s' already exists", title));
        } catch (Exception e) {
            return List.of("Failed to create movie due to general error");
        }
    }

    @ShellMethod(
            key = {"update movie"},
            value = "Updates a movie by the given title, genre and duration (in minutes)")
    public List<String> updateMovie(String title, String genre, int durationInMinutes) {
        try {
            movieService.updateByTitle(title, genre, durationInMinutes);
            return Collections.emptyList();
        } catch (EntityNotFoundException e) {
            return List.of(String.format("Movie by title '%s' does not exists", title));
        } catch (Exception e) {
            return List.of("Failed to update movie due to general error");
        }
    }

    @ShellMethod(
            key = {"delete movie"},
            value = "Deletes a movie by the given title")
    public List<String> deleteMovie(String title) {
        try {
            movieService.deleteByTitle(title);
            return Collections.emptyList();
        } catch (EntityNotFoundException e) {
            return List.of(String.format("Movie by title '%s' does not exists", title));
        } catch (Exception e) {
            return List.of("Failed to delete movie due to general error");
        }
    }
}
