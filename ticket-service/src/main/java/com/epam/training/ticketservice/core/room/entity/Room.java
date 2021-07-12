package com.epam.training.ticketservice.core.room.entity;

import com.epam.training.ticketservice.core.price.entity.PriceComponent;
import com.epam.training.ticketservice.core.price.service.Priceable;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
public class Room implements Priceable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @Min(value = 1)
    @Column(name = "seating_rows")
    private Integer rows;

    @NotNull
    @Min(value = 1)
    @Column(name = "seating_columns")
    private Integer columns;

    @OneToMany(mappedBy = "room",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Screening> screenings = new HashSet<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "room_price",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "price_id")
    )
    private Set<PriceComponent> priceComponents = new HashSet<>();

    @Builder
    public Room(Long id,
                @NotNull @NotEmpty String name,
                @NotNull @Min(value = 1) Integer rows,
                @NotNull @Min(value = 1) Integer columns) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }
}
