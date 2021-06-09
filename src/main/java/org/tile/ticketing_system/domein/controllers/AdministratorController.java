package org.tile.ticketing_system.domein.controllers;

import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;

public class AdministratorController extends DomainControllerAbstraction {

	private final Actemium actemium;

	protected AdministratorController(Actemium actemium, User loggedInUser) {
		super(actemium, loggedInUser);
		this.actemium = super.getActemium();
	}

	@Override
	public void createClient(CreateClientDto createClientDto) {
		this.actemium.addClient(createClientDto);
	}

	@Override
	public void editClient(EditClientDto editClientDto) {
		this.actemium.editClient(editClientDto);
	}

	@Override
	public void createEmployee(CreateEmployeeDto createEmployeeDto) { // address toe te voegen in .NET
		this.actemium.addEmployee(createEmployeeDto);
	}

	@Override
	public void editEmployee(EditEmployeeDto editEmployeeDto) {
		this.actemium.editEmployee(editEmployeeDto);
	}

}
