package org.tile.ticketing_system.domein.services;

import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.commitTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.rollbackTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.startTransaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;
import org.tile.ticketing_system.domein.interfaces.ICompany;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.repository.BijlageDaoJpa;
import org.tile.ticketing_system.domein.repository.TicketDaoJpa;

import lombok.extern.java.Log;

@Log
public class TicketService {

    private final TicketDaoJpa ticketDaoJpa;
    private final BijlageDaoJpa bijlageDaoJpa;

    public TicketService(TicketDaoJpa ticketDaoJpa, BijlageDaoJpa bijlageDaoJpa) {
        this.ticketDaoJpa = ticketDaoJpa;
        this.bijlageDaoJpa = bijlageDaoJpa;
    }

    public List<ITicket> getAllTicketsFor(User loggedInUser) {
        startTransaction();
        List<Ticket> allTickets;
        List<ITicket> allTicketsPerInterface = new ArrayList<>();
        if (loggedInUser.isTechnician()) {
            allTickets = this.ticketDaoJpa.findAllByEmployeeId(loggedInUser.getEmployee().getEmployeeId());
  
        } else {
            allTickets = this.ticketDaoJpa.findAll();
        }
        
        
        for (Ticket ticket : allTickets) {
        	var iticket = (ITicket) (Object) ticket;
        	allTicketsPerInterface.add(iticket);
        }
        
        commitTransaction();
        return allTicketsPerInterface;
    }

    public void editTicket(EditTicketDto command, Ticket ticketToEdit) {
    	Ticket ticketEdit = ticketToEdit;
        ticketEdit.editTicket(command.getTitel(), command.getOmschrijving(), command.getOpmerkingen(), command.getStatus(),
                command.getBijlageDescription(), (command.getBijlage()  == null || command.getBijlage().getDataFiles() == null) ? ticketToEdit.getBijlage() : command.getBijlage());
        try {
            if (ticketEdit.bijlage != null) {
                startTransaction();
                this.bijlageDaoJpa.insert(ticketEdit.bijlage);
                commitTransaction();
            }
            startTransaction();
            this.ticketDaoJpa.update(ticketEdit);
            commitTransaction();
        } catch (Exception exception) {
            rollbackTransaction();
        }
    }

    public Ticket createTicket(CreateTicketDto dto, Company company) {

        var ticket = new Ticket(LocalDateTime.now(), dto.getTitel(), dto.getOmschrijving(),
                dto.getStatus());
        ticket.setOpmerkingen(dto.getOpmerkingen());
        ticket.setCompany(company);
        ticket.setContract(null);
        ticket.setBijlage(dto.getBijlage());
        ticket.setImageDescription(dto.getImageDescription());
        ticket.setDatumGewijzigd(LocalDateTime.now());
        try {
            if (ticket.bijlage != null) {
                startTransaction();
                this.bijlageDaoJpa.insert(ticket.bijlage);
                commitTransaction();
            }
            startTransaction();
            this.ticketDaoJpa.insert(ticket);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
        }
        return ticket;
    }
}
