package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefaultPriceService implements PriceService {

    private int basePrice = 1500;

    @Override
    public int getPerSeatPriceBy(String movieTitle, String roomName, LocalDateTime startDateTime) {
        return 0;
    }

    @Override
    public int getPerSeatPriceBy(Screening screening) {
        return basePrice;
    }

    @Override
    public void updateBasePrice(int newBasePrice) {
        basePrice = newBasePrice;
    }

    @Override
    public void createPriceComponent(String name, int newBasePrice) {
        throw new RuntimeException();
    }

    @Override
    public void attachPriceComponentToMovie(String priceComponent, String movieTitle) {
        throw new RuntimeException();
    }

    @Override
    public void attachPriceComponentToRoom(String priceComponent, String roomName) {
        throw new RuntimeException();
    }

    @Override
    public void attachPriceComponentToScreening(String priceComponent, String movieTitle, String roomName, LocalDateTime startDateTime) {
        throw new RuntimeException();
    }
}
