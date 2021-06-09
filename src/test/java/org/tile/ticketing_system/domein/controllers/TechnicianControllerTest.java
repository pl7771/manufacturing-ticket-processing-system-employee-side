package org.tile.ticketing_system.domein.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Actemium;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.tile.ticketing_system.domein.DtoMother.aCreateTicketDto;
import static org.tile.ticketing_system.domein.DtoMother.aEditTicketDto;

@ExtendWith(MockitoExtension.class)
class TechnicianControllerTest {

    @Mock
    private Actemium actemium;

    @InjectMocks
    private TechnicianController technicianController;

    @Test
    void shouldCreateTicket() {
        var createTicketDto = aCreateTicketDto();

        this.technicianController.createTicket(createTicketDto);

        verify(this.actemium).addTicket(createTicketDto);
    }

    @Test
    void shouldEditTicket() {
        var editTicketDto = aEditTicketDto();

        this.technicianController.editTicket(editTicketDto);

        verify(this.actemium).editTicket(editTicketDto);
    }

    @Test
    void shouldThrowExceptionForUnsupportedOperations() {
        assertThatThrownBy(() -> this.technicianController.getActiveContractCountByContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.getCompletedTicketsCountPerContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.getPercentageTicketsCompletedInTimePerContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.createContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.editContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.createClient(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.editClient(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.createEmployee(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.technicianController.editEmployee(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

    }
}
