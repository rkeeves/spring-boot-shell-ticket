package com.epam.training.ticketservice.shell.converter;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeatConverter implements Converter<String, List<Seat>> {

    @Override
    public List<Seat> convert(String seatListExpression) {
        return Arrays.stream(seatListExpression.split(" "))
                .map(seatExpression -> {
                    var parts = seatExpression.split(",");
                    if (parts.length != 2) {
                        throw new RuntimeException("Invalid string expression was given for seat list");
                    }
                    return Seat.builder()
                            .row(Integer.parseInt(parts[0]))
                            .column(Integer.parseInt(parts[1]))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
