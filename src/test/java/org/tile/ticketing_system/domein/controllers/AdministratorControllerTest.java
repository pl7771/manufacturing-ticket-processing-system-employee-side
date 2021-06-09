package org.tile.ticketing_system.domein.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.UserMother;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.tile.ticketing_system.domein.DtoMother.aCreateClientDto;
import static org.tile.ticketing_system.domein.DtoMother.aCreateEmployeeAdminDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditClientDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditEmployeeDto;
import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;

@ExtendWith(MockitoExtension.class)
class AdministratorControllerTest {

    @Mock
    private Actemium actemium;

    private AdministratorController administratorController;

    @BeforeEach
    void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        User loggedInUser = UserMother.aUserWithRole(ADMINISTRATOR);
        this.administratorController = new AdministratorController(this.actemium, loggedInUser);
    }

    @Test
    void shouldCreateClient() {
        var createClientDto = aCreateClientDto();

        this.administratorController.createClient(createClientDto);

        verify(this.actemium).addClient(createClientDto);
    }

    @Test
    void shouldEditClient() {
        var editClientDto = anEditClientDto();

        this.administratorController.editClient(editClientDto);

        verify(this.actemium).editClient(editClientDto);
    }

    @Test
    void shouldCreateEmployee() {
        var createEmployeeDto = aCreateEmployeeAdminDto();

        this.administratorController.createEmployee(createEmployeeDto);

        verify(this.actemium).addEmployee(createEmployeeDto);
    }

    @Test
    void shouldEditEmployee() {
        var editEmployeeDto = anEditEmployeeDto();

        this.administratorController.editEmployee(editEmployeeDto);

        verify(this.actemium).editEmployee(editEmployeeDto);
    }

    @Test
    void shouldThrowExceptionForUnsupportedOperations() {
        assertThatThrownBy(() -> this.administratorController.getActiveContractCountByContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.administratorController.getCompletedTicketsCountPerContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.administratorController.getPercentageTicketsCompletedInTimePerContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.administratorController.createContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.administratorController.editContractType(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

        assertThatThrownBy(() -> this.administratorController.editTicket(null))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("U hebt geen rechten om deze handeling uit te voeren!");

    }
}
