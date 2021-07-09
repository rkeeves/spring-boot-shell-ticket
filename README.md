# Movie Ticket Shell App 

> Based on an [EPAM course material](https://github.com/epam-deik-cooperation/epam-deik-java-dev/tree/main/final) at DE IK PTI.

A small Spring Shell application for managing movie tickets using Spring Boot. 

## Getting started

### Packaging the app

To build and package the application you must have maven installed on your system 
(assuming you are in project root folder):

```
mvn package
```

This will create an executable jar at `ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar` 

### Running the packaged app

You have two options depending on what kind of database you want to use:
* Use H2
* Use MySQL (default)

#### H2

The easiest way is to start the app is using an H2 embedded database.
The H2 database will be deleted when you exit the application.

> This will also setup an admin account for you (username: admin, password: admin).

```
java -jar -Dspring.profiles.active=ci ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar
```

#### MySQL (default)

You can also use a proper MySQL database for persistent data storage. This is the default setting.

> An admin account won't be created, so you have to set it up yourself.

You can supply the datasource url, username and password two ways:

##### Via environment variables

The following environment variables can be used to supply the values:
* `MYSQL_DATABASE_URL` - connection url
* `MYSQL_DATABASE_USERNAME` - username
* `MYSQL_DATABASE_PASSWORD` - password

Then just start the application:

```
java -jar ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar 
```

##### Via command line

The following properties can be used:
* `spring.datasource.url` - connection url
* `spring.datasource.username` - username
* `spring.datasource.password` - password

Example usage:

```
java -jar ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar --spring.datasource.url=jdbc:mysql://localhost:3306/something --spring.datasource.username=something --spring.datasource.password=something 
```

## Acceptance Tests

The app is basically a movie ticket management system with a bit of user management added on top of it, 
and most importantly: a simple shell program, aka no fancy things.

The app has a clearly defined set of requirements that it must meet.
These can be found under the **ticket-service-acceptance-tests** module.

> For more info checkout [Details](DETAILS.md)

All of them follow the gherkin/cucumber/BDD format, aka:

```
Feature: README readability

  Background:
    Given a person is seeing this README

  Scenario: a non-blind person can read the README
    Given the user is not blind
    When the user sees this README
    Then the user will imagine a pony
```

## Running the Tests

There are 4 possible levels of requirements.
Starting from grade 2, each one getting more and more difficult.

* Grade 2 includes grade2 tests only
* Grade 3 includes grade2, 3 tests
* Grade 4 includes grade2, 3, 4 tests
* Grade 5 includes grade2, 3, 4, 5 tests

You don't have to set up the db, the acceotance tests use the `ci` profile which sets up everything.

Be aware: the acceptance tests can take a long time (like 2-5 minutes).
They are started when you see the following on the output:

```
Running com.training.epam.ticketservice.at.TestExecutor
```

You can run any of them by a simple maven command.

##### Grade 2 (least difficult)

```shell
mvn clean verify -P requirements-grade2
```

##### Grade 3

```shell
mvn clean verify -P requirements-grade3
```

##### Grade 4

```shell
mvn clean verify -P requirements-grade4
```

##### Grade 5 (most difficult)

```shell
mvn clean verify -P requirements-grade5
```

## Acceptance Test Fails

> Tests in the *ticket-service* module don't have IO related issues. *They also have new scenarios.*

If an acceptance test fails it doesn't necessary indicate the program's incorrectness.

The acceptance tests start the program as a process, basically like this:

```shell
java -jar -Dspring.profiles.active=ci ../ticket-service/target/ticket-service-0.0.1-SNAPSHOT.jar
```

Due to IO, issues can occur which are specified below.

#### TimeoutException

The test failed due to the fact that it didn't receive the program's prompt on the output.

The root cause of this is (most of the time) that the application is still loading, 
but the test assumes that it's dead or stuck if a certain time duration has passed.

If you see that the test fail because of timeout, 
then try increasing the value of the timeout (so that the process gets more time to load up).
To do this, modify the following constant *(in the ticket-service-acceptance-tests subproject)*:

```
com.training.epam.ticketservice.at.GenericCliProcessStepDefs.OUTPUT_TIMEOUT
```

#### Prompt or last command treated as output

The acceptance test sometimes expect more than one line of output and make assertions based on multiline outputs.
To tackle this issue the test runner clears the output after the last command.
If a process is still generating output after the clearing it will be stuck in the buffer.

If you see that some assertions fail because they somehow read the prompt itself, or the old output, 
then you must increase the value of the IO wait (so that the process gets more time to load up).
To do this, modify the following constant *(in the ticket-service-acceptance-tests subproject)*:

```
com.training.epam.ticketservice.at.ProcessUnderTest.DELAY_BEFORE_CLEANING_PROCESS_OUTPUT
```
### Acceptance Test Inconsistencies

Some acceptance test include some well hidden *typos*.

For example, the user can `sign up` but the case below uses `create account`.

```
Scenario: an authenticated, non-privileged user can list movies
    Given the user types the "sign in privileged admin admin" command
    And the user types the "create movie Sátántangó drama 450" command
    And the user types the "sign out" command
    And the user types the "create account sanyi asdQWE123" command
    When the user types the "sign in sanyi asdQWE123" command
    And the user types the "list movies" command
    Then the next line of the output is "Sátántangó (drama, 450 minutes)"
```

Maybe it was intended? 
Or maybe they were testing the students by giving *noisy* criteria to simulate real life environments?
Or maybe they were testing whether the students follow requirements blindly?

Who knows?!

## Links

The original project can be found [here](https://github.com/epam-deik-cooperation/epam-deik-java-dev/tree/main/final).