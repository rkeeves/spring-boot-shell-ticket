package com.epam.training.ticketservice.core.movie.service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface MovieService {

    List<String> list();

    void create(String title, String genre, int durationInMinutes) throws EntityExistsException;

    void updateByTitle(String title, String genre, int durationInMinutes) throws EntityNotFoundException;

    void deleteByTitle(String title) throws EntityNotFoundException;
}
