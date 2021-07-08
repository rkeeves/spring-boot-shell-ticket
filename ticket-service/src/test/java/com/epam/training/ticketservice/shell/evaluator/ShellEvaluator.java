package com.epam.training.ticketservice.shell.evaluator;

import org.springframework.shell.Input;
import org.springframework.shell.Shell;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ShellEvaluator implements EnterCommandStep, ExpectOutputStep {

    private Shell shell;

    private Input input;

    private ShellEvaluator(Shell shell) {
        this.shell = shell;
    }

    public static EnterCommandStep usingShell(Shell shell) {
        return new ShellEvaluator(shell);
    }

    @Override
    public ExpectOutputStep afterCommand(String command) {
        this.input = () -> command;
        return this;
    }

    @Override
    public ExpectOutputStep afterCommandByWords(String... words) {
        this.input = inputOfWords(words);
        return this;
    }

    @Override
    public void expectOutput(String... lines) {
        assertEquals(Arrays.asList(lines), shell.evaluate(input));
    }

    public static void commandIsNotAvailable(Shell shell, String command) {
        var method = shell.listCommands().get(command);
        assertFalse(method.getAvailability().isAvailable());
    }

    public static Input inputOfWords(String... words) {
        return new Input() {

            @Override
            public String rawText() {
                return Arrays.stream(words).collect(Collectors.joining(" "));
            }

            @Override
            public List<String> words() {
                return List.of(words);
            }
        };
    }
}
