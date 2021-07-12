package com.epam.training.ticketservice.core.price.repository;

import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PriceComponentRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PriceComponentRepository priceComponentRepository;

    @Test
    void givenEntityExists_whenFindByName_thenReturnEntity() {
        // given
        var name = "somename";
        var price = 100;
        var priceComponent = PriceComponent.builder()
                .name(name)
                .price(price)
                .build();
        testEntityManager.persist(priceComponent);
        // when
        var result = priceComponentRepository.findByName(name);
        // then
        assertTrue(result.isPresent());
        var actual = result.get();
        assertEquals(name, actual.getName());
        assertEquals(price, actual.getPrice());
    }

    @Test
    void givenEntityDoesNotExist_whenFindByName_thenReturnEmpty() {
        // given
        var name = "somename";
        // when
        var result = priceComponentRepository.findByName(name);
        // then
        assertTrue(result.isEmpty());
    }
}