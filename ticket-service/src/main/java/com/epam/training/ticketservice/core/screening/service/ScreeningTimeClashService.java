package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;

import java.time.LocalDateTime;

public interface ScreeningTimeClashService {

    void checkNoClash(int duration, Room room, LocalDateTime startDateTime) throws ScreeningTimeClashException;
}
