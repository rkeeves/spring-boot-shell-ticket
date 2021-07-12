package com.epam.training.ticketservice.core.room.repository;

import com.epam.training.ticketservice.core.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Transactional(readOnly = true)
    Optional<Room> findByName(String name);
}
