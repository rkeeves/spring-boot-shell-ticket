package com.epam.training.ticketservice.core.room.service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface RoomService {

    List<String> list();

    void create(String name, int rows, int colums) throws EntityExistsException;

    void updateByName(String name, int rows, int colums) throws EntityNotFoundException;

    void deleteByName(String name) throws EntityNotFoundException;
}
