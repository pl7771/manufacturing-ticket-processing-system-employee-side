package org.tile.ticketing_system.domein.services;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Client;
import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Employee;
import org.tile.ticketing_system.domein.Role;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.repository.ClientDaoJpa;
import org.tile.ticketing_system.domein.repository.EmployeeDaoJpa;
import org.tile.ticketing_system.domein.repository.RoleDaoJpa;
import org.tile.ticketing_system.domein.repository.UserDaoJpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.tile.ticketing_system.domein.UserMother.aUserWithPassword;
import static org.tile.ticketing_system.domein.UserMother.aUserWithRole;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDaoJpa userDaoJpa;

    @Mock
    private ClientDaoJpa clientDaoJpa;

    @Mock
    private EmployeeDaoJpa employeeDaoJpa;

    @Mock
    private RoleDaoJpa roleDaoJpa;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldLoginValidUserAndGetLoggedInUser() throws NoSuchFieldException, IllegalAccessException {
        when(this.userDaoJpa.findByEmailAddress("aUserName@gmail.com"))
                .thenReturn(Optional.of(aUserWithPassword("123")));

        User loggedInUser = this.userService.login("aUserName@gmail.com", "123");

        assertEquals("aUserName@gmail.com", loggedInUser.getUserName());
        assertEquals("Jan", loggedInUser.getFirstName());
        assertEquals("Janssens", loggedInUser.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenPasswordInvalid() throws NoSuchFieldException, IllegalAccessException {
        when(this.userDaoJpa.findByEmailAddress("aUserName@gmail.com"))
                .thenReturn(Optional.of(aUserWithPassword("123")));

        assertThrows(IllegalArgumentException.class, () -> this.userService.login("aUserName@gmail.com", "WRONG"));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(this.userDaoJpa.findByEmailAddress(any()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> this.userService.login("aUserName@gmail.com", "123"));
    }

    @Test
    void shouldGetAllRoles() {
        this.userService.getAllRoles();
        verify(this.roleDaoJpa).findAll();
    }

    @Test
    void shouldGetAllUsersOfTypeUser() {
        this.userService.getAllUsersOfType(UserService.UserType.USER);
        verify(this.userDaoJpa).findAll();
    }

    @Test
    void shouldGetAllUsersOfTypeClient() {
        this.userService.getAllUsersOfType(UserService.UserType.CLIENT);
        verify(this.clientDaoJpa).findAll();
    }

    @Test
    void shouldGetAllUsersOfTypeEmployee() {
        this.userService.getAllUsersOfType(UserService.UserType.EMPLOYEE);
        verify(this.employeeDaoJpa).findAll();
    }

    @Test
    void shouldCreateAClient() {
        CreateClientDto command = new CreateClientDto("COCA", "Jan", "Janssens", "jan@jansens.jan");
        Company company = new Company("COCA", "JANLAAN 8", "12345678");
        Role role = new Role(Role.RoleType.CLIENT);

        Client createdClient = this.userService.createClient(command, company, role);

        verify(this.clientDaoJpa).insert(refEq(createdClient));

        assertThat(createdClient.getApplicationUser().isKlant()).isTrue();
        assertThat(createdClient.getApplicationUser().getFirstName()).isEqualTo("Jan");
        assertThat(createdClient.getApplicationUser().getLastName()).isEqualTo("Janssens");
        assertThat(createdClient.getApplicationUser().getEmail()).isEqualTo("jan@jansens.jan");
        assertThat(createdClient.getCompany().getName()).isEqualTo("COCA");
    }

    @Test
    void shouldEditClient() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(Role.RoleType.CLIENT);
        Company company = new Company("COCA", "JANLAAN 8", "12345678");

        Client clientToEdit = new Client(user, company);

        EditClientDto editClientDto = new EditClientDto("1", "COCA", "nieuweVoornaam", "nieuweAchternaam", "nieuweEmail@mail.com", User.StatusGebruiker.ACTIEF);
        this.userService.editClient(editClientDto, company, clientToEdit);

        assertThat(clientToEdit.getApplicationUser().getFirstName()).isEqualTo("nieuweVoornaam");
        assertThat(clientToEdit.getApplicationUser().getLastName()).isEqualTo("nieuweAchternaam");
        assertThat(clientToEdit.getApplicationUser().getEmail()).isEqualTo("nieuweEmail@mail.com");
        assertThat(clientToEdit.getApplicationUser().getStatusGebruiker()).isEqualTo("ACTIEF");

        verify(this.clientDaoJpa).update(clientToEdit);
    }

    @Test
    void shouldCreateAnEmployee() {
        CreateEmployeeDto command = new CreateEmployeeDto("Jan", "Janssens", "jan@jansens.jan", "0478888888", "addressLaan", "Technician");
        Role role = new Role(Role.RoleType.TECHNICIAN);

        Employee createdEmployee = this.userService.createEmployee(command, role);

        verify(this.employeeDaoJpa).insert(refEq(createdEmployee));

        assertThat(createdEmployee.getApplicationUser().isTechnician()).isTrue();
        assertThat(createdEmployee.getApplicationUser().getFirstName()).isEqualTo("Jan");
        assertThat(createdEmployee.getApplicationUser().getLastName()).isEqualTo("Janssens");
        assertThat(createdEmployee.getApplicationUser().getEmail()).isEqualTo("jan@jansens.jan");
    }

    @Test
    void shouldEditEmployee() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(Role.RoleType.TECHNICIAN);
        Role administratorRole = new Role(Role.RoleType.ADMINISTRATOR);

        Employee employeeToEdit = new Employee(user, "oudAddress");

        EditEmployeeDto editEmployeeDto = new EditEmployeeDto("1", "nieuweVoornaam", "nieuweAchternaam", "nieuweEmail@mail.com", "0479999999", administratorRole, "nieuwAddress", User.StatusGebruiker.ACTIEF);
        this.userService.editEmployee(editEmployeeDto, employeeToEdit);

        assertThat(employeeToEdit.getApplicationUser().getFirstName()).isEqualTo("nieuweVoornaam");
        assertThat(employeeToEdit.getApplicationUser().getLastName()).isEqualTo("nieuweAchternaam");
        assertThat(employeeToEdit.getApplicationUser().getEmail()).isEqualTo("nieuweEmail@mail.com");
        assertThat(employeeToEdit.getAddress()).isEqualTo("nieuwAddress");
        assertThat(employeeToEdit.getApplicationUser().getStatusGebruiker()).isEqualTo("ACTIEF");

        verify(this.employeeDaoJpa).update(employeeToEdit);
    }
}
