package com.epam.training.ticketservice.shell.config;

import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class ShellConfiguration {

    @Value("${app.shell.prompt}")
    private String prompt;

    @Bean
    public PromptProvider tickerServicePrompt() {
        return () -> new AttributedString(prompt);
    }
}
