package com.epam.training.ticketservice.core.movie.repository;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Transactional(readOnly = true)
    Optional<Movie> findByTitle(String title);
}
