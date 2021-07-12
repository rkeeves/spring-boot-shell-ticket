package com.epam.training.ticketservice.core.movie.entity;

import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.service.Priceable;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
public class Movie implements Priceable {

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

    @OneToMany(mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Screening> screenings = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "movie_price",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "price_id")
    )
    private Set<PriceComponent> priceComponents = new HashSet<>();

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

    public Movie() {

    }
}
