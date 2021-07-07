package com.epam.training.ticketservice.core.account.entity;

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
