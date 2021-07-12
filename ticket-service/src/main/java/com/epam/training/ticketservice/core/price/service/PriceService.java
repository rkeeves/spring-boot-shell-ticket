package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.screening.entity.Screening;

public interface PriceService {

    int getPerSeatPriceBy(Screening screening);

    void updateBasePrice(int newBasePrice);
}
