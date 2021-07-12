package com.epam.training.ticketservice.core.price.repository;

import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PriceComponentRepository extends JpaRepository<PriceComponent, Long> {

    @Transactional
    Optional<PriceComponent> findByName(String name);
}
