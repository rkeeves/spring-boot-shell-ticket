package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultPriceService implements PriceService {

    private int basePrice = 1500;

    @Override
    public int getPerSeatPriceBy(Screening screening) {
        return basePrice;
    }

    @Override
    public void updateBasePrice(int newBasePrice) {
        basePrice = newBasePrice;
    }
}
