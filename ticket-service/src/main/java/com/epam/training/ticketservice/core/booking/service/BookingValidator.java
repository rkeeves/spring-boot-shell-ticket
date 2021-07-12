package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.screening.entity.Screening;

import java.util.List;

public interface BookingValidator {

    void validate(Screening screening, List<Seat> seats)
            throws MultiSeatBookingNotPossibleException;
}
