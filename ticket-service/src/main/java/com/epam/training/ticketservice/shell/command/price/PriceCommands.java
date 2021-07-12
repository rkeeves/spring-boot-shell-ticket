package com.epam.training.ticketservice.shell.command.price;

import com.epam.training.ticketservice.core.price.service.PriceService;
import com.epam.training.ticketservice.core.security.service.SecurityService;
import com.epam.training.ticketservice.shell.command.SecuredCommand;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

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
}
