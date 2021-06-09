package org.tile.ticketing_system.domein;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;

import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DtoMother {

    public static CreateTicketDto aCreateTicketDto() {
        return new CreateTicketDto("eenTitel", "eenOmschrijving", "eenOpmerking", Ticket.Status.AANGEMAAKT, "Coca", null, null, "eenOmschrijving");
    }

    public static EditTicketDto aEditTicketDto() {
        return new EditTicketDto(1, "eenTitel", "eenOmschrijving", "eenOpmerking", Ticket.Status.IN_BEHANDELING, "eenBijlageDiscription", null);
    }

    public static CreateEmployeeDto aCreateEmployeeAdminDto() {
        return new CreateEmployeeDto("newFirstName", "newLastName", "newEmailAdress@gmail.com", "0488553344", "someAdressLaan", "Administrator");
    }

    public static EditEmployeeDto anEditEmployeeDto() {
        return new EditEmployeeDto("testId", "newFirstName", "newLastName", "newEmailAdress@gmail.com", "0488553344", new Role(ADMINISTRATOR), "someAdressLaan", User.StatusGebruiker.ACTIEF);
    }

    public static CreateClientDto aCreateClientDto() {
        return new CreateClientDto("Coca", "newFirstname", "newLastname", "newEmailAddress@gmail.com");
    }

    public static EditClientDto anEditClientDto() {
        return new EditClientDto("testId", "Coca", "newFirstname", "newLastName", "newEmailAdress@gmail.com", User.StatusGebruiker.ACTIEF);
    }

    public static CreateContractTypeDto aCreateContractTypeDto() {
        return new CreateContractTypeDto("eenNaam", 20, 20, 20, ContractType.ManierAanmakenTicket.EMAIL, ContractType.GedekteTijdstippen.ALTIJD, ContractType.ContractTypeStatus.ACTIEF);
    }

    public static EditContractTypeDto anEditContractTypeDto() {
        return new EditContractTypeDto(1, "eenNaam", 20, 20, 20, ContractType.ManierAanmakenTicket.EMAIL, ContractType.GedekteTijdstippen.ALTIJD, ContractType.ContractTypeStatus.ACTIEF);
    }
}
