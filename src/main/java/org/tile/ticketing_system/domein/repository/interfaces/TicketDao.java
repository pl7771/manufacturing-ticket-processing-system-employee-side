package org.tile.ticketing_system.domein.repository.interfaces;

import java.util.List;

import org.tile.ticketing_system.domein.Ticket;

public interface TicketDao extends GenericDao<Ticket> {
    List<Ticket> findAllByEmployeeId(String employeeId);
}
