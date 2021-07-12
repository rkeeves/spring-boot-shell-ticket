package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultBookingValidator implements BookingValidator {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional(rollbackFor = { MultiSeatBookingNotPossibleException.class })
    public void validate(Screening screening, List<Seat> seats) throws MultiSeatBookingNotPossibleException {
        var rows = screening.getRoom().getRows();
        var columns = screening.getRoom().getColumns();
        var errors = new ArrayList<String>();
        for (var seat : seats) {
            var row = seat.getRow();
            var col = seat.getColumn();
            if (row > rows || col > columns) {
                errors.add(String.format("Seat (%d,%d) is invalid, room has %d rows and %d columns",
                        row, col, rows, columns));
            }
            var existingBookings = bookingRepository
                    .findAllByScreeningAndIdRowAndIdColumn(screening, row, col);
            if (!existingBookings.isEmpty()) {
                errors.add(String.format("Seat (%d,%d) is already taken", row, col));
            }
        }
        if (errors.isEmpty()) {
            return;
        }
        throw new MultiSeatBookingNotPossibleException(errors);
    }
}
