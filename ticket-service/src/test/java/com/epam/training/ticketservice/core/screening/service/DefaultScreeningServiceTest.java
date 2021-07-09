package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultScreeningServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private ScreeningTimeClashService clashService;

    @InjectMocks
    private DefaultScreeningService screeningService;

    @Test
    void givenEntitiesDontExist_whenList_thenReturnEmptyList() {
        // given
        when(screeningRepository.findAll())
                .thenReturn(Collections.emptyList());
        // when
        var list = screeningService.list();
        // then
        assertTrue(list.isEmpty());
        verify(screeningRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenEntitiesExist_whenList_thenReturnDescriptionStringList() {
        // given
        var movieA = Movie.builder()
                .id(1L)
                .title("Movie A")
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        var roomA = Room.builder()
                .id(2L)
                .name("Room A")
                .rows(10)
                .columns(20)
                .build();
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        var screeningA = Screening.builder()
                .movie(movieA)
                .room(roomA)
                .startDateTime(startTimeA)
                .build();
        var movieB = Movie.builder()
                .id(1L)
                .title("Movie B")
                .genre("genre B")
                .durationInMinutes(20)
                .build();
        var roomB = Room.builder()
                .id(2L)
                .name("Room B")
                .rows(20)
                .columns(30)
                .build();
        var startTimeB = LocalDateTime.of(2000,1,1,1,2);
        var screeningB = Screening.builder()
                .movie(movieB)
                .room(roomB)
                .startDateTime(startTimeB)
                .build();
        var screenings = List.of(screeningA, screeningB);
        var expected = List.of("Movie A (genre A, 10 minutes), screened in room Room A, at 2000-01-01 01:01",
                "Movie B (genre B, 20 minutes), screened in room Room B, at 2000-01-01 01:02");
        when(screeningRepository.findAll())
                .thenReturn(screenings);
        // when
        var list = screeningService.list();
        // then
        assertEquals(expected, list);
        verify(screeningRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrows_whenList_thenThrow() {
        // given
        doThrow(RuntimeException.class)
                .when(screeningRepository)
                .findAll();
        // when
        assertThrows(RuntimeException.class, () -> screeningService.list());
        // then
        verify(screeningRepository, times(1))
                .findAll();
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenEntityDoesNotExistAlready_whenCreateScreening_thenSaveEntity() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var movieA = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        var roomA = Room.builder()
                .id(2L)
                .name(roomName)
                .rows(10)
                .columns(20)
                .build();
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        var screeningA = Screening.builder()
                .movie(movieA)
                .room(roomA)
                .startDateTime(startTimeA)
                .build();
        var movieModified = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        movieModified.getScreenings().add(screeningA);
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieA));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(roomA));
        when(movieRepository.save(movieModified))
                .thenReturn(movieModified);
        when(screeningRepository.findAllByRoom(roomA))
                .thenReturn(Collections.emptyList());
        // when
        screeningService.createScreening(title, roomName, startTimeA);
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verify(screeningRepository, times(1))
                .findAllByRoom(roomA);
        verify(movieRepository, times(1))
                .save(movieModified);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenMovieDoesNotExist_whenCreateScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> screeningService.createScreening(title, roomName, startTimeA));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRoomDoesNotExist_whenCreateScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var movieA = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieA));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.empty());
        // when
        assertThrows(EntityNotFoundException.class, () -> screeningService.createScreening(title, roomName, startTimeA));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenAnyTimeClashExists_whenCreateScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var movieA = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        var roomA = Room.builder()
                .id(2L)
                .name(roomName)
                .rows(10)
                .columns(20)
                .build();
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        var screeningA = Screening.builder()
                .movie(movieA)
                .room(roomA)
                .startDateTime(startTimeA)
                .build();
        var movieModified = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        movieModified.getScreenings().add(screeningA);
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieA));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(roomA));
        when(movieRepository.save(movieModified))
                .thenReturn(movieModified);
        when(screeningRepository.findAllByRoom(roomA))
                .thenReturn(Collections.emptyList());
        // when
        screeningService.createScreening(title, roomName, startTimeA);
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrowsOnSave_whenCreateScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var movieA = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        var roomA = Room.builder()
                .id(2L)
                .name(roomName)
                .rows(10)
                .columns(20)
                .build();
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        var screeningA = Screening.builder()
                .movie(movieA)
                .room(roomA)
                .startDateTime(startTimeA)
                .build();
        var movieModified = Movie.builder()
                .id(1L)
                .title(title)
                .genre("genre A")
                .durationInMinutes(10)
                .build();
        movieModified.getScreenings().add(screeningA);
        when(movieRepository.findByTitle(title))
                .thenReturn(Optional.of(movieA));
        when(roomRepository.findByName(roomName))
                .thenReturn(Optional.of(roomA));
        when(screeningRepository.findAllByRoom(roomA))
                .thenReturn(Collections.emptyList());
        doThrow(RuntimeException.class)
                .when(movieRepository)
                .save(movieModified);
        // when
        assertThrows(RuntimeException.class, () -> screeningService.createScreening(title, roomName, startTimeA));
        // then
        verify(movieRepository, times(1))
                .findByTitle(title);
        verify(roomRepository, times(1))
                .findByName(roomName);
        verify(screeningRepository, times(1))
                .findAllByRoom(roomA);
        verify(movieRepository, times(1))
                .save(movieModified);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenEntityDoesExist_whenDeleteScreening_thenDeleteEntity() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        when(screeningRepository.deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA))
                .thenReturn(1);
        // when
        assertDoesNotThrow(() -> screeningService.deleteScreening(title, roomName, startTimeA));
        // then
        verify(screeningRepository, times(1))
                .deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryFailsToDelete_whenDeleteScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        when(screeningRepository.deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA))
                .thenReturn(0);
        // when
        assertThrows(EntityNotFoundException.class, () -> screeningService.deleteScreening(title, roomName, startTimeA));
        // then
        verify(screeningRepository, times(1))
                .deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void givenRepositoryThrowsOnDelete_whenDeleteScreening_thenThrow() {
        // given
        var title = "Movie A";
        var roomName = "Room A";
        var startTimeA = LocalDateTime.of(2000,1,1,1,1);
        doThrow(RuntimeException.class)
                .when(screeningRepository)
                .deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA);
        // when
        assertThrows(RuntimeException.class, () -> screeningService.deleteScreening(title, roomName, startTimeA));
        // then
        verify(screeningRepository, times(1))
                .deleteByMovieTitleAndRoomNameAndIdStartDateTime(title, roomName, startTimeA);
        verifyNoMoreInteractions(screeningRepository);
        verifyNoMoreInteractions(clashService);
        verifyNoMoreInteractions(movieRepository);
        verifyNoMoreInteractions(roomRepository);
    }
}