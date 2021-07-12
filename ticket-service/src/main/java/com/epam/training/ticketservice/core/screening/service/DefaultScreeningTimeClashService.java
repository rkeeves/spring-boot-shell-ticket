package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefaultScreeningTimeClashService implements ScreeningTimeClashService {

    public void check(LocalDateTime start,
                      LocalDateTime end,
                      LocalDateTime movieStart,
                      int existingDuration) throws ScreeningTimeClashException {
        var movieEnd = movieStart.plusMinutes(existingDuration);
        var timeSlotStart = movieStart.minusMinutes(10);
        var timeSlotEnd = movieEnd.plusMinutes(10);
        if (end.compareTo(timeSlotStart) <= 0) {
            // No collision. Finishes before reserved time slot.
            return;
        }
        if (start.compareTo(timeSlotEnd) >= 0) {
            // No collision. Starts after reserved time slot.
            return;
        }
        if (end.compareTo(movieStart) <= 0) {
            // Collision. The existing movie would start in the in the break after the new movie.
            throw new ScreeningTimeClashException("The existing movie start in the break period "
                    + "after the new screening in this room");
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
