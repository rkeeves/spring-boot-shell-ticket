package com.epam.training.ticketservice.core.booking.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SeatTest {

    @ParameterizedTest
    @MethodSource("provideForCompareTo")
    void test_compareTo(Seat first, Seat second, int expected) {
        assertEquals(expected, first.compareTo(second));
    }

    static Stream<Arguments> provideForCompareTo() {
        return Stream.of(
                Arguments.of(Seat.of(1, 1), Seat.of(2, 2), -1),
                Arguments.of(Seat.of(1, 2), Seat.of(2, 2), -1),
                Arguments.of(Seat.of(1, 3), Seat.of(2, 2), -1),
                Arguments.of(Seat.of(1, 1), Seat.of(1, 2), -1),
                Arguments.of(Seat.of(2, 1), Seat.of(1, 2), 1),
                Arguments.of(Seat.of(2, 2), Seat.of(1, 2), 1),
                Arguments.of(Seat.of(2, 3), Seat.of(1, 2), 1),
                Arguments.of(Seat.of(2, 3), Seat.of(2, 2), 1),
                Arguments.of(Seat.of(2, 2), Seat.of(1, 2), 1),
                Arguments.of(Seat.of(2, 2), Seat.of(2, 2), 0)
        );
    }
}