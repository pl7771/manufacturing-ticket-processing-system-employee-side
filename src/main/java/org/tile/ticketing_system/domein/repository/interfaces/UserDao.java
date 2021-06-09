package org.tile.ticketing_system.domein.repository.interfaces;

import java.util.Optional;

import org.tile.ticketing_system.domein.User;

public interface UserDao extends GenericDao<User>{

    Optional<User> findByEmailAddress(String emailAddress);
}
