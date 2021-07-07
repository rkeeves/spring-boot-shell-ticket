package com.epam.training.ticketservice.shell.command;

import com.epam.training.ticketservice.core.security.service.SecurityService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;

@RequiredArgsConstructor
public class SecuredCommand {

    @Getter(AccessLevel.PROTECTED)
    private final SecurityService securityService;

    public Availability isAuthenticated() {
        if (securityService.isAuthenticated()) {
            return Availability.available();
        } else {
            return Availability.unavailable("You must be signed in to use this command");
        }
    }

    public Availability isNotAuthenticated() {
        if (!securityService.isAuthenticated()) {
            return Availability.available();
        } else {
            return Availability.unavailable("You must NOT be signed in to use this command");
        }
    }

    public Availability isPrivileged() {
        if (securityService.isAuthenticated() && securityService.isPrivileged()) {
            return Availability.available();
        } else {
            return Availability.unavailable("You must have privileges to use this command");
        }
    }
}
