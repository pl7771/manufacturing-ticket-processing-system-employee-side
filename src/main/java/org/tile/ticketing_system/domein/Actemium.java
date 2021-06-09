package org.tile.ticketing_system.domein;

import java.text.DecimalFormat;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import lombok.Getter;
import lombok.extern.java.Log;
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
import org.tile.ticketing_system.domein.services.ContractService;
import org.tile.ticketing_system.domein.services.TicketService;
import org.tile.ticketing_system.domein.services.UserService;

import static org.tile.ticketing_system.domein.services.UserService.UserType.CLIENT;
import static org.tile.ticketing_system.domein.services.UserService.UserType.EMPLOYEE;
import static org.tile.ticketing_system.domein.services.UserService.UserType.USER;

@Log
@Getter
public class Actemium {

	// -------------------------------PROPERTIES--------------------------------------
	private final UserService userService;
	private final TicketService ticketService;
	private final ContractService contractService;
	private User loggedInUser;

	// Lijsten die gefilterd zijn op basis van gebruikersRol - Setten net na
	// inloggen?
	private ObservableList<IUser> users = FXCollections.observableArrayList();
	private ObservableList<IRole> roles = FXCollections.observableArrayList();
	private ObservableList<IClient> clients = FXCollections.observableArrayList();
	private ObservableList<IEmployee> employees = FXCollections.observableArrayList();
	private ObservableList<ITicket> tickets = FXCollections.observableArrayList();
	private ObservableList<ICompany> companies = FXCollections.observableArrayList();
	private ObservableList<IContract> contracts = FXCollections.observableArrayList();
	private ObservableList<IContractType> contractTypes = FXCollections.observableArrayList();

	private FilteredList<IUser> filteredUserList;
	private FilteredList<ITicket> filteredTicketList;
	private FilteredList<IRole> filteredRoleList;
	
	private Clock clock; //voor kpi's

	// -------------------------------CONSTRUCTOR------------------------------------
	public Actemium(UserService userService, TicketService ticketService, ContractService contractService) {
		this.userService = userService;
		this.ticketService = ticketService;
		this.contractService = contractService;
		this.clock = Clock.system(ZoneId.systemDefault());
	}

	public User login(String emailAddress, String password) {
		this.loggedInUser = this.userService.login(emailAddress, password);
		return this.loggedInUser;
	}

	public void refreshLocalData(User loggedInUser) {
		setAllUsers();
		setRoles();
		setAllCompanies();
		setEmployees();
		setClients();
		setTickets(loggedInUser);
		setContracts();
		setContractTypes();
	}

