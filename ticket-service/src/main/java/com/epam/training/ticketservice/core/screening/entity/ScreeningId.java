package com.epam.training.ticketservice.core.screening.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class ScreeningId implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    public ScreeningId() {

    }

    public ScreeningId(Long movieId, Long roomId, LocalDateTime startDateTime) {
        this.movieId = movieId;
        this.roomId = roomId;
        this.startDateTime = startDateTime;
    }
}
