package org.tile.ticketing_system.domein.interfaces;

import org.tile.ticketing_system.domein.Bijlage;
import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.Ticket;

import java.time.LocalDateTime;

public interface ITicket {
    Company getCompany();

    Contract getContract();

    int getTicketId();

    String getTitel();

    String getOmschrijving();

    String getOpmerkingen();

    boolean isGewijzigd();

    LocalDateTime getDatumAangemaakt();

    LocalDateTime getDatumAfgesloten();

    LocalDateTime getDatumGewijzigd();

    Ticket.Status getStatus();

    int getAantalWijzigingen();

    String getImageDescription();

    Bijlage getBijlage();

    boolean kanWijzigen();

    String getEmployeeId();
}
