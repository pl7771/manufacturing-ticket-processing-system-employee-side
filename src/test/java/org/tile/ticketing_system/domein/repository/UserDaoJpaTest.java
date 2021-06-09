package org.tile.ticketing_system.domein.repository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.tile.ticketing_system.domein.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoJpaTest {
    // DIT PAKT DE ECHTE DATABANK
    private final UserDaoJpa userDaoJpa = new UserDaoJpa();

    @Test
    void shouldFindUserByEmailAddress() {
        Optional<User> foundUser = this.userDaoJpa.findByEmailAddress("supportmanager@gmail.com");

        assertThat(foundUser).isPresent();
        foundUser.ifPresent(user -> {
            assertThat(user.getUserName()).isEqualTo("supportmanager@gmail.com");
            assertThat(user.getFirstName()).isEqualTo("Support");
            assertThat(user.getLastName()).isEqualTo("Manager");
            assertThat(user.isSupportManager()).isTrue();
        });
    }

    @Test
    void shouldReturnEmptyOptionalWhenNoUserFound() {
        Optional<User> foundUser = this.userDaoJpa.findByEmailAddress("NOTEXISTANT");

        assertThat(foundUser).isEmpty();
    }
}