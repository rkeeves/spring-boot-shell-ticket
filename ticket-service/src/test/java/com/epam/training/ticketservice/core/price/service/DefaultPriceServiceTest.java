package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DefaultPriceServiceTest {

    @InjectMocks
    private DefaultPriceService priceService;

    @Test
    void givenScreeningValid_whenGetPerSeatPriceBy_thenReturnCorrectPrice() {
        // given
        var basePrice = 1000;
        var screening = createScreening();
        priceService.updateBasePrice(basePrice);
        // when
        var result = priceService.getPerSeatPriceBy(screening);
        // then
        assertEquals(basePrice, result);
    }

    private Screening createScreening() {
        return Screening.builder()
                .room(createRoom(10,20))
                .movie(createMovie())
                .startDateTime(LocalDateTime.of(2000,12,8,12,3))
                .build();
    }

    private Movie createMovie() {
        return Movie.builder()
                .title("movieA")
                .genre("genreA")
                .durationInMinutes(100)
                .build();
    }

    private Room createRoom(int rows, int cols) {
        return Room.builder()
                .name("roomA")
                .rows(rows)
                .columns(cols)
                .build();
    }

}