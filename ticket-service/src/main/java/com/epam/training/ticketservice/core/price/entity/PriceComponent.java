package com.epam.training.ticketservice.core.price.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "price_components")
@Data
@NoArgsConstructor
public class PriceComponent {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @Column
    private int price;

    @Builder
    public PriceComponent(Long id, @NotNull @NotEmpty String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
