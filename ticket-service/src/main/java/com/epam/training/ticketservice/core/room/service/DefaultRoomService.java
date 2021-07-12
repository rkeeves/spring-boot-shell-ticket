package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.entity.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultRoomService implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> list() {
        return roomRepository.findAll()
                .stream()
                .map(this::describe)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = EntityExistsException.class)
    public void create(String name, int rows, int colums) throws EntityExistsException {
        var result = roomRepository.findByName(name);
        if (result.isPresent()) {
            throw new EntityExistsException(name);
        }
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(colums)
                .build();
        roomRepository.save(room);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void updateByName(String name, int rows, int colums) throws EntityNotFoundException {
        var result = roomRepository.findByName(name);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(name);
        }
        var room = result.get();
        room.setRows(rows);
        room.setColumns(colums);
        roomRepository.save(room);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteByName(String name) throws EntityNotFoundException {
        var result = roomRepository.findByName(name);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(name);
        }
        var room = result.get();
        roomRepository.delete(room);
    }

    private String describe(Room room) {
        var name = room.getName();
        var rows = room.getRows();
        var cols = room.getColumns();
        var seats = rows * cols;
        return String.format("Room %s with %d seats, %d rows and %d columns", name, seats, rows, cols);
    }
}
