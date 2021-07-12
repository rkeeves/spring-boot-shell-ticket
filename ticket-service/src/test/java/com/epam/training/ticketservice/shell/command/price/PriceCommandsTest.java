package com.epam.training.ticketservice.shell.command.price;

import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.price.service.PriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceCommandsTest {

    @Mock
    private PriceService priceService;

    @InjectMocks
    private PriceCommands priceCommands;

    @Test
    void givenAuthorizedToUseCommand_whenUpdateBasePrice_thenCallService() {
        // given
        // when
        priceCommands.updateBasePrice(5000);
        // then
        verify(priceService, times(1))
                .updateBasePrice(5000);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenNoExceptionsOccur_whenShowPriceFor_thenReturnCorrectAnswer() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000, 12, 1, 12, 0);
        var perSeatPrice = 1000;
        var seats = List.of(Seat.of(10,10), Seat.of(10, 11));
        when(priceService.getPerSeatPriceBy(movieTitle, roomName, startDateTime))
                .thenReturn(perSeatPrice);
        // when
        var result = priceCommands.showPriceFor(movieTitle, roomName, startDateTime, seats);
        // then
        assertEquals(List.of("The price for this booking would be 2000 HUF"), result);
        verify(priceService, times(1))
                .getPerSeatPriceBy(movieTitle, roomName, startDateTime);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenExceptionOccurs_whenShowPriceFor_thenReturnErrorStrings() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000, 12, 1, 12, 0);
        var seats = List.of(Seat.of(10,10), Seat.of(10, 11));
        doThrow(RuntimeException.class)
                .when(priceService)
                .getPerSeatPriceBy(movieTitle, roomName, startDateTime);
        // when
        var result = priceCommands.showPriceFor(movieTitle, roomName, startDateTime, seats);
        // then
        assertEquals(List.of("Show price for bookings failed"), result);
        verify(priceService, times(1))
                .getPerSeatPriceBy(movieTitle, roomName, startDateTime);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenExceptionOccurs_whenAttachPriceComponentToRoom_thenReturnErrorStringList() {
        // given
        var roomName = "roomA";
        var priceName = "fee";
        doThrow(RuntimeException.class)
                .when(priceService)
                .attachPriceComponentToRoom(priceName, roomName);
        // when
        var result = priceCommands.attachPriceComponentToRoom(priceName, roomName);
        // then
        assertEquals(List.of("Attaching price component to room failed"), result);
        verify(priceService, times(1))
                .attachPriceComponentToRoom(priceName, roomName);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenNoExceptionOccurs_whenAttachPriceComponentToRoom_thenReturnEmptyList() {
        // given
        var roomName = "roomA";
        var priceName = "fee";
        // when
        var result = priceCommands.attachPriceComponentToRoom(priceName, roomName);
        // then
        assertEquals(Collections.emptyList(), result);
        verify(priceService, times(1))
                .attachPriceComponentToRoom(priceName, roomName);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenExceptionOccurs_whenAttachPriceComponentToMovie_thenReturnErrorStringList() {
        // given
        var movieTitle = "movieA";
        var priceName = "fee";
        doThrow(RuntimeException.class)
                .when(priceService)
                .attachPriceComponentToMovie(priceName, movieTitle);
        // when
        var result = priceCommands.attachPriceComponentToMovie(priceName, movieTitle);
        // then
        assertEquals(List.of("Attaching price component to movie failed"), result);
        verify(priceService, times(1))
                .attachPriceComponentToMovie(priceName, movieTitle);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenNoExceptionOccurs_whenAttachPriceComponentToMovie_thenReturnEmptyList() {
        // given
        var movieTitle = "movieA";
        var priceName = "fee";
        // when
        var result = priceCommands.attachPriceComponentToMovie(priceName, movieTitle);
        // then
        assertEquals(Collections.emptyList(), result);
        verify(priceService, times(1))
                .attachPriceComponentToMovie(priceName, movieTitle);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenExceptionOccurs_whenAttachPriceComponentToScreening_thenReturnErrorStrings() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000, 12, 1, 12, 0);
        var priceName = "fee";
        doThrow(RuntimeException.class)
                .when(priceService)
                .attachPriceComponentToScreening(priceName, movieTitle, roomName, startDateTime);
        // when
        var result = priceCommands.attachPriceComponentToScreening(priceName,
                movieTitle,
                roomName,
                startDateTime);
        // then
        assertEquals(List.of("Attaching price component to screening failed"), result);
        verify(priceService, times(1))
                .attachPriceComponentToScreening(priceName,
                        movieTitle,
                        roomName,
                        startDateTime);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenNoExceptionOccurs_whenAttachPriceComponentToScreening_thenReturnEmptyList() {
        // given
        var movieTitle = "movieA";
        var roomName = "roomA";
        var startDateTime = LocalDateTime.of(2000, 12, 1, 12, 0);
        var priceName = "fee";
        // when
        var result = priceCommands.attachPriceComponentToScreening(priceName,
                movieTitle,
                roomName,
                startDateTime);
        // then
        assertEquals(Collections.emptyList(), result);
        verify(priceService, times(1))
                .attachPriceComponentToScreening(priceName,
                        movieTitle,
                        roomName,
                        startDateTime);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenExceptionOccurs_whenCreatePriceComponent_thenReturnErrorStrings() {
        // given
        var priceName = "movieA";
        var price = 10;
        doThrow(RuntimeException.class)
                .when(priceService)
                .createPriceComponent(priceName, price);
        // when
        var result = priceCommands.createPriceComponent(priceName, price);
        // then
        assertEquals(List.of("Creating price component failed"), result);
        verify(priceService, times(1))
                .createPriceComponent(priceName, price);
        verifyNoMoreInteractions(priceService);
    }

    @Test
    void givenNoExceptionOccurs_whenCreatePriceComponent_thenReturnErrorStrings() {
        // given
        var priceName = "movieA";
        var price = 10;
        // when
        var result = priceCommands.createPriceComponent(priceName, price);
        // then
        assertEquals(Collections.emptyList(), result);
        verify(priceService, times(1))
                .createPriceComponent(priceName, price);
        verifyNoMoreInteractions(priceService);
    }
}