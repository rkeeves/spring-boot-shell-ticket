package com.epam.training.ticketservice.core.booking.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeatTest {

    @ParameterizedTest
    @MethodSource("provideForCompareTo")
    void test_compareTo(Seat first, Seat second) {
        var expected = Comparator.comparingInt(Seat::getRow)
                .thenComparing(Seat::getColumn)
                .compare(first, second);
        assertEquals(expected, first.compareTo(second));
    }

    static Stream<Arguments> provideForCompareTo() {
        var list = new ArrayList<Arguments>();
        for (int rowA = 0; rowA < 3; rowA++) {
            for (int colA = 0; colA < 3; colA++) {
                var seatA = Seat.of(rowA, colA);
                for (int rowB = 0; rowB < 3; rowB++) {
                    for (int colB = 0; colB < 3; colB++) {
                        list.add(Arguments.of(seatA, Seat.of(rowB, colB)));
                    }
                }
            }
        }
        return list.stream();
    }
}