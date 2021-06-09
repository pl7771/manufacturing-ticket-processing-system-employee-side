package org.tile.ticketing_system.domein.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.tile.ticketing_system.domein.DtoMother.aCreateContractTypeDto;
import static org.tile.ticketing_system.domein.DtoMother.aCreateTicketDto;
import static org.tile.ticketing_system.domein.DtoMother.aEditTicketDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditContractTypeDto;

@ExtendWith(MockitoExtension.class)
class SupportManagerControllerTest {

    @Mock
    private Actemium actemium;

    @InjectMocks
    private SupportManagerController supportManagerController;

    @Test
    void shouldCreateTicket() {
        var createTicketDto = aCreateTicketDto();

        this.supportManagerController.createTicket(createTicketDto);

        verify(this.actemium).addTicket(createTicketDto);
    }

    @Test
    void shouldEditTicket() {
        var editTicketDto = aEditTicketDto();

        this.supportManagerController.editTicket(editTicketDto);

        verify(this.actemium).editTicket(editTicketDto);
    }

    @Test
    void shouldGetActiveContractCountByContractType() {
        var contractType = new ContractType();

        this.supportManagerController.getActiveContractCountByContractType(contractType);

        verify(this.actemium).getActiveContractCountByContractType(contractType);
    }

    @Test
    void shouldGetCompletedTicketsCountPerContractType() {
        var contractType = new ContractType();

        this.supportManagerController.getCompletedTicketsCountPerContractType(contractType);

        verify(this.actemium).getCompletedTicketsCountPerContractType(contractType);
    }

    @Test
    void shouldGetPercentageTicketsCompletedInTimePerContractType() {
        var contractType = new ContractType();

        this.supportManagerController.getPercentageTicketsCompletedInTimePerContractType(contractType);

        verify(this.actemium).getPercentageTicketsCompletedInTimePerContractType(contractType);
    }

    @Test
    void shouldEditContractType() {
        var editContractTypeDto = anEditContractTypeDto();

        this.supportManagerController.editContractType(editContractTypeDto);

        verify(this.actemium).editContractType(editContractTypeDto);
    }

    @Test
    void shouldCreateContractType() {
        var createContractTypeDto = aCreateContractTypeDto();

        this.supportManagerController.createContractType(createContractTypeDto);

        verify(this.actemium).addContractType(createContractTypeDto);
    }

    @Test
    void shouldThrowExceptionForUnsupportedOperations() {
        assertThatThrownBy(() -> this.supportManagerController.createClient(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.supportManagerController.editClient(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.supportManagerController.createEmployee(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.supportManagerController.editEmployee(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");
    }
}
