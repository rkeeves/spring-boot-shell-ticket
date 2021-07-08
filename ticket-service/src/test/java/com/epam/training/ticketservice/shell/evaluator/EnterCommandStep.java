package com.epam.training.ticketservice.shell.evaluator;

public interface EnterCommandStep {

    ExpectOutputStep afterCommand(String command);

    ExpectOutputStep afterCommandByWords(String... words);
}
