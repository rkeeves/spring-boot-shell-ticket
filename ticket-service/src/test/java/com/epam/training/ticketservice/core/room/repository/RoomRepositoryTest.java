package com.epam.training.ticketservice.core.room.repository;

import com.epam.training.ticketservice.core.room.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomRepositoryTest {

    // TestEntityManager is used to catch validation errors
    // https://github.com/spring-projects/spring-boot/issues/7079
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void givenValidEntity_whenSave_thenEntityIsSaved() {
        // given
        var name = "some title";
        var rows = 12;
        var columns = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        var savedRoom = roomRepository.save(room);
        // then
        assertNotNull(savedRoom);
        assertEquals(name, savedRoom.getName());
        assertEquals(rows, savedRoom.getRows());
        assertEquals(columns, savedRoom.getColumns());
    }

    @Test
    void givenNameIsNull_whenSave_thenThrows() {
        // given
        var rows = 12;
        var columns = 10;
        var room = Room.builder()
                .name(null)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenNameIsEmpty_whenSave_thenThrows() {
        // given
        var name = "";
        var rows = 12;
        var columns = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenNameIsNotUnique_whenSave_thenThrows() {
        // given
        var name = "";
        var rows = 12;
        var columns = 10;
        var otherRows = 12;
        var otherColumns = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        var otherRoom = Room.builder()
                .name(name)
                .rows(otherRows)
                .columns(otherColumns)
                .build();
        // when
        roomRepository.save(room);
        roomRepository.save(otherRoom);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenRowsIsNull_whenSave_thenThrows() {
        // given
        var name = "name";
        var columns = 10;
        var room = Room.builder()
                .name(name)
                .rows(null)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenRowsIsZero_whenSave_thenThrows() {
        // given
        var name = "name";
        var rows = 0;
        var columns = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenRowsIsNegative_whenSave_thenThrows() {
        // given
        var name = "name";
        var rows = -1;
        var columns = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenColumnsIsNull_whenSave_thenThrows() {
        // given
        var name = "name";
        var rows = 10;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(null)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenColumnsIsZero_whenSave_thenThrows() {
        // given
        var name = "name";
        var rows = 21;
        var columns = 0;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }

    @Test
    void givenColumnsIsNegative_whenSave_thenThrows() {
        // given
        var name = "name";
        var rows = 21;
        var columns = -1;
        var room = Room.builder()
                .name(name)
                .rows(rows)
                .columns(columns)
                .build();
        // when
        roomRepository.save(room);
        // then
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.flush());
    }
}