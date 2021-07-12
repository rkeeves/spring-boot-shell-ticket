package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.account.entity.Account;
import com.epam.training.ticketservice.core.account.repository.AccountRepository;
import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingRequest;
import com.epam.training.ticketservice.core.booking.dto.MultiSeatBookingResponse;
import com.epam.training.ticketservice.core.booking.dto.Seat;
import com.epam.training.ticketservice.core.booking.entity.Booking;
import com.epam.training.ticketservice.core.booking.entity.BookingId;
import com.epam.training.ticketservice.core.booking.exception.MultiSeatBookingNotPossibleException;
import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.price.service.PriceService;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements BookingService {

    private final AccountRepository accountRepository;

    private final ScreeningRepository screeningRepository;

    private final BookingRepository bookingRepository;

    private final PriceService priceService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    @Transactional(readOnly = true)
    public List<String> listBookingsByAccount(String username) {
        var owner = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("The user does not exist"));
        Map<Screening, List<Booking>> map = owner.getBookings()
                .stream()
                .collect(groupingBy(Booking::getScreening));
        return map.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> bookingsGroupedByScreeningsToString(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private String bookingsGroupedByScreeningsToString(Screening screening, List<Booking> bookings) {
        var seatsAsString = bookingSeatsAsString(bookings);
        var sumPrice = sumPriceOfBookings(bookings);
        return String.format("Seats %s on %s in room %s starting at %s for %d HUF",
                seatsAsString,
                screening.getMovie().getTitle(),
                screening.getRoom().getName(),
                screening.getId().getStartDateTime().format(dateTimeFormatter),
                sumPrice);
    }

    private String bookingSeatsAsString(Collection<Booking> bookings) {
        return bookings.stream()
                .map(Booking::getId)
                .map(bookingId -> Seat.builder().row(bookingId.getRow()).column(bookingId.getColumn()).build())
                .sorted()
                .map(bookingId -> String.format("(%d,%d)", bookingId.getRow(), bookingId.getColumn()))
                .collect(Collectors.joining(", "));
    }

    private int sumPriceOfBookings(Collection<Booking> bookings) {
        return bookings.stream()
                .mapToInt(Booking::getPrice)
                .sum();
    }

    @Override
    @Transactional(rollbackFor = { MultiSeatBookingNotPossibleException.class })
    public MultiSeatBookingResponse book(MultiSeatBookingRequest request)
            throws MultiSeatBookingNotPossibleException {
        var owner = accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("The user does not exist"));
        var screening = screeningRepository.findByMovieTitleAndRoomNameAndIdStartDateTime(
                request.getMovieTitle(),
                request.getRoomName(),
                request.getStartDateTime())
                .orElseThrow(() -> new EntityNotFoundException("The screening does not exist"));
        var seats = request.getSeats();

        var errors = listErrors(screening, seats);
        if (!errors.isEmpty()) {
            throw new MultiSeatBookingNotPossibleException(errors);
        }

        var perSeatPrice = priceService.getPerSeatPriceBy(screening);
        saveAllBookings(screening, owner, seats, perSeatPrice);
        return MultiSeatBookingResponse.builder()
                .sumPrice(perSeatPrice * seats.size())
                .seats(seats)
                .build();

    }

    private List<String> listErrors(Screening screening, List<Seat> seats) {
        var rows = screening.getRoom().getRows();
        var columns = screening.getRoom().getColumns();
        var errors = new ArrayList<String>();
        for (var seat : seats) {
            var row = seat.getRow();
            var col = seat.getColumn();
            if (row > rows || col > columns) {
                errors.add(String.format("Seat (%d,%d) is invalid, room has %d rows and %d columns",
                        row, col, rows, columns));
            }
            var existingBookings = bookingRepository
                    .findAllByScreeningAndIdRowAndIdColumn(screening, row, col);
            if (!existingBookings.isEmpty()) {
                errors.add(String.format("Seat (%d,%d) is already taken", row, col));
            }
        }
        return errors;
    }

    private void saveAllBookings(Screening screening, Account owner, List<Seat> seats, int perSeatPrice) {
        var bookings = screening.getBookings();
        seats.stream()
                .map(seat -> createBooking(screening, seat.getRow(), seat.getColumn(), owner, perSeatPrice))
                .forEach(bookings::add);
        screeningRepository.save(screening);
    }

    private Booking createBooking(Screening screening, int row, int col, Account owner, int perSeatPrice) {
        var bookingId = BookingId.builder()
                .screeningId(screening.getId())
                .row(row)
                .column(col)
                .build();
        return Booking.builder()
                .id(bookingId)
                .owner(owner)
                .screening(screening)
                .price(perSeatPrice)
                .build();
    }
}
