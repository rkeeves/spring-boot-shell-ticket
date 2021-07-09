package com.epam.training.ticketservice.shell.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles({"test"})
public class StepDefsIntegrationTest extends SpringIntegrationTest {

    @Given("the application is started")
    public void applicationStarted() {

    }

    @Given("the prompt containing {string} is printed")
    public void promptReturned(String expectedPrompt) {
        assertThat(promptProvider.getPrompt(), notNullValue());
        assertThat(promptProvider.getPrompt().toString(), equalTo(expectedPrompt));
    }

    @When("the user types the {string} command")
    public void theUserTypes(String command) throws IOException {
        evaluateUserInputLine(command);
    }

    @Then("the next line of the output is {string}")
    public void theNextLineOfOutputContains(String expectedOutput) {
        String actualLine = lastLine();
        assertThat(actualLine, equalTo(expectedOutput));
    }

    @Then("the {string} shell command is not available")
    public void theCommandIsNotAvailable(String shellCommand) {
        assertFalse(isAvailable(shellCommand));
    }

    @After
    public void cleanup() {
        currentAuthenticationService.setAuthentication(null);
    }
}
