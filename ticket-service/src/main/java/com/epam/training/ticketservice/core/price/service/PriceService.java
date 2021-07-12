package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.screening.entity.Screening;

import java.time.LocalDateTime;

public interface PriceService {

    int getPerSeatPriceBy(String movieTitle, String roomName, LocalDateTime startDateTime);

    int getPerSeatPriceBy(Screening screening);

    void updateBasePrice(int newBasePrice);

    void createPriceComponent(String name, int newBasePrice);

    void attachPriceComponentToMovie(String priceComponent, String movieTitle);

    void attachPriceComponentToRoom(String priceComponent, String roomName);

    void attachPriceComponentToScreening(String priceComponent,
                                         String movieTitle,
                                         String roomName,
                                         LocalDateTime startDateTime);
}
