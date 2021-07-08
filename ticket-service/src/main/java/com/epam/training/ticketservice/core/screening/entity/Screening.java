package com.epam.training.ticketservice.core.screening.entity;

import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.room.entity.Room;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "screenings")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"movie", "room"})
public class Screening {

    @EmbeddedId
    private ScreeningId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("movieId")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roomId")
    private Room room;

    @Builder
    public Screening(Movie movie, Room room, LocalDateTime startDateTime) {
        this.movie = movie;
        this.room = room;
        this.id = new ScreeningId(movie.getId(), room.getId(), startDateTime);
    }
}
