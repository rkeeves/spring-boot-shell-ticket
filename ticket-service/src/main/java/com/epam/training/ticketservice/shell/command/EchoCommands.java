package com.epam.training.ticketservice.shell.command;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
@ShellCommandGroup("echo")
public class EchoCommands {

    @ShellMethod(key = "echo",
            value = "Echoes the given string")
    public String echo(String message) {
       return message;
    }

    @ShellMethod(key = "echo optional",
            value = "Echoes the given string if it is not 'null' otherwise does not echo")
    public Optional<String> echoOptional(String message) {
        if ("null".equals(message)) {
            return Optional.empty();
        }
        return Optional.of(message);
    }
}
