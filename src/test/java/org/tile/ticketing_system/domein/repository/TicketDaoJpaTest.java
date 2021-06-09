package org.tile.ticketing_system.domein.repository;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.tile.ticketing_system.domein.Ticket;

import static org.assertj.core.api.Assertions.assertThat;

class TicketDaoJpaTest {

    private final TicketDaoJpa ticketDaoJpa = new TicketDaoJpa();

    @Test
    void shouldFindTicketsByEmployeeId() {
        List<Ticket> tickets = this.ticketDaoJpa.findAllByEmployeeId("437aaf43-5abe-405e-bbb7-c6fc06b39ceb");
        assertThat(tickets.size()).isGreaterThan(11);
    }

    @Test
    void shouldFindTicketsNoTicketsForWrongEmployeeId() {
        List<Ticket> tickets = this.ticketDaoJpa.findAllByEmployeeId("someWrongId");
        assertThat(tickets).isEmpty();
    }
}
