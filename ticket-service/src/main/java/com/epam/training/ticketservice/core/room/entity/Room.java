package com.epam.training.ticketservice.core.room.entity;

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
@Table(name = "rooms")
@Data
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @Min(value = 1)
    @Column
    private Integer rows;

    @NotNull
    @Min(value = 1)
    @Column
    private Integer columns;

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
