package com.epam.training.ticketservice.core.movie.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String title;

    @NotNull
    @NotEmpty
    @Column
    private String genre;

    @Min(value = 1)
    @NotNull
    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes;

    @Builder
    public Movie(Long id,
                 @NotNull @NotEmpty String title,
                 @NotNull @NotEmpty String genre,
                 @Min(value = 1) @NotNull Integer durationInMinutes) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.durationInMinutes = durationInMinutes;
    }
}
