package com.epam.training.ticketservice.core.account.entity;

import com.epam.training.ticketservice.core.booking.entity.Booking;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "username", unique = true)
    private String username;

    @NotNull
    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "privileged")
    private boolean privileged;

    @OneToMany(mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    @Builder
    public Account(Long id,
                   @NotNull @NotEmpty String username,
                   @NotNull @NotEmpty String password,
                   boolean privileged) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.privileged = privileged;
    }
}
