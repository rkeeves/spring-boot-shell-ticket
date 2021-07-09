package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DefaultScreeningTimeClashServiceTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private DefaultScreeningTimeClashService clashService;

    private final LocalDateTime existingStart = atFixedDay("10:10");

    private final int existingDuration = 40;

    @BeforeEach
    void init() {
        clashService = new DefaultScreeningTimeClashService();
    }

    @ParameterizedTest
    @MethodSource("provideNonCollisions")
    void givenNonCollidingTimes_whenChek_thenDontThrow(String startTimeString,
                                                       String endTimeString) {
        // given
        LocalDateTime start = atFixedDay(startTimeString);
        LocalDateTime end = atFixedDay(endTimeString);
        // when
        assertDoesNotThrow(() -> clashService.check(start, end, existingStart, existingDuration));
        // then
    }

    static Stream<Arguments> provideNonCollisions() {
        return Stream.of(
                Arguments.of("09:00", "09:50"),
                Arguments.of("09:00", "10:00"),
                Arguments.of("11:00", "12:00"),
                Arguments.of("11:10", "12:10")
        );
    }

    @ParameterizedTest
    @MethodSource("provideOverlaps")
    void givenWouldOverlap_whenChek_thenThrow(String startTimeString,
                                              String endTimeString) {
        // given
        LocalDateTime start = atFixedDay(startTimeString);
        LocalDateTime end = atFixedDay(endTimeString);
        // when
        var exception = assertThrows(ScreeningTimeClashException.class, () -> clashService.check(start, end, existingStart, existingDuration));
        // then
        assertEquals("There is an overlapping screening", exception.getMessage());
    }

    static Stream<Arguments> provideOverlaps() {
        return Stream.of(
                Arguments.of("09:00", "10:30"),
                Arguments.of("09:00", "10:50"),
                Arguments.of("10:10", "12:00"),
                Arguments.of("10:30", "12:00"),
                Arguments.of("10:10", "10:50"),
                Arguments.of("10:10", "10:40"),
                Arguments.of("10:20", "10:40"),
                Arguments.of("10:20", "10:50"),
                Arguments.of("09:00", "11:00"),
                Arguments.of("09:00", "12:00"),
                Arguments.of("10:00", "12:00")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNewWouldStartInBreakAfterExisting")
    void givenNewWouldStartInBreakAfterExisting_whenChek_thenThrow(String startTimeString,
                                              String endTimeString) {
        // given
        var expected = "This would start in the break period "
                + "after another screening in this room";
        LocalDateTime start = atFixedDay(startTimeString);
        LocalDateTime end = atFixedDay(endTimeString);
        // when
        var exception = assertThrows(ScreeningTimeClashException.class, () -> clashService.check(start, end, existingStart, existingDuration));
        // then
        assertEquals(expected, exception.getMessage());
    }

    static Stream<Arguments> provideNewWouldStartInBreakAfterExisting() {
        return Stream.of(
                Arguments.of("10:50", "12:00"),
                Arguments.of("10:55", "12:00")
        );
    }

    @ParameterizedTest
    @MethodSource("provideExistingWouldStartInBreakAfterNew")
    void givenExistingWouldStartInBreakAfterNew_whenChek_thenThrow(String startTimeString,
                                                     String endTimeString) {
        // given
        var expected = "The existing movie start in the break period "
                + "after the new screening in this room";
        LocalDateTime start = atFixedDay(startTimeString);
        LocalDateTime end = atFixedDay(endTimeString);
        // when
        var exception = assertThrows(ScreeningTimeClashException.class, () -> clashService.check(start, end, existingStart, existingDuration));
        // then
        assertEquals(expected, exception.getMessage());
    }

    static Stream<Arguments> provideExistingWouldStartInBreakAfterNew() {
        return Stream.of(
                Arguments.of("09:00", "10:10"),
                Arguments.of("09:00", "10:05")
        );
    }

    static LocalDateTime atFixedDay(String time) {
        return LocalDateTime.parse("2020-12-08 "+time, formatter);
    }
}