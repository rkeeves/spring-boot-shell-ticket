package com.epam.training.ticketservice.shell.converter;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SeatConverterTest {

    private SeatConverter seatConverter;

    @BeforeEach
    void init() {
        seatConverter = new SeatConverter();
    }

    @ParameterizedTest
    @MethodSource("provideValidSeatLists")
    void givenValidString_whenConvert_thenReturnListOfSeats(String input, List<Seat> expected) {
        assertEquals(expected, seatConverter.convert(input));
    }

    static Stream<Arguments> provideValidSeatLists() {
        return Stream.of(
                Arguments.of("1,2", List.of(seat(1,2))),
                Arguments.of("1,2 3,4", List.of(seat(1,2), seat(3,4))),
                Arguments.of("1,2 3,4 5,6", List.of(seat(1,2), seat(3,4), seat(5,6)))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSeatLists")
    void givenInvalidString_whenConvert_thenThrow(String input) {
        assertThrows(RuntimeException.class, () -> seatConverter.convert(input));
    }

    static Stream<Arguments> provideInvalidSeatLists() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("1"),
                Arguments.of("1,"),
                Arguments.of("1, 2"),
                Arguments.of("1 ,2"),
                Arguments.of("(1,2"),
                Arguments.of("1,2,3,4"),
                Arguments.of("1,2 3,4,5,6)")
        );
    }

    private static Seat seat(int row, int column) {
        return Seat.of(row, column);
    }
}