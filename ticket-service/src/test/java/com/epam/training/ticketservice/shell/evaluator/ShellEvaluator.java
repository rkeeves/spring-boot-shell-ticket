package com.epam.training.ticketservice.shell.evaluator;

import org.springframework.shell.Shell;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ShellEvaluator implements EnterCommandStep, ExpectOutputStep {

    private Shell shell;

    private String command;

    private ShellEvaluator(Shell shell) {
        this.shell = shell;
    }

    public static EnterCommandStep usingShell(Shell shell) {
        return new ShellEvaluator(shell);
    }

    @Override
    public ExpectOutputStep afterCommand(String command) {
        this.command = command;
        return this;
    }

    @Override
    public void expectOutput(String... lines) {
        assertEquals(Arrays.asList(lines), shell.evaluate(() -> command));
    }

    public static void commandIsNotAvailable(Shell shell, String command) {
        var method = shell.listCommands().get(command);
        assertFalse(method.getAvailability().isAvailable());
    }
}
