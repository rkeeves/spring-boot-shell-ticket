package com.epam.training.ticketservice.core.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seat implements Comparable<Seat> {

    private final int row;

    private final int column;

    public static Seat of(int row, int column) {
        return Seat.builder()
                .row(row)
                .column(column)
                .build();
    }

    @Override
    public int compareTo(Seat o) {
        if (row < o.row) {
            return -1;
        }
        if (row > o.row) {
            return 1;
        }
        if (column < o.column) {
            return -1;
        }
        if (column > o.column) {
            return 1;
        }
        return 0;
    }
}
