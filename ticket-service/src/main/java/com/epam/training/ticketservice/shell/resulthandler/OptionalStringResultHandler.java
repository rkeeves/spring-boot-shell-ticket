package com.epam.training.ticketservice.shell.resulthandler;

import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.springframework.shell.ResultHandler;

import java.util.Optional;

@RequiredArgsConstructor
public class OptionalStringResultHandler implements ResultHandler<Optional<String>> {

    private final Terminal terminal;

    @Override
    public void handleResult(Optional<String> s) {
        if (s.isPresent()) {
            terminal.writer().println(s.get());
            terminal.flush();
        }
    }
}