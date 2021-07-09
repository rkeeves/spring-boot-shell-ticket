package com.epam.training.ticketservice.shell.cucumber;

import com.epam.training.ticketservice.core.security.service.CurrentAuthenticationService;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
@DirtiesContext
@CucumberContextConfiguration
@ActiveProfiles({"test"})
public class SpringIntegrationTest {

    @Autowired
    protected CurrentAuthenticationService currentAuthenticationService;

    @Autowired
    protected PromptProvider promptProvider;

    @Autowired
    ShellEvaluator shellEvaluator;

    protected void evaluateUserInputLine(String userInputLine) {
        shellEvaluator.eval(userInputLine);
    }

    protected String lastLine() {
        return shellEvaluator.lastLine();
    }

    protected boolean isAvailable(String shellCommand) {
        return shellEvaluator.isAvailable(shellCommand);
    }
}
