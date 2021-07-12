package com.epam.training.ticketservice.core.screening.repository;

import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.screening.entity.Screening;
import com.epam.training.ticketservice.core.screening.entity.ScreeningId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, ScreeningId> {

    @Transactional(readOnly = true)
    Optional<Screening> findByMovieTitleAndRoomNameAndIdStartDateTime(String movieTitle,
                                                                      String roomName,
                                                                      LocalDateTime startDateTime);

    @Transactional(readOnly = true)
    List<Screening> findAllByRoom(Room room);

    @Modifying
    int deleteByMovieTitleAndRoomNameAndIdStartDateTime(String movieTitle,
                                                        String roomName,
                                                        LocalDateTime startDateTime);
}
