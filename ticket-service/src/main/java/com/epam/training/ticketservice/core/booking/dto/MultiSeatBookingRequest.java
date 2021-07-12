package com.epam.training.ticketservice.core.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MultiSeatBookingRequest {

    private final String username;

    private final String movieTitle;

    private final String roomName;

    private final LocalDateTime startDateTime;

    private final List<Seat> seats;
}
