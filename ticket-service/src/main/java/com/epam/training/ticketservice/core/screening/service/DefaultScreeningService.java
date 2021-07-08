package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.exception.ScreeningTimeClashException;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultScreeningService implements ScreeningService {

    private final MovieRepository movieRepository;

    private final RoomRepository roomRepository;

    private final ScreeningRepository screeningRepository;

    private final ScreeningTimeClashService screeningTimeClashService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    @Transactional(readOnly = true)
    public List<String> list() {
        return screeningRepository.findAll()
                .stream()
                .map(this::describe)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ScreeningTimeClashException.class})
    public void createScreening(String movieTitle, String roomName, LocalDateTime startDateTime)
            throws EntityNotFoundException, ScreeningTimeClashException {
        var movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new EntityNotFoundException("Movie " + movieTitle + " does not exist"));
        var room = roomRepository.findByName(roomName)
                .orElseThrow(() -> new EntityNotFoundException("Room " + roomName + " does not exist"));
        screeningTimeClashService.checkNoClash(movie.getDurationInMinutes(), room, startDateTime);
        var screening = Screening.builder()
                .movie(movie)
                .room(room)
                .startDateTime(startDateTime)
                .build();
        movie.getScreenings().add(screening);
        movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startDateTime) {
        int deletedRows = screeningRepository.deleteByMovieTitleAndRoomNameAndIdStartDateTime(movieTitle,
                roomName,
                startDateTime);
        if (deletedRows != 1) {
            throw new EntityNotFoundException("Failed to delete");
        }
    }

    private String describe(Screening screening) {
        var movie = screening.getMovie();
        var movieTitle = movie.getTitle();
        var movieGenre = movie.getGenre();
        var movieDuration = movie.getDurationInMinutes();
        var roomName = screening.getRoom().getName();
        var startDateTimeString = screening.getId().getStartDateTime().format(dateTimeFormatter);
        return String.format("%s (%s, %d minutes), screened in room %s, at %s",
                movieTitle,
                movieGenre,
                movieDuration,
                roomName,
                startDateTimeString);
    }
}
