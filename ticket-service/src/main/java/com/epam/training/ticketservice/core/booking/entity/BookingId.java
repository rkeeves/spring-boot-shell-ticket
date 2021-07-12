package com.epam.training.ticketservice.core.booking.entity;

import com.epam.training.ticketservice.core.screening.entity.ScreeningId;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class BookingId implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Embedded
    private ScreeningId screeningId;

    private Long accountId;

    @NotNull
    @Min(value = 1)
    @Column(name = "seat_row")
    private Integer row;

    @NotNull
    @Min(value = 1)
    @Column(name = "seat_column")
    private Integer column;

    @Builder
    public BookingId(ScreeningId screeningId,
                     Long accountId,
                     @NotNull @Min(value = 1) Integer row,
                     @NotNull @Min(value = 1) Integer column) {
        this.screeningId = screeningId;
        this.accountId = accountId;
        this.row = row;
        this.column = column;
    }
}
