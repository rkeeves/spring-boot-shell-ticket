package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> list() {
        return movieRepository.findAll()
                .stream()
                .map(this::describe)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = EntityExistsException.class)
    public void create(String title, String genre, int durationInMinutes) throws EntityExistsException {
        var result = movieRepository.findByTitle(title);
        if (result.isPresent()) {
            throw new EntityExistsException(title);
        }
        var movie = Movie.builder()
                .title(title)
                .genre(genre)
                .durationInMinutes(durationInMinutes)
                .build();
        movieRepository.save(movie);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void updateByTitle(String title, String genre, int durationInMinutes) throws EntityNotFoundException {
        var result = movieRepository.findByTitle(title);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(title);
        }
        var movie = result.get();
        movie.setGenre(genre);
        movie.setDurationInMinutes(durationInMinutes);
        movieRepository.save(movie);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteByTitle(String title) throws EntityNotFoundException {
        var result = movieRepository.findByTitle(title);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(title);
        }
        var movie = result.get();
        movieRepository.delete(movie);
    }

    private String describe(Movie movie) {
        return String.format("%s (%s, %d minutes)", movie.getTitle(), movie.getGenre(), movie.getDurationInMinutes());
    }
}
