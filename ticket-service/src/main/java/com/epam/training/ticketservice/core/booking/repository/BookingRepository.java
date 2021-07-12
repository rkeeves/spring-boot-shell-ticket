package com.epam.training.ticketservice.core.booking.repository;

import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.booking.entity.BookingId;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, BookingId> {

    @Transactional(readOnly = true)
    List<Booking> findAllByScreeningAndIdRowAndIdColumn(Screening screening, int row, int column);
}
