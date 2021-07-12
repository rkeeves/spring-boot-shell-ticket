package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingRequest;
import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingResponse;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;

import java.util.List;

public interface BookingService {

    List<String> listBookingsByAccount(String username);

    MultiSeatBookingResponse book(MultiSeatBookingRequest multiSeatBookingRequest)
            throws MultiSeatBookingNotPossibleException;
}
