package com.epam.training.ticketservice.core.price.service;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultPriceServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private PriceComponentRepository priceComponentRepository;

    @InjectMocks
    private DefaultPriceService priceService;

    @Test
    void givenScreeningValidHasNoPriceComponents_whenGetPerSeatPriceByScreening_thenReturnCorrectPrice() {
        // given
        var basePrice = 1000;
        priceService.updateBasePrice(basePrice);
        var screening = createScreening();
        // when
        var result = priceService.getPerSeatPriceBy(screening);
        // then
        assertEquals(basePrice, result);
    }

    @Test
    void givenScreeningHasPriceComponents_whenGetPerSeatPriceByScreening_thenReturnCorrectPrice() {
        // given
        var basePrice = 1000;
        priceService.updateBasePrice(basePrice);
        var screening = createScreening();
        var screeningFee = 1300;
        var priceComponent = PriceComponent.builder()
                .name("someComponent")
                .price(screeningFee)
                .build();
        screening.getPriceComponents().add(priceComponent);
        // when
        var result = priceService.getPerSeatPriceBy(screening);
        // then
        assertEquals(basePrice + screeningFee, result);
    }

    @Test
    void givenRoomHasPriceComponents_whenGetPerSeatPriceByScreening_thenReturnCorrectPrice() {
        // given
        var basePrice = 1000;
        priceService.updateBasePrice(basePrice);
        var screening = createScreening();
        var roomFee = 1300;
        var priceComponent = PriceComponent.builder()
                .name("someComponent")
                .price(roomFee)
                .build();
        screening.getRoom().getPriceComponents().add(priceComponent);
        // when
        var result = priceService.getPerSeatPriceBy(screening);
        // then
        assertEquals(basePrice + roomFee, result);
    }

    @Test
    void givenMovieHasPriceComponents_whenGetPerSeatPriceByScreening_thenReturnCorrectPrice() {
        // given
        var basePrice = 1000;
        priceService.updateBasePrice(basePrice);
        var screening = createScreening();
        var movieFee = 1300;
        var priceComponent = PriceComponent.builder()
                .name("someComponent")
                .price(movieFee)
                .build();
        screening.getMovie().getPriceComponents().add(priceComponent);
        // when
        var result = priceService.getPerSeatPriceBy(screening);
        // then
        assertEquals(basePrice + movieFee, result);
    }

    @Test
    void givenScreeningExists_whenGetPerSeatPriceByMovieTitleRoomNameStartDateTime_thenReturnCorrectPrice() {
        // given
        var basePrice = 0;
        priceService.updateBasePrice(basePrice);
        var movieTitle = "movieA";
        var movie = createMovie(movieTitle);
        var roomName = "roomA";
        var room = createRoom(roomName);
        var startDateTime = LocalDateTime.of(2000,12,8,12,3);
        var screening = createScreening(movie, room, startDateTime);
        var screeningFee = 1;
        var screeningComponent = createPriceComponent("screeningFee", screeningFee);
        var roomFee = 10;
        var roomComponent = createPriceComponent("movieFee", roomFee);
        var movieFee = 100;
        var movieComponent = createPriceComponent("movieFee", movieFee);
        screening.getPriceComponents().add(screeningComponent);
        screening.getRoom().getPriceComponents().add(roomComponent);
        screening.getMovie().getPriceComponents().add(movieComponent);
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle,
                roomName,
                startDateTime))
                .thenReturn(Optional.of(screening));
        // when
        var result = priceService.getPerSeatPriceBy(movieTitle, roomName, startDateTime);
        // then
        assertEquals(basePrice + screeningFee + roomFee + movieFee, result);
        verify(screeningRepository, times(1))
            .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle,
                roomName,
                startDateTime);
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void givenScreeningDoesNotExist_whenGetPerSeatPriceByMovieTitleRoomNameStartDateTime_thenThrow() {
        // given
        var basePrice = 0;
        priceService.updateBasePrice(basePrice);
        var movieTitle = "movieA";
        var movie = createMovie(movieTitle);
        var roomName = "roomA";
        var room = createRoom(roomName);
        var startDateTime = LocalDateTime.of(2000,12,8,12,3);
        var screening = createScreening(movie, room, startDateTime);
        var screeningFee = 1;
        var screeningComponent = createPriceComponent("screeningFee", screeningFee);
        var roomFee = 10;
        var roomComponent = createPriceComponent("movieFee", roomFee);
        var movieFee = 100;
        var movieComponent = createPriceComponent("movieFee", movieFee);
        screening.getPriceComponents().add(screeningComponent);
        screening.getRoom().getPriceComponents().add(roomComponent);
        screening.getMovie().getPriceComponents().add(movieComponent);
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> {
            priceService.getPerSeatPriceBy(movieTitle, roomName, startDateTime);
        });
        // then
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle,
                        roomName,
                        startDateTime);
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void givenValidInputs_whenCreatePriceComponent_thenSaveNewPriceComponent() {
        // given
        var name = "screeningFee";
        var fee = 100;
        // when
        priceService.createPriceComponent(name, fee);
        var priceComponent = createPriceComponent(name, fee);
        // then
        verify(priceComponentRepository, times(1))
                .save(priceComponent);
        verifyNoMoreInteractions(priceComponentRepository);
    }

    @Test
    void givenValidInputs_whenAttachPriceComponentToMovie_thenAddPriceComponent() {
        // given
        var movieTitle = "movieA";
        var movie = createMovie(movieTitle);
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(movieRepository.findByTitle(movieTitle))
                .thenReturn(Optional.of(movie));
        when(movieRepository.save(movie))
                .thenReturn(movie);
        // when
        priceService.attachPriceComponentToMovie(name, movieTitle);
        // then
        assertTrue(movie.getPriceComponents().contains(priceComponent));
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(movieRepository, times(1))
                .findByTitle(movieTitle);
        verify(movieRepository, times(1))
                .save(movie);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenMovieNotFound_whenAttachPriceComponentToMovie_thenAddPriceComponent() {
        // given
        var movieTitle = "movieA";
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(movieRepository.findByTitle(movieTitle))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToMovie(name, movieTitle));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(movieRepository, times(1))
                .findByTitle(movieTitle);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void givenPriceComponentNotFound_whenAttachPriceComponentToMovie_thenThrow() {
        // given
        var movieTitle = "movieA";
        var name = "fee";
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToMovie(name, movieTitle));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoInteractions(movieRepository);
    }

    @Test
    void givenValidInputs_whenAttachPriceComponentToRoom_thenAddPriceComponent() {
        // given
        var roomName = "roomA";
        var room = createRoom(roomName);
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(room));
        when(roomRepository.save(room))
                .thenReturn(room);
        // when
        priceService.attachPriceComponentToRoom(name, roomName);
        // then
        assertTrue(room.getPriceComponents().contains(priceComponent));
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verify(roomRepository, times(1))
                .save(room);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomNotFound_whenAttachPriceComponentToRoom_thenAddPriceComponent() {
        // given
        var roomName = "roomA";
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToRoom(name, roomName));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenPriceComponentNotFound_whenAttachPriceComponentToRoom_thenThrow() {
        // given
        var roomName = "roomA";
        var name = "fee";
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToRoom(name, roomName));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoInteractions(roomRepository);
    }

    @Test
    void givenValidInputs_whenAttachPriceComponentToScreening_thenAddPriceComponent() {
        // given
        var movieTitle = "movieA";
        var movie = createMovie(movieTitle);
        var roomName = "roomA";
        var room = createRoom(roomName);
        var startDateTime = LocalDateTime.of(2000,12,8,12,3);
        var screening = createScreening(movie, room, startDateTime);
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.of(screening));
        when(screeningRepository.save(screening))
                .thenReturn(screening);
        // when
        priceService.attachPriceComponentToScreening(name, movieTitle, roomName, startDateTime);
        // then
        assertTrue(screening.getPriceComponents().contains(priceComponent));
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verify(screeningRepository, times(1))
                .save(screening);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void givenScreeningNotFound_whenAttachPriceComponentToScreening_thenAddPriceComponent() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,8,12,3);
        var name = "fee";
        var fee = 100;
        var priceComponent = createPriceComponent(name, fee);
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.of(priceComponent));
        when(screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToScreening(name, movieTitle, roomName, startDateTime));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verify(screeningRepository, times(1))
                .findByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle, roomName, startDateTime);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void givenPriceComponentNotFound_whenAttachPriceComponentToScreening_thenThrow() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000,12,8,12,3);
        var name = "fee";
        when(priceComponentRepository.findByName(name))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> priceService.attachPriceComponentToScreening(name, movieTitle, roomName, startDateTime));
        // then
        verify(priceComponentRepository, times(1))
                .findByName(name);
        verifyNoMoreInteractions(priceComponentRepository);
        verifyNoInteractions(screeningRepository);
    }

    private Screening createScreening() {
        return createScreening(createMovie(),
                createRoom(),
                LocalDateTime.of(2000,12,8,12,3));
    }

    private Screening createScreening(Movie movie, Room room, LocalDateTime startDateTime) {
        return Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .build();
    }

    private Movie createMovie() {
        return createMovie("movieA");
    }

    private Movie createMovie(String title) {
        return Movie.builder()
                .title(title)
                .genre("genreA")
                .durationInMinutes(100)
                .build();
    }

    private Room createRoom() {
        return createRoom("roomA");
    }

    private Room createRoom(String name) {
        return Room.builder()
                .name(name)
                .rows(10)
                .columns(10)
                .build();
    }

    private PriceComponent createPriceComponent(String name, int price) {
        return PriceComponent.builder()
                .name(name)
                .price(price)
                .build();
    }
}