	public IEmployee findEmployee(String id) {
		return employees.stream().filter(e -> e.getApplicationUser().getId().equals(id)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Geen employee met ApplicationUserId " + id + " gevonden."));
	}
	
	public void addEmployee(CreateEmployeeDto dto) {
		var role = (Role) this.findRole(dto.getRole());
		var newEmployee = this.userService.createEmployee(dto, role);
		this.users.add(newEmployee.getApplicationUser());
		this.employees.add(newEmployee);
		this.filteredUserList = new FilteredList<>(this.users, null);
	}

	public void editEmployee(EditEmployeeDto editEmployeeDto) {
		var employeeToEdit = (Employee) findEmployee(editEmployeeDto.getUserId());
		this.userService.editEmployee(editEmployeeDto, employeeToEdit);
	}

	public IClient findClient(String id) {
		return clients.stream().filter(c -> c.getApplicationUser().getId().equals(id)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Geen client met ApplicationUserId " + id + " gevonden."));
	}
	
	public void addClient(CreateClientDto dto) {
		var foundCompany = (Company) findCompany(dto.getCompany());
		var role = (Role) this.findRole("Client");
		var newClient = this.userService.createClient(dto, foundCompany, role);
		this.users.add(newClient.getApplicationUser());
		this.clients.add(newClient);
		this.filteredUserList = new FilteredList<>(this.users, null);
	}

	public void editClient(EditClientDto dto) {
		var foundCompany = (Company) findCompany(dto.getCompanyName());
		var clientToEdit = (Client) findClient(dto.getUserId());
		this.userService.editClient(dto, foundCompany, clientToEdit);
	}

	public void updateLastSession() {
		userService.updateLastSession(loggedInUser);
	}
	
	public long getTicketWijzigingen() {
		System.out.printf("last session: %s", this.loggedInUser.getLastSession());
		System.out.printf("max date ticket gewijzigd: %s", tickets.stream().map(t -> t.getDatumGewijzigd()).max(Comparator.naturalOrder()));
		
		return this.tickets.stream().filter(t -> {
			return t.getDatumGewijzigd().compareTo(this.loggedInUser.getLastSession()) > 0;
		}).count();
	}
	
	public ITicket findTicket(int id) {
		return this.tickets.stream().filter(e -> e.getTicketId() == id).findFirst().orElseThrow(
				() -> new IllegalArgumentException("Geen ticket met id: " + id + " gevonden."));
	}

	public IContractType findContractTypeById(int id) {
		return this.contractTypes.stream().filter(e -> e.getContractTypeId() == id).findFirst().orElseThrow(
				() -> new IllegalArgumentException("Geen contract type met id: " + id + "gevonden."));
	}
	
	public void addTicket(CreateTicketDto dto) {
		var foundCompany = (Company) findCompany(dto.getCompany());
		log.warning(">>>>>>> Dto create ticket in Actemium: " + dto);
		var newTicket = this.ticketService.createTicket(dto, foundCompany);
		this.tickets.add((ITicket)newTicket);
	}

	public void editTicket(EditTicketDto editTicketDto) {
		var ticketToEdit = (Ticket) findTicket(editTicketDto.getTicketId());
		this.ticketService.editTicket(editTicketDto, ticketToEdit);
	}

	public void addContractType(CreateContractTypeDto dto){
		var contractType = this.contractService.createContractType(dto);
		this.contractTypes.add(contractType);
	}

	public void editContractType(EditContractTypeDto editContractTypeDto) {
		var contractTypeToEdit = (ContractType) findContractTypeById(editContractTypeDto.getContractTypeId());
		this.contractService.editContractType(editContractTypeDto, contractTypeToEdit);
	}

	public ICompany findCompany(String companyName) {
		String fout  = companyName == null ? "Bedrijf is niet gekozen" : "Geen company met naam " + companyName + " gevonden.";
		return this.companies.stream().filter(company -> company.getName().equals(companyName)).findAny()
				.orElseThrow(() -> new IllegalArgumentException(fout));
	}

	public IRole findRole(String roleName) {
		if (roleName == null || roleName.isBlank()) {
			throw new IllegalArgumentException("Role is niet gekozen");
		}
		return roles.stream().filter(role -> role.getName().equals(roleName)).findAny()
				.orElseThrow(() -> new IllegalArgumentException("Geen role met naam " + roleName + "gevonden."));
	}

	// filters
	// - Users
	public void changeFilter_User(String status, String role, String firstName, String userName) {
		Predicate<IUser> userStatus = user -> {
			// If filter text is empty, display all users.
			if (status == null || status.isBlank()) {
				return true;
			}
			String lowerCaseValue = status.toLowerCase();
			return user.getStatusGebruiker() != null && user.getStatusGebruiker().toLowerCase().equals(lowerCaseValue);
		};
		Predicate<IUser> userRole = user -> {
			// If filter text is empty, display all users.
			if (role == null || role.isBlank()) {
				return true;
			}
			String lowerCaseValue = role.toLowerCase();

			if (user.getUserRoles().isEmpty())
				return false;
			String roleOfUser = user.getRole().getName().toLowerCase();
			if (role.equals("employee")) {
				return roleOfUser.equals("technician") || roleOfUser.equals("administrator") || roleOfUser.equals("supportmanager");
			}
			return roleOfUser.equals(lowerCaseValue);
		};
		Predicate<IUser> userFirstName = user -> {
			// If filter text is empty, display all users.
			if (firstName == null || firstName.isBlank()) {
				return true;
			}
			String lowerCaseValue = firstName.toLowerCase();

			return user.getFirstName().toLowerCase().contains(lowerCaseValue) || user.getLastName().toLowerCase().contains(lowerCaseValue);
		};
		Predicate<IUser> userUserName = user -> {
			// If filter text is empty, display all users.
			if (userName == null || userName.isBlank()) {
				return true;
			}
			String lowerCaseValue = userName.toLowerCase();

			return user.getUserName().toLowerCase().contains(lowerCaseValue);
		};
		
		filteredUserList.setPredicate(userStatus.and(userRole).and(userFirstName).and(userUserName));
	}

	// - Tickets
	public void changeFilter_Ticket_Status(String filterValue) {
		filteredTicketList.setPredicate(ticket -> {
			// If filter text is empty, display all users.
			if (filterValue == null || filterValue.isBlank()) {
				return true;
			}
			String status = ticket.getStatus().name();
			if(filterValue.equals("OPENSTAAND"))
				return status.equals("AANGEMAAKT") || status.equals("IN_BEHANDELING");
			return status.equals(filterValue);
		});
	}
	// -Roles

	public void changeFilter_Role_WithoutClient(boolean withoutClient) {
		filteredRoleList.setPredicate(role -> {
			if (withoutClient) {
				return !(role.getName().equalsIgnoreCase("Client"));
			}
			return true;
		});
	}

	// SETTERS
	private void setAllCompanies() {
		this.companies = FXCollections.observableArrayList(this.contractService.getAllCompanies());
	}

	private void setAllUsers() {
		this.users = FXCollections.observableArrayList(this.userService.getAllUsersOfType(USER));
		this.filteredUserList = new FilteredList<>(this.users, null);
	}

	private void setEmployees() {
		this.employees = FXCollections.observableArrayList(userService.getAllUsersOfType(EMPLOYEE));
	}

	private void setClients() {
		this.clients = FXCollections.observableArrayList(userService.getAllUsersOfType(CLIENT));
	}

	private void setRoles() {
		this.roles = FXCollections.observableArrayList(this.userService.getAllRoles());
		this.filteredRoleList = new FilteredList<IRole>(this.roles, r -> true);
	}

	private void setTickets(User loggedInUser) {
		this.tickets = FXCollections.observableArrayList(ticketService.getAllTicketsFor(loggedInUser));
		this.filteredTicketList = new FilteredList<ITicket>(this.tickets, t -> true);
	}

	private void setContracts() {
		this.contracts = FXCollections.observableArrayList(this.contractService.getAllContracts());
	}

	private void setContractTypes() {
		this.contractTypes = FXCollections.observableArrayList(this.contractService.getAllContractTypes());
	}
	
	public void setClock(Clock clock) {
		this.clock = clock;
	}
	// EINDE SETTERS

	//Contracts stats
	public int getActiveContractCountByContractType(IContractType type) {
		return (int) contracts.stream()
				.filter(e -> e.getStatus() != Contract.ContractStatus.BEEINDIGD && e.getType() == type)
				.count();
	}

	//ticket stats
	public int getCompletedTicketsCountPerContractType(IContractType type) { //nog niet gebruikt
		return (int) tickets.stream()
				.filter(e -> (e.getContract() != null) && (e.getContract().getType() == type && e.getStatus() == Ticket.Status.AFGEHANDELD)).count();
	}

	public int getPercentageTicketsCompletedInTimePerContractType(IContractType type) { //nog niet gebruikt
		List<ITicket> allTickets = this.getTicketsCompletedPerContractType(type);
		return getPercentageTicketsCompletedInTimePerTicketList(allTickets);
		}	
	
	private int getPercentageTicketsCompletedInTimePerTicketList(List<ITicket> tickets) { //nog correct te implementeren?
		if (tickets.size() == 0) {
			return 100;
		} else {
			int countSuccesses = 0;
			for (ITicket ticket : tickets) {

				// Databank soms verkeerd geseed met null-waarden
				if (ticket.getDatumAfgesloten() != null) {

					double durationInDays = Duration.between(ticket.getDatumAangemaakt(), ticket.getDatumAfgesloten())
							.toDays();

					if (durationInDays < ticket.getContract().getType().getMaximaleAfhandeltijd()) {
						countSuccesses++;
					}
				}
			}
			return (countSuccesses / tickets.size()) * 100 ; 
		}
	}
	
	public Map <String, Integer> getStatPercentageTicketsCompletedInTimePerMonth() { //nog correct te implementeren?
		LocalDateTime minusSix = LocalDateTime.now().minusMonths(6);
		String monthMinusSix = (Maand.of(minusSix.getMonth().getValue()).toString()) + " " + minusSix.getDayOfMonth();
		LocalDateTime minusFive = LocalDateTime.now().minusMonths(5);
		String monthMinusFive= Maand.of(minusFive.getMonth().getValue()).toString() + " " + minusFive.getDayOfMonth();
		LocalDateTime minusFour = LocalDateTime.now().minusMonths(4);
		String monthMinusFour = Maand.of(minusFour.getMonth().getValue()).toString() + " " + minusFour.getDayOfMonth();
		LocalDateTime minusThree = LocalDateTime.now().minusMonths(3);
		String monthMinusThree = Maand.of(minusThree.getMonth().getValue()).toString() + " " + minusThree.getDayOfMonth();
		LocalDateTime minusTwo = LocalDateTime.now().minusMonths(2);
		String monthMinusTwo = Maand.of(minusTwo.getMonth().getValue()).toString() + " " + minusTwo.getDayOfMonth();
		LocalDateTime minusOne = LocalDateTime.now().minusMonths(1);
		String monthMinusOne = Maand.of(minusOne.getMonth().getValue()).toString() + " " + minusOne.getDayOfMonth();
		LocalDateTime minusZero = LocalDateTime.now();
		String monthMinusZero = Maand.of(minusZero.getMonth().getValue()).toString() + " " + minusZero.getDayOfMonth();

		Map<String, List<ITicket>> ticketMap = new LinkedHashMap<>();
		ticketMap.put(monthMinusSix + " - " + monthMinusFive, new ArrayList<>());
		ticketMap.put(monthMinusFive + " - " + monthMinusFour, new ArrayList<>());
		ticketMap.put(monthMinusFour + " - " + monthMinusThree, new ArrayList<>());
		ticketMap.put(monthMinusThree + " - " + monthMinusTwo, new ArrayList<>());
		ticketMap.put(monthMinusTwo + " - " + monthMinusOne, new ArrayList<>());
		ticketMap.put(monthMinusOne + " - " + monthMinusZero, new ArrayList<>());
		
		List<ITicket> allTickets = this.getTickets().stream().filter(e -> e.getDatumAfgesloten() != null).collect(Collectors.toList());
		
		//Vul ticketMap op met tickets gesorteerd per maand
		for (ITicket ticket : allTickets) {
			LocalDateTime afsluitdatumTicket = ticket.getDatumAfgesloten();
			
			if (timeInRange(minusSix, minusFive, afsluitdatumTicket)) {
				ticketMap.get(monthMinusSix + " - " + monthMinusFive).add(ticket);
			}
			if (timeInRange(minusFive, minusFour, afsluitdatumTicket)) {
				ticketMap.get(monthMinusFive + " - " + monthMinusFour).add(ticket);
			}
			if (timeInRange(minusFour, minusThree, afsluitdatumTicket)) {
				ticketMap.get(monthMinusFour + " - " + monthMinusThree).add(ticket);
			}
			if (timeInRange(minusThree, minusTwo, afsluitdatumTicket)) {
				ticketMap.get(monthMinusThree + " - " + monthMinusTwo).add(ticket);
			}
			if (timeInRange(minusTwo, minusOne, afsluitdatumTicket)) {
				ticketMap.get(monthMinusTwo + " - " + monthMinusOne).add(ticket);
			}
			if (timeInRange(minusOne, minusZero, afsluitdatumTicket)) {
				ticketMap.get(monthMinusOne + " - " + monthMinusZero).add(ticket);
			}
		}		

		Map<String, Integer> statistiek = new LinkedHashMap<>();
		
		for (Entry<String, List<ITicket>> entry : ticketMap.entrySet()) {
			statistiek.put(entry.getKey(), getPercentageTicketsCompletedInTimePerTicketList(entry.getValue()));
		}
		log.info(statistiek.toString());

		return statistiek;
	}
	
	public Map <String, Integer> getStatCompletedInTimePercentageContractTypes() {
		Map<String, Integer> statistiek = new LinkedHashMap<>();
		
		List<IContractType> contracttypes = getContractTypes();
		
		for (IContractType type : contracttypes) {
			statistiek.put(type.getName(), getPercentageTicketsCompletedInTimePerContractType(type));
		}
		
		return statistiek;
	}

	public List<ITicket> getTicketsCompletedPerContractType(IContractType type) {
		return tickets.stream()
				.filter(e -> e.getContract() != null)
				.filter(e -> e.getContract().getType() == type && e.getStatus() == Ticket.Status.AFGEHANDELD)
				.collect(Collectors.toList());
	}

	public boolean contractTypeHasCompletedTickets(IContractType type) {
		return this.getTicketsCompletedPerContractType(type).size() != 0;
	}
	
	//STATISTIEK
	public Map<String, Long> getStatStatusTicket() {
		Map<String, Long> statistiek = new LinkedHashMap<>();
		statistiek.put("Afgehandeld ticket", tickets.stream().filter(e -> e.getStatus().equals(Ticket.Status.AFGEHANDELD)).count());
		statistiek.put("Aangemaakte tickets", tickets.stream().filter(e -> e.getStatus().equals(Ticket.Status.AANGEMAAKT)).count());
		statistiek.put("Geannullerde tickets", tickets.stream().filter(e -> e.getStatus().equals(Ticket.Status.GEANNULEERD)).count());
		statistiek.put("Tickets in behandeling", tickets.stream().filter(e -> e.getStatus().equals(Ticket.Status.IN_BEHANDELING)).count());

		return statistiek;
	}
	
	public Map<String, Long> getStatContractTypeTicket(){
		var ticketsMetContract = tickets.stream().filter(e -> e.getContract() != null).collect(Collectors.toList());
		Map<String, Long> statistiek = new LinkedHashMap<>();
		for (IContractType type : contractTypes) {
			long filteredTicketsCount = ticketsMetContract.stream().filter(e -> e.getContract().getType() == type).count();
			statistiek.put(type.getName() + " - " + filteredTicketsCount, filteredTicketsCount);
		}
		
		return statistiek;
	}

	public Map<String, Double> getStatGemiddeldeLooptijd() {
		Map<String, Double> statistiek = new LinkedHashMap<>();
		var CompletedTickets = tickets.stream()
				.filter(e -> e.getContract() != null && e.getStatus() == Ticket.Status.AFGEHANDELD
						&& e.getDatumAfgesloten() != null && e.getDatumAangemaakt() != null)
				.collect(Collectors.toList());

		// Geeft per type een lijst van tickets
		for (IContractType type : contractTypes) {
			List<ITicket> ticketsPerType = CompletedTickets.stream().filter(e -> e.getContract().getType() == type)
					.collect(Collectors.toList());
			List<Long> leadTimePerTicketList = new ArrayList<>();

			// Telt per ticket het aantal dagen tussen aanmaak en afsluit
			for (ITicket ticket : ticketsPerType) {
				Duration leadTime = Duration.between(ticket.getDatumAangemaakt(), ticket.getDatumAfgesloten());
				leadTimePerTicketList.add(leadTime.toDays());
			}

			// Berekent gemiddelde lead time
			double AverageLeadTimePerContractType = leadTimePerTicketList.stream().mapToDouble(value -> value).average()
					.orElse(0);

			// Voegt nieuwe data toe met label en waarde
			statistiek.put(type.getName(), AverageLeadTimePerContractType);
		}
		return statistiek;
	}

	public Map<String, Integer> getStatTicketsAfgewerktPerMaand() {
		List<ITicket> afgehandeldeTickets = tickets.stream().filter(e -> e.getStatus() == Ticket.Status.AFGEHANDELD).collect(Collectors.toList());
		return createMapStatistiekPerMaand(afgehandeldeTickets, true);
	}
	
	public Map<String, Integer> getStatTicketsAangemaaktPerMaand() {
		return createMapStatistiekPerMaand(tickets, false);
	}
	
	public Map<String, Integer> getStatTicketsAfgewerktBinnenTijd(){ //nog niet gebruikt
		List<ITicket> ticketsAfgewerktBinnenTijd = tickets.stream()
				.filter(e -> e.getStatus() == Ticket.Status.AFGEHANDELD
				&& e.getContract().getType().getMaximaleAfhandeltijd() < e.getDatumAfgesloten().getHour() - e.getDatumAangemaakt().getHour())
				.collect(Collectors.toList());
		return createMapStatistiekPerMaand(ticketsAfgewerktBinnenTijd, false);
	}
	
	public Map<String, Double> getStatTicketsGemiddeldeOpenstaandPerMaand(){
		return berekenMaandGemiddeldes(TicketsBerekenOpenstaandPerDagVoorJaar());
	}
	
	public Map<String, Double> getStatContractenGemiddeldeLopendPerMaand(){
		return berekenMaandGemiddeldes(ContractenBerekenOpenstaandPerDagVoorJaar());
	}
	
	//KPI's
	public Map<String, String> getKPITicketsAfgewerktBinnenTijd(){
			List<ITicket> ticketsAfgewerktBinnenTijd = tickets.stream()
					.filter(e -> e.getStatus() == Ticket.Status.AFGEHANDELD
					&& e.getContract().getType().getMaximaleAfhandeltijd() > Duration.between(e.getDatumAangemaakt(), e.getDatumAfgesloten()).toHours())
					.collect(Collectors.toList());
			
			Map<String, LocalDateTime> intervallen = new LinkedHashMap<>();
			intervallen.put("Deze week: ", LocalDateTime.now().minusWeeks(1));
			intervallen.put("Deze maand: ", LocalDateTime.now().minusMonths(1));
			intervallen.put("Dit jaar: ", LocalDateTime.now().minusYears(1));
			
			return createMapKPI_weekMaandJaar(ticketsAfgewerktBinnenTijd, tickets, intervallen, false, true);
		}
	
	public Map<String, String> getKPITicketsOpenstaandPerDag(){
		return berekenSummaries("aantal openstaande tickets per dag", TicketsBerekenOpenstaandPerDagVoorJaar());
	}
	
	public Map<String, String> getKPIContractenLopendePerDag(){
		return berekenSummaries("aantal lopende contracten per dag", ContractenBerekenOpenstaandPerDagVoorJaar());
	}
	
	//Z. hulpmethodes Statistieken/KPI's
		//Z.1 Algemeen
	private Map<String, Double> berekenMaandGemiddeldes(Long[] data) {
		//berekent de gemiddeldes per maand uit een array die data bevat voor 365 dagen
		Map<String, Double> kpi = new LinkedHashMap<>();
		LocalDateTime now = LocalDateTime.now(clock);
		int reedsBerekend = 0;
		int maandCounter = now.getMonthValue()+11;
		
		//berekening voor eerste maand
		int eersteMaand = now.getMonthValue();
		int dagVanDeMaand = now.getDayOfMonth();
		int dagenInEersteMaand = YearMonth.of(now.getYear(), eersteMaand).lengthOfMonth();
		IntSummaryStatistics summaryEersteMaand = Stream.of(data).skip(reedsBerekend).limit(dagVanDeMaand).mapToInt(Long::intValue).summaryStatistics();
		reedsBerekend += dagVanDeMaand;
		maandCounter--;
		kpi.put(Maand.of(eersteMaand).toString(), (Double) summaryEersteMaand.getAverage());
		
		//berekening voor andere 11 maanden
		for(int i = 0; i < 11; i++) {
			int maand = (maandCounter%12)+1;
			int dagenInMaand = YearMonth.of(now.getYear(), maand).lengthOfMonth();
			IntSummaryStatistics summary = Stream.of(data).skip(reedsBerekend).limit(dagenInMaand).mapToInt(Long::intValue).summaryStatistics();
			reedsBerekend += dagenInMaand;
			maandCounter--;
			kpi.put(Maand.of(maand).toString(), (Double) summary.getAverage());
		}
		
		return kpi;
	}
	
	private Map<String, String> berekenSummaries(String title, Long[] data){
		Map<String, String> kpi = new LinkedHashMap<>();
		LocalDateTime now = LocalDateTime.now(clock);
		
		//deze week 
		IntSummaryStatistics summaryWeek = Stream.of(data).limit(now.getDayOfWeek().getValue()).mapToInt(Long::intValue).summaryStatistics();
		double averageWeek = summaryWeek.getAverage();
		int maxWeek= summaryWeek.getMax();
		int minWeek = summaryWeek.getMin();
		kpi.put(title + String.format(" - %s", "DEZE WEEK"), null);
		kpi.put("gemiddeld (w): ", String.format("%.2f", averageWeek));
		kpi.put("max (w): ", String.format("%s", maxWeek));
		kpi.put("min (w): ", String.format("%s", minWeek));
		
		//deze maand
		IntSummaryStatistics summaryMaand = Stream.of(data).limit(now.getDayOfMonth()).mapToInt(Long::intValue).summaryStatistics();
		double averageMaand = summaryMaand.getAverage();
		int maxMaand = summaryMaand.getMax();
		int minMaand = summaryMaand.getMin();
		kpi.put( title + String.format(" - %s", Maand.of(now.getMonthValue())), null);
		kpi.put("gemiddeld (m): ", String.format("%.2f", averageMaand));
		kpi.put("max (m): ", String.format("%s", maxMaand));
		kpi.put("min (m): ", String.format("%s", minMaand));
		
		//dit jaar
		IntSummaryStatistics summaryJaar = Stream.of(data).limit(now.getDayOfYear()).mapToInt(Long::intValue).summaryStatistics();
		double averageJaar = summaryJaar.getAverage();
		int maxJaar = summaryJaar.getMax();
		int minJaar = summaryJaar.getMin();
		kpi.put(title + String.format(" - %s", now.getYear()), null);
		kpi.put("gemiddeld (j): ", String.format("%.2f", averageJaar));
		kpi.put("max (j): ", String.format("%s", maxJaar));
		kpi.put("min (j): ", String.format("%s", minJaar));
		
		return kpi;
	}
	
		//Z.2 tickets
	private Long[] TicketsBerekenOpenstaandPerDagVoorJaar() {
		//berekent het gemiddelde aantal openstaande tickets per dag, voor 365 dagen
		LocalDateTime now = LocalDateTime.now(clock);
		Calendar calendar = Calendar.getInstance();
		calendar.set(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		int yearMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		Long data[] = new Long[yearMaxDays];
		IntStream.rangeClosed(0, yearMaxDays-1).forEach(day ->{ //per dag
			LocalDateTime datum = now.minusDays(day);
			data[day] = tickets.stream().filter(t ->{ //openstaande tickets op deze dag
				boolean reedsAangemaakt = datum.compareTo(t.getDatumAangemaakt()) >= 0;
				boolean wasNogNietAfgesloten = t.getDatumAfgesloten() == null ? (t.getStatus() == Ticket.Status.AANGEMAAKT || t.getStatus() == Ticket.Status.IN_BEHANDELING) : datum.compareTo(t.getDatumAfgesloten()) < 0;
				return reedsAangemaakt && wasNogNietAfgesloten;
			}).count();
		});
		
		return data;
	}
	
	private Map<String, String> createMapKPI_weekMaandJaar(List<ITicket> ticketsTeller, List<ITicket> ticketsNoemer, Map<String, LocalDateTime> intervallen, boolean afgesloten, boolean percentage){
		Map<String, String> res = new LinkedHashMap<>();
		DecimalFormat decFormat = new DecimalFormat("#%");
				
				for(var e : intervallen.entrySet()) {
					String kpi;
					double teller = 0;
					for(var t : ticketsTeller) {
						LocalDateTime dateToCheck;
						if (afgesloten) {
							 dateToCheck = t.getDatumAfgesloten();
						} else {
							 dateToCheck = t.getDatumAangemaakt();
						}
						if(timeInRange(e.getValue(), LocalDateTime.now(), dateToCheck)) teller++;
					}
					if(percentage) {
						double noemer = 0;
						for(var tn : ticketsNoemer) {
							LocalDateTime dateToCheck;
							if (afgesloten) {
								 dateToCheck = tn.getDatumAfgesloten();
							} else {
								 dateToCheck = tn.getDatumAangemaakt();
							}
							if(timeInRange(e.getValue(), LocalDateTime.now(), dateToCheck)) noemer++;
						}
						//System.out.printf("teller: %s, noemer: %s, kpi: %s%n", teller, noemer, teller/noemer);
						if(teller == 0 && noemer == 0) kpi = "-";
						else if(teller != 0) kpi = decFormat.format(teller/noemer);
						else kpi = decFormat.format(0);
					} else {
						kpi = String.valueOf(teller);
					}
					//put
					res.put(e.getKey(), kpi);
				}
				
			return res;
		}
	
	private Map<String, Integer> createMapStatistiekPerMaand(List<ITicket> tickets, boolean afgesloten){
		// Intervallen
		LocalDateTime minusSix = LocalDateTime.now(clock).minusMonths(6);
		String monthMinusSix = (Maand.of(minusSix.getMonth().getValue()).toString()) + " " + minusSix.getDayOfMonth();
		LocalDateTime minusFive = LocalDateTime.now(clock).minusMonths(5);
		String monthMinusFive= Maand.of(minusFive.getMonth().getValue()).toString() + " " + minusFive.getDayOfMonth();
		LocalDateTime minusFour = LocalDateTime.now(clock).minusMonths(4);
		String monthMinusFour = Maand.of(minusFour.getMonth().getValue()).toString() + " " + minusFour.getDayOfMonth();
		LocalDateTime minusThree = LocalDateTime.now(clock).minusMonths(3);
		String monthMinusThree = Maand.of(minusThree.getMonth().getValue()).toString() + " " + minusThree.getDayOfMonth();
		LocalDateTime minusTwo = LocalDateTime.now(clock).minusMonths(2);
		String monthMinusTwo = Maand.of(minusTwo.getMonth().getValue()).toString() + " " + minusTwo.getDayOfMonth();
		LocalDateTime minusOne = LocalDateTime.now(clock).minusMonths(1);
		String monthMinusOne = Maand.of(minusOne.getMonth().getValue()).toString() + " " + minusOne.getDayOfMonth();
		LocalDateTime minusZero = LocalDateTime.now(clock);
		String monthMinusZero = Maand.of(minusZero.getMonth().getValue()).toString() + " " + minusZero.getDayOfMonth();


				Map<String, Integer> ticketMap = new LinkedHashMap<>();
				ticketMap.put(monthMinusSix + " - " + monthMinusFive, 0);
				ticketMap.put(monthMinusFive  + " - " +  monthMinusFour, 0);
				ticketMap.put(monthMinusFour  + " - " +  monthMinusThree, 0);
				ticketMap.put(monthMinusThree  + " - " +  monthMinusTwo, 0);
				ticketMap.put(monthMinusTwo  + " - " +  monthMinusOne, 0);
				ticketMap.put(monthMinusOne  + " - " +  monthMinusZero, 0);

				for (ITicket ticket : tickets) {
					
					LocalDateTime dateToCheck;
					if (afgesloten) {
						 dateToCheck = ticket.getDatumAfgesloten();
					} else {
						 dateToCheck = ticket.getDatumAangemaakt();
					}

					if (timeInRange(minusSix, minusFive, dateToCheck)) {
						ticketMap.merge(monthMinusSix  + " - " +  monthMinusFive, 1, Integer::sum);
					}
					if (timeInRange(minusFive, minusFour, dateToCheck)) {
						ticketMap.merge(monthMinusFive  + " - " +  monthMinusFour, 1, Integer::sum);
					}
					if (timeInRange(minusFour, minusThree, dateToCheck)) {
						ticketMap.merge(monthMinusFour  + " - " +  monthMinusThree, 1, Integer::sum);
					}
					if (timeInRange(minusThree, minusTwo, dateToCheck)) {
						ticketMap.merge(monthMinusThree  + " - " +  monthMinusTwo, 1, Integer::sum);
					}
					if (timeInRange(minusTwo, minusOne, dateToCheck)) {
						ticketMap.merge(monthMinusTwo  + " - " +  monthMinusOne, 1, Integer::sum);
					}
					if (timeInRange(minusOne, minusZero, dateToCheck)) {
						ticketMap.merge(monthMinusOne  + " - " +  monthMinusZero, 1, Integer::sum);
					}
				}

				return ticketMap;
	}
	
	private boolean timeInRange(LocalDateTime begin, LocalDateTime end, LocalDateTime toCheck) {

		if (toCheck.isAfter(begin) && toCheck.isBefore(end))
			return true;
		else
			return false;
	}

		//Z.3 Contracten
	private Long[] ContractenBerekenOpenstaandPerDagVoorJaar() {
		//berekent het gemiddelde aantal openstaande tickets per dag, voor 365 dagen
		LocalDateTime now = LocalDateTime.now(clock);
		Calendar calendar = Calendar.getInstance();
		calendar.set(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		int yearMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		Long data[] = new Long[yearMaxDays];
		IntStream.rangeClosed(0, yearMaxDays-1).forEach(day ->{ //per dag
			LocalDateTime datum = now.minusDays(day);
			data[day] = contracts.stream().filter(c ->{ //openstaande tickets op deze dag
				boolean reedsAangemaakt = datum.compareTo(c.getStartDatum()) >= 0;
				boolean wasNogNietAfgesloten = c.getEindDatum() == null ? (c.getStatus() == Contract.ContractStatus.LOPEND) : datum.compareTo(c.getEindDatum()) < 0;
				return reedsAangemaakt && wasNogNietAfgesloten;
			}).count();
		});
		return data;
	}
	
	private enum Maand {
		JANUARI, FEBRUARI, MAART, APRIL, MEI, JUNI, JULI, AUGUSTUS, SEPTEMBER, OKTOBER, NOVEMBER, DECEMBER;
		
		private static final Maand[] ENUMS = Maand.values();
		
		public static Maand of(int maand) {
	        if (maand < 1 || maand > 12) {
	            throw new DateTimeException("Invalid value for MonthOfYear: " + maand);
	        }
	        return ENUMS[maand - 1];
	    }
	}


}
