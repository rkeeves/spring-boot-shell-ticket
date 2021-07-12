package com.epam.training.ticketservice.shell.cucumber;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.fail;

@Component
@RequiredArgsConstructor
public class ShellEvaluator {

    private final Shell shell;

    private final Queue<String> output = new ArrayDeque<>();

    void eval(String userInputLine) {
        var result = shell.evaluate(QuotedStringsEscapedAdapter.of(userInputLine));
        if (result == null) {
            fail(userInputLine + " returned null");
        }
        if (result instanceof CommandNotCurrentlyAvailable) {
            fail(userInputLine + " was NOT available");
        }
        try {
            List<String> list = cast(result);
            output.addAll(list);
        } catch (ClassCastException e) {
            fail(userInputLine + " result was not a string list");
        }
    }

    String lastLine() {
        return output.poll();
    }

    boolean isAvailable(String shellCommand) {
        var command = shell.listCommands().get(shellCommand);
        return command.getAvailability().isAvailable();
    }

    @SuppressWarnings("unchecked")
    private static <T extends List<?>> T cast(Object obj) {
        return (T) obj;
    }

    public void clearOutputBuffer() {
        output.clear();
    }
}
