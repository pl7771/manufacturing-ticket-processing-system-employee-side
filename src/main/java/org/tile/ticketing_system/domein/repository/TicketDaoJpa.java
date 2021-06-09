package org.tile.ticketing_system.domein.repository;

import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.java.Log;
import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.repository.interfaces.TicketDao;

@Log
public class TicketDaoJpa extends GenericDaoJpa<Ticket> implements TicketDao {
	public TicketDaoJpa() {
		super(Ticket.class);
	}

	@Override
	public List<Ticket> findAllByEmployeeId(String employeeId) {
		try {
			var getTicketsQuery = em.createQuery("SELECT t from Ticket t where t.employeeId = ?1").setParameter(1, employeeId);
			var foundTickets = (List<Ticket>) getTicketsQuery.getResultList();
			log.info("Found tickets");
			return foundTickets;
		} catch (NoResultException e) {
			return List.of();
		}
	}
}
