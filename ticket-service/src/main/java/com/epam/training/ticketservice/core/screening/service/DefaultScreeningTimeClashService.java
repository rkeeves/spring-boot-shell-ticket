package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefaultScreeningTimeClashService implements ScreeningTimeClashService {

    private final ScreeningRepository screeningRepository;

    @Override
    @Transactional(readOnly = true)
    public void checkNoClash(int duration, Room room, LocalDateTime start) throws ScreeningTimeClashException {
        LocalDateTime end = start.plusMinutes(duration);
        var screenings = screeningRepository.findAllByRoom(room);
        for (var screening : screenings) {
            check(start, end, screening);
        }
    }

    public void check(LocalDateTime start,
                      LocalDateTime end,
                      Screening screening) throws ScreeningTimeClashException {
        var screeningId = screening.getId();
        var movieLength = screening.getMovie().getDurationInMinutes();
        var movieStart = screeningId.getStartDateTime();
        var movieEnd = movieStart.plusMinutes(movieLength);
        var breakEnd = movieEnd.plusMinutes(10);
        if (end.compareTo(movieStart) <= 0) {
            // No collision. Finishes before reserved time slot.
            return;
        }
        if (start.compareTo(breakEnd) >= 0) {
            // No collision. Starts after reserved time slot.
            return;
        }
        if (start.compareTo(movieEnd) >= 0) {
            // Collision. The new movie would start in the break after the existing movie.
            throw new ScreeningTimeClashException("This would start in the break period "
                    + "after another screening in this room");
        }
        // Collision. The new movie would directly overlap the existing movie.
        throw new ScreeningTimeClashException("There is an overlapping screening");
    }
}
