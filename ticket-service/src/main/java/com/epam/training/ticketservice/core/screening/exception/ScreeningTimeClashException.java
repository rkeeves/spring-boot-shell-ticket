package com.epam.training.ticketservice.core.screening.exception;

public class ScreeningTimeClashException extends RuntimeException {

    public ScreeningTimeClashException(String message) {
        super(message);
    }
}
