package com.epam.training.ticketservice.core.security.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String username) {
        super(String.format("Account by username '%s' already exists", username));
    }
}
