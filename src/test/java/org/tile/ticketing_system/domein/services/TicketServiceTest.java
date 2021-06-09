package org.tile.ticketing_system.domein.services;

import lombok.extern.java.Log;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.*;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.repository.TicketDaoJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Log
@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketDaoJpa ticketDaoJpa;
    @InjectMocks
    private TicketService ticketService;
    private List<Ticket> tickets;

    public TicketServiceTest(){
        tickets = new ArrayList<>();
        tickets.add(new Ticket(LocalDateTime.now(), "Titel1", "123"));
        tickets.add(new Ticket(LocalDateTime.now(), "Titel2", "456"));
        tickets.add(new Ticket(LocalDateTime.now(), "Titel3", "789"));
    }

    @Test
    void retourneetJuistTicketsVoorBepaaldeUser(){
        tickets.get(0).setEmployeeId("1");
        tickets.get(0).setStatus(Ticket.Status.AFGEHANDELD);
        tickets.get(1).setEmployeeId("1");
        tickets.get(2).setEmployeeId(null);
        User user = new User("testuser@gmail.com","Test","User");
        user.setRole(new Role(Role.RoleType.CLIENT));
        Employee employee = new Employee(user, "address");
        user.setEmployee(employee);
        Mockito.when(ticketDaoJpa.findAll()).thenReturn(tickets);
        List<ITicket> result = ticketService.getAllTicketsFor(user);
        Assertions.assertThat(result).isEqualTo(tickets);
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void editTicket_updatesTicketsRepo(){
        Ticket ticketToUpdate = tickets.get(0);
        EditTicketDto commandToUpdateTicket = new EditTicketDto(1,"Updated Titel", "Updated omschrijving",
                                                            "Updated opmerking", Ticket.Status.AFGEHANDELD, "", null);
        ticketService.editTicket(commandToUpdateTicket, ticketToUpdate);
        Mockito.verify(ticketDaoJpa, Mockito.times(1)).update(ticketToUpdate);
        Assertions.assertThat(ticketToUpdate.getTitel()).isEqualTo(commandToUpdateTicket.getTitel());
    }

    @Test
    public void createTicket_insertsTicketInRepo(){
        Ticket ticketToCreate = tickets.get(0);
        ContractType contractType = new ContractType("naam3", ContractType.ManierAanmakenTicket.APPLICATIE, ContractType.GedekteTijdstippen.ALTIJD, 20, 10, 500);
        Contract contract = new Contract(contractType, Contract.ContractStatus.LOPEND, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        Company company = new Company("CompanyName1", "Address1", "phoneNumber1");
        CreateTicketDto commandToCreateTicket = new CreateTicketDto("New titel ticket", "New Omschrijving",
                                                                            "New opmerking", Ticket.Status.AANGEMAAKT, "Coca-Cola", contract, null, "");

        ticketService.createTicket(commandToCreateTicket, company);
        Mockito.verify(ticketDaoJpa, Mockito.times(1)).insert(ticketToCreate);
    }

}
