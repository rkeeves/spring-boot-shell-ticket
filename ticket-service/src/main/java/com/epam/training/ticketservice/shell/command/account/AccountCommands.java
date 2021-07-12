package com.epam.training.ticketservice.shell.command.account;

import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.core.security.service.SignInSignOutService;
import com.epam.training.ticketservice.core.security.service.SignUpService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ShellComponent
@ShellCommandGroup("account")
public class AccountCommands extends SecuredCommand {

    private final SignInSignOutService signInSignOutService;

    private final SignUpService signUpService;

    private final BookingService bookingService;

    public AccountCommands(SecurityService securityService,
                           SignInSignOutService signInSignOutService,
                           SignUpService signUpService,
                           BookingService bookingService) {
        super(securityService);
        this.signInSignOutService = signInSignOutService;
        this.signUpService = signUpService;
        this.bookingService = bookingService;
    }

    @ShellMethod(
            key = {"sign in", "sign in privileged"},
            value = "Signs in with the supplied username and password if the user exists")
    @ShellMethodAvailability("isNotAuthenticated")
    public List<String> signIn(String username, String password) {
        try {
            signInSignOutService.signIn(username, password);
            return Collections.emptyList();
        } catch (AuthenticationException e) {
            return List.of("Login failed due to incorrect credentials");
        } catch (Exception e) {
            return List.of("Login failed due to general error");
        }
    }

    @ShellMethod(
            key = {"sign out"},
            value = "Signs out the current user if it exists")
    @ShellMethodAvailability("isAuthenticated")
    public List<String> signOut() {
        signInSignOutService.signOut();
        return Collections.emptyList();
    }

    // create account had to be added because there's a typo in acceptance test movies.feature
    // they probably meant sign up but who knows
    @ShellMethod(
            key = {"sign up", "create account"},
            value = "Signs up with the given username and password a new user")
    public List<String> signUp(String username, String password) {
        try {
            signUpService.signUp(username, password);
            return Collections.emptyList();
        } catch (AccountAlreadyExistsException e) {
            return List.of("Sign up failed, user by this username already exists");
        } catch (Exception e) {
            return List.of("Sign up failed due to general error");
        }
    }

    @ShellMethod(
            key = {"describe account"},
            value = "Describes the currently signed in user")
    public List<String> describeAccount() {
        var usernameOptional = getSecurityService().username();
        if (usernameOptional.isEmpty()) {
            return List.of("You are not signed in");
        }
        var lines = new ArrayList<String>();
        var username = usernameOptional.get();
        if (getSecurityService().isPrivileged()) {
            lines.add(String.format("Signed in with privileged account '%s'", username));
        } else {
            lines.add(String.format("Signed in with account '%s'", username));
        }
        var bookings = bookingService.listBookingsByAccount(username);
        if (bookings.isEmpty()) {
            lines.add("You have not booked any tickets yet");
            return lines;
        }
        lines.add("Your previous bookings are");
        lines.addAll(bookings);
        return lines;
    }
}
