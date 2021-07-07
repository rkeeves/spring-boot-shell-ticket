package com.epam.training.ticketservice.shell.config;

import com.epam.training.ticketservice.shell.resulthandler.OptionalStringResultHandler;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.ResultHandler;
import org.springframework.shell.jline.PromptProvider;

import java.util.Optional;

@Configuration
public class ShellConfiguration {

    @Value("${app.shell.prompt}")
    private String prompt;

    @Bean
    public PromptProvider tickerServicePrompt() {
        return () -> new AttributedString(prompt);
    }

    @Bean
    public ResultHandler<Optional<String>> shellHelper(@Lazy Terminal terminal) {
        return new OptionalStringResultHandler(terminal);
    }
}
