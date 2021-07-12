package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DefaultPriceService implements PriceService {

    private int basePrice = 1500;

    private final MovieRepository movieRepository;

    private final RoomRepository roomRepository;

    private final ScreeningRepository screeningRepository;

    private final PriceComponentRepository priceComponentRepository;

    @Override
    @Transactional(readOnly = true)
    public int getPerSeatPriceBy(String movieTitle, String roomName, LocalDateTime startDateTime) {
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(
                movieTitle,
                roomName,
                startDateTime)
                .orElseThrow(() -> new EntityNotFoundException("Screening not found"));
        return getPerSeatPriceBy(screening);
    }

    @Override
    @Transactional(readOnly = true)
    public int getPerSeatPriceBy(Screening screening) {
        var screeningPrices = screening.getPriceComponents().stream();
        var moviePrices = screening.getMovie().getPriceComponents().stream();
        var roomPrices =  screening.getRoom().getPriceComponents().stream();
        return Stream.concat(roomPrices, Stream.concat(screeningPrices, moviePrices))
                .mapToInt(PriceComponent::getPrice)
                .reduce(basePrice, Integer::sum);
    }

    @Override
    public void updateBasePrice(int newBasePrice) {
        basePrice = newBasePrice;
    }

    @Override
    @Transactional
    public void createPriceComponent(String name, int price) {
        var priceComponent = PriceComponent.builder()
                .name(name)
                .price(price)
                .build();
        priceComponentRepository.save(priceComponent);
    }

    @Override
    @Transactional
    public void attachPriceComponentToMovie(String priceName, String movieTitle) {
        var priceComponent = findPriceComponentByName(priceName);
        var movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new EntityNotFoundException("Movie was not found"));
        movie.getPriceComponents().add(priceComponent);
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void attachPriceComponentToRoom(String priceName, String roomName) {
        var priceComponent = findPriceComponentByName(priceName);
        var room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new EntityNotFoundException("Room was not found"));
        room.getPriceComponents().add(priceComponent);
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void attachPriceComponentToScreening(String priceName,
                                                String movieTitle,
                                                String roomName,
                                                LocalDateTime startDateTime) {
        var priceComponent = findPriceComponentByName(priceName);
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle,
                roomName,
                startDateTime)
                .orElseThrow(() -> new EntityNotFoundException("Screening was not found"));
        screening.getPriceComponents().add(priceComponent);
        screeningRepository.save(screening);
    }

    private PriceComponent findPriceComponentByName(String priceName) {
        return priceComponentRepository.findByName(priceName)
                .orElseThrow(() -> new EntityNotFoundException("Price component was not found"));
    }
}
