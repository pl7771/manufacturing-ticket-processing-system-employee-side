package org.tile.ticketing_system.domein.repository.interfaces;

import java.util.List;

public interface GenericDao<T> {
    List<T> findAll();
    <U> T get(U id);
    T update(T object);
    void delete(T object);
    void insert(T object);
    <U> boolean exists(U id);
}

