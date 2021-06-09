package org.tile.ticketing_system.domein;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

import com.amdelamar.jhash.Hash;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;
import static org.tile.ticketing_system.domein.Role.RoleType.CLIENT;
import static org.tile.ticketing_system.domein.Role.RoleType.SUPPORTMANAGER;
import static org.tile.ticketing_system.domein.Role.RoleType.TECHNICIAN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TicketMother {

    public static Ticket aTicketWithId(int id) throws NoSuchFieldException, IllegalAccessException {
        Ticket ticket = new Ticket(LocalDateTime.now(), "eenTicket", "eenOmschrijving");

        Field ticketIdField = ticket.getClass().getDeclaredField("ticketId");
        ticketIdField.setAccessible(true);
        ticketIdField.set(ticket, id);
        return ticket;
    }
}
