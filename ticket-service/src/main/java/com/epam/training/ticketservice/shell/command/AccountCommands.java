package com.epam.training.ticketservice.shell.command;

import com.epam.training.ticketservice.core.security.exception.AccountAlreadyExistsException;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.core.security.service.SignInSignOutService;
import com.epam.training.ticketservice.core.security.service.SignUpService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
@ShellCommandGroup("account")
public class AccountCommands extends SecuredCommand {

    private final SignInSignOutService signInSignOutService;

    private final SignUpService signUpService;

    public AccountCommands(SecurityService securityService,
                           SignInSignOutService signInSignOutService,
                           SignUpService signUpService) {
        super(securityService);
        this.signInSignOutService = signInSignOutService;
        this.signUpService = signUpService;
    }

    @ShellMethod(
            key = {"sign in", "sign in privileged"},
            value = "Signs in with the supplied username and password if the user exists")
    @ShellMethodAvailability("isNotAuthenticated")
    public Optional<String> signIn(String username, String password) {
        try {
            signInSignOutService.signIn(username, password);
            return Optional.empty();
        } catch (AuthenticationException e) {
            return Optional.of("Login failed due to incorrect credentials");
        } catch (Exception e) {
            return Optional.of("Login failed due to general error");
        }
    }

    @ShellMethod(
            key = {"sign out"},
            value = "Signs out the current user if it exists")
    @ShellMethodAvailability("isAuthenticated")
    public void signOut() {
        signInSignOutService.signOut();
    }

    @ShellMethod(
            key = {"sign up"},
            value = "Signs up with the given username and password a new user")
    public Optional<String> signUp(String username, String password) {
        try {
            signUpService.signUp(username, password);
            return Optional.empty();
        } catch (AccountAlreadyExistsException e) {
            return Optional.of("Sign up failed, user by this username already exists");
        } catch (Exception e) {
            return Optional.of("Sign up failed due to general error");
        }
    }

    @ShellMethod(
            key = {"describe account"},
            value = "Describes the currently signed in user")
    public List<String> describeAccount() {
        var usernameOptional = getSecurityService().username();
        if (usernameOptional.isEmpty() || !getSecurityService().isAuthenticated()) {
            return List.of("You are not signed in");
        }
        var lines = new ArrayList<String>();
        var username = usernameOptional.get();
        if (getSecurityService().isPrivileged()) {
            lines.add(String.format("Signed in with privileged account '%s'", username));
        } else {
            lines.add(String.format("Signed in with account '%s'", username));
        }
        lines.add("You have not booked any tickets yet");
        return lines;
    }
}
