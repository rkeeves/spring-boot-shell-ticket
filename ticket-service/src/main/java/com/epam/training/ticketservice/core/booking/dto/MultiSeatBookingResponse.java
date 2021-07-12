package com.epam.training.ticketservice.core.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MultiSeatBookingResponse {

    private final int sumPrice;

    private final List<Seat> seats;
}
