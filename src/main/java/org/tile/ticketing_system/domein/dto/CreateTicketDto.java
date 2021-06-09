package org.tile.ticketing_system.domein.dto;

import lombok.Value;
import org.tile.ticketing_system.domein.Bijlage;
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.Ticket;

@Value
public class CreateTicketDto {
    String titel;
    String omschrijving;
    String opmerkingen;
    Ticket.Status status;
    String company;
    Contract contract;
    Bijlage bijlage;
    String imageDescription;
}
