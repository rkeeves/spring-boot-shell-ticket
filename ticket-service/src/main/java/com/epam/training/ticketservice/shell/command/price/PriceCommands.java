package com.epam.training.ticketservice.shell.command.price;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.price.service.PriceService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ShellComponent
@ShellCommandGroup("price")
public class PriceCommands extends SecuredCommand {

    private final PriceService priceService;

    public PriceCommands(SecurityService securityService,
                         PriceService priceService) {
        super(securityService);
        this.priceService = priceService;
    }

    @ShellMethod(
            key = {"update base price"},
            value = "changes the base per seat price")
    @ShellMethodAvailability("isPrivileged")
    public List<String> updateBasePrice(int newBasePrice) {
        priceService.updateBasePrice(newBasePrice);
        return Collections.emptyList();
    }

    @ShellMethod(
            key = {"show price for"},
            value = "Shows the price for the given booking")
    @ShellMethodAvailability("isPrivileged")
    public List<String> showPriceFor(String movieTitle,
                                     String roomName,
                                     LocalDateTime startDateTime,
                                     List<Seat> seats) {
        try {
            var price = priceService.getPerSeatPriceBy(movieTitle, roomName, startDateTime);
            return List.of(String.format("The price for this booking would be %d HUF",
                    seats.size() * price));
        } catch (Exception e) {
            return List.of("Show price for bookings failed");
        }
    }

    @ShellMethod(
            key = {"creates price component"},
            value = "Creates a price component")
    @ShellMethodAvailability("isPrivileged")
    public List<String> createPriceComponent(String priceComponent, int fee) {
        try {
            priceService.createPriceComponent(priceComponent, fee);
            return Collections.emptyList();
        } catch (Exception e) {
            return List.of("Creating price component failed");
        }
    }

    @ShellMethod(
            key = {"attach price component to movie"},
            value = "Adds a new price component by the specified name to the movie")
    @ShellMethodAvailability("isPrivileged")
    public List<String> attachPriceComponentToMovie(String priceComponent, String movieTitle) {
        try {
            priceService.attachPriceComponentToMovie(priceComponent, movieTitle);
            return Collections.emptyList();
        } catch (Exception e) {
            return List.of("Attaching price component to movie failed");
        }
    }

    @ShellMethod(
            key = {"attach price component to room"},
            value = "Adds a new price component by the specified name to the room")
    @ShellMethodAvailability("isPrivileged")
    public List<String> attachPriceComponentToRoom(String priceComponent, String roomName) {
        try {
            priceService.attachPriceComponentToRoom(priceComponent, roomName);
            return Collections.emptyList();
        } catch (Exception e) {
            return List.of("Attaching price component to room failed");
        }
    }

    @ShellMethod(
            key = {"attach price component to screening"},
            value = "Adds a new price component by the specified name to the screening")
    @ShellMethodAvailability("isPrivileged")
    public List<String> attachPriceComponentToScreening(String priceComponent,
                                                        String movieTitle,
                                                        String roomName,
                                                        LocalDateTime startDateTime) {
        try {
            priceService.attachPriceComponentToScreening(priceComponent, movieTitle, roomName, startDateTime);
            return Collections.emptyList();
        } catch (Exception e) {
            return List.of("Attaching price component to screening failed");
        }
    }
}
