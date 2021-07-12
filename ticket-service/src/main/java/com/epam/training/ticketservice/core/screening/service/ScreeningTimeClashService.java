package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;

import java.time.LocalDateTime;

public interface ScreeningTimeClashService {

    void check(LocalDateTime start,
          LocalDateTime end,
          LocalDateTime existingStart,
          int existingDuration) throws ScreeningTimeClashException;
}
