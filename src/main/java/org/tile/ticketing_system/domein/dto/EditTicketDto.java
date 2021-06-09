package org.tile.ticketing_system.domein.dto;

import lombok.Value;
import org.tile.ticketing_system.domein.Bijlage;
import org.tile.ticketing_system.domein.Ticket;

@Value
public class EditTicketDto {
    int ticketId;
    String titel;
    String omschrijving;
    String opmerkingen;
    Ticket.Status status;
    String bijlageDescription;
    Bijlage bijlage;
}
