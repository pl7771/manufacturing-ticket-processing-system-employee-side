package org.tile.ticketing_system.domein.services;

import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.commitTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.rollbackTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.startTransaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.tile.ticketing_system.domein.Client;
import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Employee;
import org.tile.ticketing_system.domein.Role;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.interfaces.ICompany;
import org.tile.ticketing_system.domein.interfaces.IRole;
import org.tile.ticketing_system.domein.repository.ClientDaoJpa;
import org.tile.ticketing_system.domein.repository.EmployeeDaoJpa;
import org.tile.ticketing_system.domein.repository.RoleDaoJpa;
import org.tile.ticketing_system.domein.repository.UserDaoJpa;

import lombok.extern.java.Log;

@Log
public class UserService {

	private final UserDaoJpa userDaoJpa;
	private final RoleDaoJpa roleDaoJpa;
	private final ClientDaoJpa clientDaoJpa;
	private final EmployeeDaoJpa employeeDaoJpa;

	public UserService(UserDaoJpa userDaoJpa, RoleDaoJpa roleDaoJpa, ClientDaoJpa clientDaoJpa,
			EmployeeDaoJpa employeeDaoJpa) {
		this.userDaoJpa = userDaoJpa;
		this.roleDaoJpa = roleDaoJpa;
		this.clientDaoJpa = clientDaoJpa;
		this.employeeDaoJpa = employeeDaoJpa;
	}

	public User login(String emailAddress, String password) {
		startTransaction();
		log.info("Logging in user " + emailAddress);
		var foundUser = userDaoJpa.findByEmailAddress(emailAddress).filter(user -> user.validatePassword(password));
		commitTransaction();

		return foundUser.orElseThrow(() -> new IllegalArgumentException("Ongeldig loginnaam, wachtwoord of u bent niet actief!"));
	}

	public List<IRole> getAllRoles() {
		startTransaction();
		var allRoles = this.roleDaoJpa.findAll();
		List<IRole> allRolesPerInterface = new ArrayList<>();

		for (Role role : allRoles) {
			var iRole = (IRole) (Object) role;
			allRolesPerInterface.add(iRole);
		}

		commitTransaction();
		return allRolesPerInterface;
	}

	public <T> List<T> getAllUsersOfType(UserType userType) throws EntityNotFoundException {
		startTransaction();
		List<T> required;
		switch (userType) {
		case USER:
			required = (List<T>) this.userDaoJpa.findAll();
			break;
		case CLIENT:
			required = (List<T>) this.clientDaoJpa.findAll();
			break;
		case EMPLOYEE:
			required = (List<T>) this.employeeDaoJpa.findAll();
			break;
		default:
			rollbackTransaction();
			throw new IllegalArgumentException("UserType moet ingevuld zijn!");
		}
		commitTransaction();
		return required;
	}

	public Client createClient(CreateClientDto dto, Company company, Role role) {
		// user maken
		var user = new User(dto.getEmail(), dto.getFirstName(), dto.getLastName());
		user.setRole(role);
		// client maken (link company en user)
		var client = new Client(user, company);
		// client persisteren
		try {
			startTransaction();
			this.clientDaoJpa.insert(client);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw new IllegalArgumentException(String.format("%s bestaat al", client.getApplicationUser().getEmail()));
		}
		return client;
	}

	public void editClient(EditClientDto dto, Company company, Client client) {
		client.editClient(company, dto);
		try {
			startTransaction();
			this.clientDaoJpa.update(client);
			commitTransaction();
		} catch (Exception exception) {
			rollbackTransaction();
		}
		log.info("Client info after updated changes: " + client);

	}

	public Employee createEmployee(CreateEmployeeDto dto, Role role) { // address toe te voegen in .NET
		log.warning("Attempting to create Employee with following properties: " + dto.toString() + " Role: "
				+ role.getName());

		// user maken
		var user = new User(dto.getEmail(), dto.getFirstName(), dto.getLastName(), dto.getPhoneNumber(), true);
		// role toevoegen
		user.setRole(role);
		// employee maken
		var employee = new Employee(user, dto.getAddress());
		// employee persisteren
		try {
			startTransaction();
			employeeDaoJpa.insert(employee);
			commitTransaction();
		} catch (Exception exception) {
			rollbackTransaction();
		}
		log.warning("New Employee created by UserService: " + employee.toString());

		return employee;
	}

	public void editEmployee(EditEmployeeDto dto, Employee employee) {

		employee.editEmployee(dto);

		// persisteer wijzigingen

		try {
			startTransaction();
			this.employeeDaoJpa.update(employee);
			commitTransaction();
		} catch (Exception exception) {
			rollbackTransaction();
		}

	}
	
	public void updateLastSession(User loggedInUser) {
		loggedInUser.setLastSession(LocalDateTime.now());
		try {
			startTransaction();
			this.userDaoJpa.update(loggedInUser);
			commitTransaction();
		} catch (Exception exception) {
			rollbackTransaction();
		}
		log.info("User info after updated changes: " + loggedInUser);
	}

	public enum UserType {
		USER, CLIENT, EMPLOYEE
	}
}
