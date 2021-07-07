package com.epam.training.ticketservice.core.security.service;

import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;

public interface SignUpService {

    void signUp(String username, String password) throws AccountAlreadyExistsException;
}
