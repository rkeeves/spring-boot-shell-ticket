package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningService {

    List<String> list();

    void createScreening(String movieTitle, String roomName, LocalDateTime startDateTime)
            throws EntityNotFoundException, ScreeningTimeClashException;

    void deleteScreening(String movieTitle, String roomName, LocalDateTime startDateTime)
            throws EntityNotFoundException;
}
