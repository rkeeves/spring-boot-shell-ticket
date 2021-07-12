package com.epam.training.ticketservice.core.booking.exception;

import lombok.Data;

import java.util.List;

@Data
public class MultiSeatBookingNotPossibleException extends RuntimeException {

    private List<String> errors;

    public MultiSeatBookingNotPossibleException(List<String> errors) {
        super();
        this.errors = errors;
    }
}
