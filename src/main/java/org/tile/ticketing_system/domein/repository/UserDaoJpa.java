package org.tile.ticketing_system.domein.repository;

import java.util.Optional;

import javax.persistence.NoResultException;

import lombok.extern.java.Log;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.repository.interfaces.UserDao;

@Log
public class UserDaoJpa extends GenericDaoJpa<User> implements UserDao {

    public UserDaoJpa() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        try {
            var getUserQuery = em.createQuery("SELECT u from User u where u.userName = ?1").setParameter(1, emailAddress);
            var foundUser = (User) getUserQuery.getSingleResult();
            log.info("Found user: " + foundUser.getUserName());
            return Optional.of(foundUser);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
