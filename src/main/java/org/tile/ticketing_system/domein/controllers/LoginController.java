package org.tile.ticketing_system.domein.controllers;

import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.repository.BijlageDaoJpa;
import org.tile.ticketing_system.domein.repository.ClientDaoJpa;
import org.tile.ticketing_system.domein.repository.CompanyDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractTypeDaoJpa;
import org.tile.ticketing_system.domein.repository.EmployeeDaoJpa;
import org.tile.ticketing_system.domein.repository.GenericDaoJpa;
import org.tile.ticketing_system.domein.repository.RoleDaoJpa;
import org.tile.ticketing_system.domein.repository.TicketDaoJpa;
import org.tile.ticketing_system.domein.repository.UserDaoJpa;
import org.tile.ticketing_system.domein.services.ContractService;
import org.tile.ticketing_system.domein.services.TicketService;
import org.tile.ticketing_system.domein.services.UserService;

public class LoginController {

	private final Actemium actemium;

	public LoginController() {
		this.actemium = new Actemium(
				new UserService(new UserDaoJpa(), new RoleDaoJpa(), new ClientDaoJpa(), new EmployeeDaoJpa()),
				new TicketService(new TicketDaoJpa(), new BijlageDaoJpa()),
				new ContractService(new CompanyDaoJpa(), new ContractDaoJpa(), new ContractTypeDaoJpa()));
	}

	public DomainControllerAbstraction login(String emailAddress, String password) {
		return getController(this.actemium.login(emailAddress, password));
	}

	private DomainControllerAbstraction getController(User loggedInUser) {
		this.actemium.refreshLocalData(loggedInUser);

		if (loggedInUser.isAdministrator()) {
			return new AdministratorController(this.actemium, loggedInUser);
		}
		if (loggedInUser.isSupportManager()) {
			return new SupportManagerController(this.actemium, loggedInUser);
		}
		if (loggedInUser.isTechnician()) {
			return new TechnicianController(this.actemium, loggedInUser);
		}
		throw new IllegalArgumentException(
				"Gebruiker heeft geen rechten om deze applicatie te gebruiken");
  	}

}
