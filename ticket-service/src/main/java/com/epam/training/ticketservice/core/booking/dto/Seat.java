package com.epam.training.ticketservice.core.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seat implements Comparable<Seat> {

    private final int row;

    private final int column;

    @Override
    public int compareTo(Seat o) {
        if (row < o.row || (row == o.row && column < o.column)) {
            return -1;
        }
        if (row > o.row || (row == o.row && column > o.column)) {
            return 1;
        }
        return 0;
    }
}