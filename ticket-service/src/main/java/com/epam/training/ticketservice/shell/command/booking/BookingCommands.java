package com.epam.training.ticketservice.shell.command.booking;

import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingRequest;
import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("account")
public class BookingCommands extends SecuredCommand {

    private final BookingService bookingService;

    public BookingCommands(SecurityService securityService,
                           BookingService bookingService) {
        super(securityService);
        this.bookingService = bookingService;
    }

    @ShellMethod(
            key = {"book"},
            value = "Books tickets for the specified screening and seats (specified by row and column pairs)")
    @ShellMethodAvailability("isAuthenticated")
    public List<String> book(String movieTitle, String roomName, LocalDateTime startDateTime, List<Seat> seats) {
        var usernameOptional = getSecurityService().username();
        if (usernameOptional.isEmpty()) {
            throw new IllegalStateException("Current username was null");
        }
        var username = usernameOptional.get();
        try {
            var request = MultiSeatBookingRequest.builder()
                    .username(username)
                    .movieTitle(movieTitle)
                    .roomName(roomName)
                    .startDateTime(startDateTime)
                    .seats(seats)
                    .build();
            var response = bookingService.book(request);
            var seatsAsString = response.getSeats()
                    .stream()
                    .sorted()
                    .map(seat -> String.format("(%d,%d)", seat.getRow(), seat.getColumn()))
                    .collect(Collectors.joining(", "));
            var outputString = String.format("Seats booked: %s; the price for this booking is %d HUF",
                    seatsAsString,
                    response.getSumPrice());
            return List.of(outputString);
        } catch (MultiSeatBookingNotPossibleException e) {
            return e.getErrors();
        } catch (Exception e) {
            return List.of("Booking seats failed due to general error");
        }
    }
}
