package org.tile.ticketing_system.domein.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;

import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.Client;
import org.tile.ticketing_system.domein.Employee;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateClientDto;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.CreateEmployeeDto;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;
import org.tile.ticketing_system.domein.interfaces.IClient;
import org.tile.ticketing_system.domein.interfaces.ICompany;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.domein.interfaces.IEmployee;
import org.tile.ticketing_system.domein.interfaces.IRole;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.interfaces.IUser;

public abstract class DomainControllerAbstraction {

	private static final String U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN = "U hebt geen rechten om deze handeling uit te voeren!";

	private final Actemium actemium;
	private final User loggedInUser;

	protected DomainControllerAbstraction(Actemium actemium, User loggedInUser) {
		this.actemium = actemium;
		this.loggedInUser = loggedInUser;
	}

	public User getLoggedInUser() {
		return this.loggedInUser;
	}

	protected Actemium getActemium() {
		return this.actemium;
	}

	// Ophalen van lokale items uit Actemium
	public List<String> getAllCompanyNames() {
		return this.actemium.getCompanies().stream().map(ICompany::getName).collect(Collectors.toList());
	}

	public ObservableList<ITicket> getAllTickets() {
		return actemium.getFilteredTicketList();
	}

	public ObservableList<IContract> getAllContracts() {
		return actemium.getContracts();
	}

	public ObservableList<IUser> getFilteredUserList() {
		return actemium.getFilteredUserList();
	}

	public ObservableList<IRole> getAllRoles() {
		return actemium.getFilteredRoleList();
	}

	public ObservableList<IContractType> getAllContractTypes() {
		return actemium.getContractTypes();
	}

	public IEmployee getEmployeeByApplicationUserId(String id) {
		return actemium.findEmployee(id);
	}

	public IClient getClientByApplicationUserId(String id) {
		return actemium.findClient(id);
	}

	// ----------------- ADMINISTRATOR methoden -----------------

	public void createClient(CreateClientDto createClientDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public void editClient(EditClientDto editClientDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public void createEmployee(CreateEmployeeDto createEmployeeDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public void editEmployee(EditEmployeeDto editEmployeeDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	// ----------------- SUPPORTMANAGER methoden -----------------

	public int getActiveContractCountByContractType(IContractType iContractType) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public int getCompletedTicketsCountPerContractType(IContractType type) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);

	}
	
	public int getPercentageTicketsCompletedInTimePerContractType(IContractType type) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);

	}

	public void createContractType(CreateContractTypeDto createContractTypeDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public void editContractType(EditContractTypeDto editContractTypeDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Long> getStatStatusTicket(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Long> getStatContractTypeTicket(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Double> getStatGemiddeldeLooptijd(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Integer> getStatTicketsAfgewerktPerMaand(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Integer> getStatTicketsAangemaaktPerMaand(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map <String, Integer> getStatCompletedInTimePercentageContractTypes() {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map <String, Integer> getStatPercentageTicketsCompletedInTimePerMonth() {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Double> getStatTicketsGemiddeldeOpenstaandPerMaand(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, Double> getStatContractenGemiddeldeLopendPerMaand(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, String> getKPITicketsAfgewerktBinnenTijd(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, String> getKPITicketsOpenstaandPerDag(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public Map<String, String> getKPIContractenLopendePerDag(){
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	// ----------------- SUPPORTMANAGER/TECHNICIAN methoden -----------------

	public void createTicket(CreateTicketDto createTicketDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	public void editTicket(EditTicketDto editTicketDto) {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public void updateLastSession() {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}
	
	public long getTicketWijzigingen() {
		throw new UnsupportedOperationException(U_HEBT_GEEN_RECHTEN_OM_DEZE_HANDELING_UIT_TE_VOEREN);
	}

	// ----------------- filter methodes -----------------
	// - Users
	public void changeFilter_User(String status, String role, String firstName, String userName)  {
		this.actemium.changeFilter_User(status, role, firstName, userName);
	}

	// - Tickets
	public void changeFilter_Ticket_Status(String filterValue) {
		this.actemium.changeFilter_Ticket_Status(filterValue);
	}

	// -Roles
	public void changeFilter_Role_WithoutClient(boolean withoutClient) {
		this.actemium.changeFilter_Role_WithoutClient(withoutClient);
	}
}
