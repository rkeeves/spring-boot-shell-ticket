package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.price.entity.PriceComponent;

import java.util.Set;

public interface Priceable {

    Set<PriceComponent> getPriceComponents();
}
