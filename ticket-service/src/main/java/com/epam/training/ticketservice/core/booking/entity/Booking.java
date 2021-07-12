package com.epam.training.ticketservice.core.booking.entity;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "bookings")
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    @EmbeddedId
    private BookingId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("screeningId")
    private Screening screening;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("accountId")
    private Account owner;

    @Column
    private int price;

    @Builder
    public Booking(BookingId id, Screening screening, Account owner, int price) {
        this.id = id;
        this.screening = screening;
        this.owner = owner;
        this.price = price;
    }
}
