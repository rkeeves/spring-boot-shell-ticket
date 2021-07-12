package com.epam.training.ticketservice.core.screening.entity;

import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.movie.entity.Movie;
import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.service.Priceable;
import com.epam.training.ticketservice.core.room.entity.Room;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "screenings")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"movie", "room"})
public class Screening implements Priceable {

    @EmbeddedId
    private ScreeningId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("movieId")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roomId")
    private Room room;

    @OneToMany(mappedBy = "screening",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "screening_price",
            joinColumns = {
                    @JoinColumn(name = "screening_movie_id", referencedColumnName = "movie_id"),
                    @JoinColumn(name = "screening_room_id", referencedColumnName = "room_id"),
                    @JoinColumn(name = "screening_start_date_time", referencedColumnName = "start_date_time")
            },
            inverseJoinColumns = @JoinColumn(name = "price_id")
    )
    private Set<PriceComponent> priceComponents = new HashSet<>();

    @Builder
    public Screening(Movie movie, Room room, LocalDateTime startDateTime) {
        this.movie = movie;
        this.room = room;
        this.id = new ScreeningId(movie.getId(), room.getId(), startDateTime);
    }
}
