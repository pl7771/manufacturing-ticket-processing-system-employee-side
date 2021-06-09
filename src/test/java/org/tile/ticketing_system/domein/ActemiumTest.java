package org.tile.ticketing_system.domein;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Contract.ContractStatus;
import org.tile.ticketing_system.domein.ContractType.GedekteTijdstippen;
import org.tile.ticketing_system.domein.ContractType.ManierAanmakenTicket;
import org.tile.ticketing_system.domein.Ticket.Status;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.domein.interfaces.IRole;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.services.ContractService;
import org.tile.ticketing_system.domein.services.TicketService;
import org.tile.ticketing_system.domein.services.UserService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.tile.ticketing_system.domein.ContractMother.aContractTypeWithId;
import static org.tile.ticketing_system.domein.ContractMother.anActiveContract;
import static org.tile.ticketing_system.domein.DtoMother.aCreateClientDto;
import static org.tile.ticketing_system.domein.DtoMother.aCreateContractTypeDto;
import static org.tile.ticketing_system.domein.DtoMother.aCreateEmployeeAdminDto;
import static org.tile.ticketing_system.domein.DtoMother.aCreateTicketDto;
import static org.tile.ticketing_system.domein.DtoMother.aEditTicketDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditClientDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditContractTypeDto;
import static org.tile.ticketing_system.domein.DtoMother.anEditEmployeeDto;
import static org.tile.ticketing_system.domein.TicketMother.aTicketWithId;
import static org.tile.ticketing_system.domein.UserMother.aClientWithUserId;
import static org.tile.ticketing_system.domein.UserMother.aUserWithIdAndRole;
import static org.tile.ticketing_system.domein.UserMother.anEmployeeWithUserId;
import static org.tile.ticketing_system.domein.UserMother.allRoles;
import static org.tile.ticketing_system.domein.services.UserService.UserType.CLIENT;
import static org.tile.ticketing_system.domein.services.UserService.UserType.EMPLOYEE;
import static org.tile.ticketing_system.domein.services.UserService.UserType.USER;

@ExtendWith(MockitoExtension.class)
class ActemiumTest {

    @Mock
    private UserService userService;

    @Mock
    private TicketService ticketService;

    @Mock
    private ContractService contractService;

//    @Mock
//    private Clock clock;

    @InjectMocks
    private Actemium actemium;
    
    //dummies
    private List<ITicket> ticketDummies;
    private List<IContract> contractDummies = new ArrayList<>();
    private List<IContractType> contractTypeDummies = new ArrayList<>();
    
    private Clock fixedClock;
    private final LocalDate TESTDATUM = LocalDate.of(2021, 05, 7);

    public ActemiumTest() {
    	//ticket dummies maken
    	List<Ticket> allTickets = new ArrayList<>();
        ticketDummies = new ArrayList<>();
        ContractType contractType1 = new ContractType("naam1", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 1000, 10, 500);
        ContractType contractType2 = new ContractType("naam2", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 10, 10, 500);
		ContractType contractType3 = new ContractType("naam3", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 10, 10, 500);
        
    	for(int i = 0; i<5; i++) {
    		LocalDateTime datumAangemaakt = LocalDateTime.of(2021, 03, 1, 0, 0);
    		LocalDateTime datumAfgesloten = LocalDateTime.of(2021, 03, 31, 23, 0);
    		//
    		String titel = String.format("ticket %s", i);
    		String omschrijving = String.format("omschrijving voor ticket %s", i);
    		Status status = Status.AFGEHANDELD;
    		Ticket ticket = new Ticket(datumAangemaakt, titel, omschrijving, status);
    		ticket.setDatumAfgesloten(datumAfgesloten);
    		Contract contract = new Contract(contractType1, ContractStatus.LOPEND, LocalDateTime.of(2020, 01, 10, 10, 55), LocalDateTime.of(2021, 03, 31, 23, 0));
    		ticket.setContract(contract);
    		allTickets.add(ticket);
    		contractDummies.add(contract);
    		contractTypeDummies.add(contractType1);
    	}
    	
    	//ticket dummies maken
    	for(int i = 0; i<4; i++) {
    		LocalDateTime datumAangemaakt = LocalDateTime.of(2021, 04, 1, 0, 0);
    		LocalDateTime datumAfgesloten = LocalDateTime.of(2021, 04, 15, 23, 0);
    		double maximaleAfhandeltijd = 10;
    		//
    		String titel = String.format("ticket %s", i);
    		String omschrijving = String.format("omschrijving voor ticket %s", i);
    		Status status = Status.AANGEMAAKT;
    		Ticket ticket = new Ticket(datumAangemaakt, titel, omschrijving, status);
    		ticket.setDatumAfgesloten(datumAfgesloten);
    		Contract contract = new Contract(contractType2, ContractStatus.LOPEND, LocalDateTime.of(2020, 01, 10, 10, 55), LocalDateTime.of(2022, 01, 10, 10, 55));
    		ticket.setContract(contract);
    		allTickets.add(ticket);
    		contractDummies.add(contract);
    		contractTypeDummies.add(contractType2);
    	}
    	
    	//ticket dummies maken
    	for(int i = 0; i<6; i++) {//1 tem 2 mei gemiddelde: 6, max 6, min 6
    		LocalDateTime datumAangemaakt = LocalDateTime.of(2021, 05, 1, 0, 0);
    		LocalDateTime datumAfgesloten = LocalDateTime.of(2021, 05, 7, 23, 0);
    		double maximaleAfhandeltijd = 10;
    		//
    		String titel = String.format("ticket %s", i);
    		String omschrijving = String.format("omschrijving voor ticket %s", i);
    		Status status = Status.AANGEMAAKT;
    		Ticket ticket = new Ticket(datumAangemaakt, titel, omschrijving, status);
    		ticket.setDatumAfgesloten(datumAfgesloten);
    		Contract contract = new Contract(contractType3, ContractStatus.LOPEND, LocalDateTime.of(2020, 01, 10, 10, 55), LocalDateTime.of(2022, 01, 10, 10, 55));
    		ticket.setContract(contract);
    		allTickets.add(ticket);
    		contractDummies.add(contract);
    		contractTypeDummies.add(contractType3);
    	}
    	
    	for (Ticket ticket : allTickets) {
        	var iticket = (ITicket) (Object) ticket;
        	ticketDummies.add(iticket);
        }
    	
    }

    @Test
    void shouldLogin() throws NoSuchFieldException, IllegalAccessException {
        User eenUser = UserMother.aUserWithPassword("eenPasswoord");

        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);

        User loggedInUser = this.actemium.login("mail@gmail.com", "eenPasswoord");

        assertThat(loggedInUser).isEqualTo(eenUser);

        verify(this.userService).login("mail@gmail.com", "eenPasswoord");
    }

    @Test
    void shouldRefreshLocalData() throws NoSuchFieldException, IllegalAccessException {
        User loggedInUser = aUserWithIdAndRole("testId", Role.RoleType.SUPPORTMANAGER);

        this.actemium.refreshLocalData(loggedInUser);

        verify(this.contractService).getAllCompanies();
        verify(this.userService).getAllUsersOfType(USER);
        verify(this.userService).getAllUsersOfType(EMPLOYEE);
        verify(this.userService).getAllUsersOfType(UserService.UserType.CLIENT);
        verify(this.userService).getAllRoles();
        verify(this.ticketService).getAllTicketsFor(loggedInUser);
        verify(this.contractService).getAllContracts();
        verify(this.contractService).getAllContractTypes();
    }

    @Test
    void shouldFindEmployee() throws NoSuchFieldException, IllegalAccessException {
        lenient().when(this.userService.getAllUsersOfType(EMPLOYEE)).thenReturn(List.of(anEmployeeWithUserId("testId")));
        this.actemium.refreshLocalData(null);

        Employee employee = (Employee) this.actemium.findEmployee("testId");
        assertThat(employee.getApplicationUser().getId()).isEqualTo("testId");
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        assertThatThrownBy(() -> this.actemium.findEmployee("testId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddEmployee() throws NoSuchFieldException, IllegalAccessException {
        List<IRole> roleList = allRoles();
        var adminDto = aCreateEmployeeAdminDto();
        var adminRole = roleList.stream().filter(role -> role.getName().equals("Administrator")).findAny().orElseThrow();
        lenient().when(this.userService.getAllRoles()).thenReturn(roleList);
        when(this.userService.createEmployee(adminDto, (Role) adminRole)).thenReturn(anEmployeeWithUserId("testId"));
        this.actemium.refreshLocalData(null);

        this.actemium.addEmployee(adminDto);

        var observableUserList = this.actemium.getFilteredUserList();
        assertThat(observableUserList.stream().filter(user -> user.getId().equals("testId"))).isNotEmpty();
    }

    @Test
    void shouldEditEmployee() throws NoSuchFieldException, IllegalAccessException {
        var editEmployeeDto = anEditEmployeeDto();
        var employeeToEdit = anEmployeeWithUserId("testId");
        lenient().when(this.userService.getAllUsersOfType(EMPLOYEE)).thenReturn(List.of(employeeToEdit));
        this.actemium.refreshLocalData(null);

        this.actemium.editEmployee(editEmployeeDto);

        verify(this.userService).editEmployee(editEmployeeDto, employeeToEdit);
    }

    @Test
    void shouldFindClient() throws NoSuchFieldException, IllegalAccessException {
        lenient().when(this.userService.getAllUsersOfType(CLIENT)).thenReturn(List.of(aClientWithUserId("testId")));
        this.actemium.refreshLocalData(null);

        Client client = (Client) this.actemium.findClient("testId");
        assertThat(client.getApplicationUser().getId()).isEqualTo("testId");
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {
        assertThatThrownBy(() -> this.actemium.findClient("testId"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddClient() throws NoSuchFieldException, IllegalAccessException {
        var client = aClientWithUserId("testId");
        var clientCompany = client.getCompany();
        var roleList = allRoles();
        var clientDto = aCreateClientDto();
        var clientRole = roleList.stream().filter(role -> role.getName().equals("Client")).findAny().orElseThrow();
        lenient().when(this.userService.getAllRoles()).thenReturn(roleList);
        lenient().when(this.contractService.getAllCompanies()).thenReturn(List.of(clientCompany));
        when(this.userService.createClient(clientDto, clientCompany, (Role) clientRole)).thenReturn(client);
        this.actemium.refreshLocalData(null);

        this.actemium.addClient(clientDto);

        var observableUserList = this.actemium.getFilteredUserList();
        assertThat(observableUserList.stream().filter(user -> user.getId().equals("testId"))).isNotEmpty();
    }

    @Test
    void shouldEditClient() throws NoSuchFieldException, IllegalAccessException {
        var editClientDto = anEditClientDto();
        var clientToEdit = aClientWithUserId("testId");
        lenient().when(this.userService.getAllUsersOfType(CLIENT)).thenReturn(List.of(clientToEdit));
        lenient().when(this.contractService.getAllCompanies()).thenReturn(List.of(clientToEdit.getCompany()));
        this.actemium.refreshLocalData(null);

        this.actemium.editClient(editClientDto);

        verify(this.userService).editClient(editClientDto, clientToEdit.getCompany(), clientToEdit);
    }

    @Test
    void shouldFindTicket() throws NoSuchFieldException, IllegalAccessException {
        var ticketToFind = aTicketWithId(1);
        lenient().when(this.ticketService.getAllTicketsFor(any())).thenReturn(List.of(ticketToFind));
        this.actemium.refreshLocalData(null);

        var foundTicket = this.actemium.findTicket(1);

        assertThat(foundTicket).isEqualTo(ticketToFind);
    }

    @Test
    void shouldThrowExceptionWhenTicketNotFound() {
        assertThatThrownBy(() -> this.actemium.findTicket(1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldFindContractTypeById() throws NoSuchFieldException, IllegalAccessException {
        var contractTypeToFind = aContractTypeWithId(1);
        lenient().when(this.contractService.getAllContractTypes()).thenReturn(List.of(contractTypeToFind));
        this.actemium.refreshLocalData(null);

        IContractType foundContractType = this.actemium.findContractTypeById(1);

        assertThat(foundContractType).isEqualTo(contractTypeToFind);
    }

    @Test
    void shouldThrowExceptionWhenContractTypeNotFound() {
        assertThatThrownBy(() -> this.actemium.findContractTypeById(1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAddTicket() throws NoSuchFieldException, IllegalAccessException {
        var ticketToCreate = aTicketWithId(1);
        var ticketDto = aCreateTicketDto();
        var company = new Company("Coca", "eenAddress", "0485446677");
        lenient().when(this.contractService.getAllCompanies()).thenReturn(List.of(company));
        when(this.ticketService.createTicket(any(), any())).thenReturn(ticketToCreate);
        this.actemium.refreshLocalData(null);

        this.actemium.addTicket(ticketDto);

        verify(this.ticketService).createTicket(ticketDto, company);

        var observableTicketList = this.actemium.getFilteredTicketList();
        assertThat(observableTicketList.stream().filter(ticket -> ticket.getTitel().equals("eenTicket"))).isNotEmpty();
    }

    @Test
    void shouldEditTicket() throws NoSuchFieldException, IllegalAccessException {
        var ticketToEdit = aTicketWithId(1);
        var editTicketDto = aEditTicketDto();
        lenient().when(this.ticketService.getAllTicketsFor(any())).thenReturn(List.of(ticketToEdit));
        this.actemium.refreshLocalData(null);

        this.actemium.editTicket(editTicketDto);

        verify(this.ticketService).editTicket(editTicketDto, ticketToEdit);
    }

    @Test
    void shouldAddContractType() throws NoSuchFieldException, IllegalAccessException {
        var contractType = aContractTypeWithId(1);
        var createContractTypeDto = aCreateContractTypeDto();
        when(this.contractService.createContractType(any())).thenReturn(contractType);

        this.actemium.addContractType(createContractTypeDto);

        verify(this.contractService).createContractType(createContractTypeDto);
    }

    @Test
    void shouldEditContractType() throws NoSuchFieldException, IllegalAccessException {
        var contractTypeToEdit = aContractTypeWithId(1);
        var editContractTypeDto = anEditContractTypeDto();
        lenient().when(this.contractService.getAllContractTypes()).thenReturn(List.of(contractTypeToEdit));
        this.actemium.refreshLocalData(null);

        this.actemium.editContractType(editContractTypeDto);

        verify(this.contractService).editContractType(editContractTypeDto, contractTypeToEdit);
    }

    @Test
    void shouldFindCompany() {
        var companyToFind = new Company("Coca", "eenAddress", "0486778899");
        lenient().when(this.contractService.getAllCompanies()).thenReturn(List.of(companyToFind));
        this.actemium.refreshLocalData(null);

        Company foundCompany = (Company) this.actemium.findCompany("Coca");

        assertThat(foundCompany).isEqualTo(companyToFind);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        assertThatThrownBy(() -> this.actemium.findCompany("Coca"))
                .isInstanceOf(IllegalArgumentException.class);
    }

//    @Test
//    void shouldFindRole() {
//        var allRoles = allRoles();
//        lenient().when(this.userService.getAllRoles()).thenReturn(allRoles);
//        this.actemium.refreshLocalData(null);
//
//        Role foundRole = this.actemium.findRole("Administrator");
//
//        assertThat(foundRole.getName()).isEqualTo("Administrator");
//    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        assertThatThrownBy(() -> this.actemium.findRole("Administrator"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldGetActiveContractCountByContractType() throws NoSuchFieldException, IllegalAccessException {
        var contractType = aContractTypeWithId(1);
        var activeContract = anActiveContract(contractType);

        lenient().when(this.contractService.getAllContracts()).thenReturn(List.of(activeContract));
        this.actemium.refreshLocalData(null);

        assertThat(this.actemium.getActiveContractCountByContractType(contractType)).isOne();
    }

    @Test
    void shouldReturnCountZeroWhenNoActiveContracts() throws NoSuchFieldException, IllegalAccessException {
        var contractType = aContractTypeWithId(1);

        assertThat(this.actemium.getActiveContractCountByContractType(contractType)).isZero();
    }
    
	//Contracts stats
//	public int getActiveContractCountByContractType_returnsCorrectCount(IContractType type) { methode nog niet gebruikt
//	}

	//ticket stats
//	public int getCompletedTicketsCountPerContractType_returnsCorrectCount(IContractType type) { methode nog niet gebruikt
//	}
//
    @Test
	public void getPercentageTicketsCompletedInTimePerContractType_returnsCorrectPercentage() { //methode in actemium nog correct te implementeren?
//    	//inloggen
//    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
//        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
//        actemium.login("mail@gmail.com", "eenPasswoord");
//        //mock ticketService
//        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
//        this.actemium.refreshLocalData(eenUser);
//        
//        //Arrange
//        IContractType contractType1 = this.contractTypeDummies.get(0);
//        IContractType contractType2 = new ContractType();
//		
//		//Act
//        int res1 = actemium.getPercentageTicketsCompletedInTimePerContractType(contractType1);
//        Assertions.assertThat(res1).isEqualTo(100);
//        int res2 = actemium.getPercentageTicketsCompletedInTimePerContractType(contractType2);
//        Assertions.assertThat(res2).isEqualTo(100);
		}
	
    @Test
	public void getStatPercentageTicketsCompletedInTimePerMonth_returnsCorrectPercentage() { //methode in actemium nog correct te implementeren?
//    	//inloggen
//    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
//        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
//        actemium.login("mail@gmail.com", "eenPasswoord");
//        //mock
//        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
//        lenient().when(this.contractService.getAllContractTypes()).thenReturn(this.contractTypeDummies);
//        this.actemium.refreshLocalData(eenUser);
//		
//		//Act
//        Map<String, Integer> map = actemium.getStatPercentageTicketsCompletedInTimePerMonth();
//        
//        //Assert
//        Object[] keySet = map.values().toArray();
//        Integer ct1 = (Integer) keySet[0];
//        Assertions.assertThat(ct1).isEqualTo(100);
//        Integer ct2 = (Integer) keySet[5];
//        Assertions.assertThat(ct2).isEqualTo(100);
    	
	}
	
    @Test
	public void getStatCompletedInTimePercentageContractTypes_returnsCorrectPercentage() throws NoSuchFieldException, IllegalAccessException {
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        lenient().when(this.contractService.getAllContractTypes()).thenReturn(this.contractTypeDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
        Map<String, Integer> map = actemium.getStatCompletedInTimePercentageContractTypes();
        
        //Assert
        Object[] keySet = map.values().toArray();
        Integer ct1 = (Integer) keySet[0];
        Assertions.assertThat(ct1).isEqualTo(100);
        Integer ct2 = (Integer) keySet[0];
        Assertions.assertThat(ct2).isEqualTo(100);
	}

    @Test
	public void getTicketsCompletedPerContractType_returnsTickets() throws NoSuchFieldException, IllegalAccessException {
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
        
        //Arrange
        IContractType contractType1 = this.contractTypeDummies.get(0);
        IContractType contractType2 = new ContractType();
		
		//Act
        List<ITicket> res1 = actemium.getTicketsCompletedPerContractType(contractType1);
        Assertions.assertThat(res1.size()).isEqualTo(5);
        List<ITicket> res2 = actemium.getTicketsCompletedPerContractType(contractType2);
        Assertions.assertThat(res2.size()).isEqualTo(0);
	}

    @Test
	public void contractTypeHasCompletedTickets_returnsTrueWhenCompletedTickets() throws NoSuchFieldException, IllegalAccessException {
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
        
        //Arrange
        IContractType contractType1 = this.contractTypeDummies.get(0);
        IContractType contractType2 = new ContractType();
		
		//Act
        boolean res1 = actemium.contractTypeHasCompletedTickets(contractType1);
        Assertions.assertThat(res1).isEqualTo(true);
        boolean res2 = actemium.contractTypeHasCompletedTickets(contractType2);
        Assertions.assertThat(res2).isEqualTo(false);
			}
    
    //STATISTIEK
    @Test
	public void getStatStatusTicket_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, Long> res = actemium.getStatStatusTicket();
		
		//Assert
		Object[] keySet = res.values().toArray();
		Long afgehandeld = (Long) keySet[0];
		Assertions.assertThat(afgehandeld).isEqualTo(5);
		Long aangemaakt = (Long) keySet[1];
		Assertions.assertThat(aangemaakt).isEqualTo(10);
		Long geannulleerd = (Long) keySet[2];
		Assertions.assertThat(geannulleerd).isEqualTo(0);
		Long inBehandeling = (Long) keySet[2];
		Assertions.assertThat(inBehandeling).isEqualTo(0);
		
	}
	
    @Test
	public void getStatContractTypeTicket_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        lenient().when(this.contractService.getAllContractTypes()).thenReturn(this.contractTypeDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, Long> res = actemium.getStatContractTypeTicket();
		
		//Assert
		Object[] keySet = res.values().toArray();
		Long naam3 = (Long) keySet[0];
		Assertions.assertThat(naam3).isEqualTo(5);
		Long naam2 = (Long) keySet[1];
		Assertions.assertThat(naam2).isEqualTo(4);
		Long naam1 = (Long) keySet[2];
		Assertions.assertThat(naam1).isEqualTo(6);
	}

    @Test
	public void getStatGemiddeldeLooptijd_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        lenient().when(this.contractService.getAllContractTypes()).thenReturn(this.contractTypeDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, Double> res = actemium.getStatGemiddeldeLooptijd();
		
		//Assert
		Object[] keySet = res.values().toArray();
		Double naam3 = (Double) keySet[0];
		Assertions.assertThat(naam3).isEqualTo(30);
		Double naam2 = (Double) keySet[1];
		Assertions.assertThat(naam2).isEqualTo(0);
		Double naam1 = (Double) keySet[2];
		Assertions.assertThat(naam1).isEqualTo(0);
		
	}

    @Test
	public void getStatTicketsAfgewerktPerMaand_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, Integer> res = actemium.getStatTicketsAfgewerktPerMaand();
		
		//Assert
		Object[] keySet = res.values().toArray();
		Integer maart = (Integer) keySet[3];
		Assertions.assertThat(maart).isEqualTo(0);
		Integer april = (Integer) keySet[4];
		Assertions.assertThat(april).isEqualTo(5);
		Integer mei = (Integer) keySet[5];
		Assertions.assertThat(mei).isEqualTo(0);
	}
	
    @Test
	public void getStatTicketsAangemaaktPerMaand_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, Integer> res = actemium.getStatTicketsAangemaaktPerMaand();
		
		//Assert
		Object[] keySet = res.values().toArray();
		Integer maart = (Integer) keySet[3];
		Assertions.assertThat(maart).isEqualTo(5);
		Integer april = (Integer) keySet[4];
		Assertions.assertThat(april).isEqualTo(4);
		Integer mei = (Integer) keySet[5];
		Assertions.assertThat(mei).isEqualTo(6);
	}
	
	
    @Test
    public void getStatTicketsGemiddeldeOpenstaandPerMaand_returntCorrecteStatistiek() throws NoSuchFieldException, IllegalAccessException {
		/*
		 * beter om met meerdere dummies te testen, maar heb niet voldoende tijd
		 */
		
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
        
      //Act
      		Map<String, Double> res = actemium.getStatTicketsGemiddeldeOpenstaandPerMaand();
      		
      		//Assert
      		Object[] keySet = res.values().toArray();
      		Double mei = (Double) keySet[0];
      		Assertions.assertThat(mei).isEqualTo(6);
      		Double april = (Double) keySet[1];
      		Assertions.assertThat(april).isEqualTo(2);
      		Double maart = (Double) keySet[2];
      		Assertions.assertThat(maart).isEqualTo(5);
      		Double februari = (Double) keySet[3];
      		Assertions.assertThat(februari).isEqualTo(0);
	}
    
    @Test
    public void getStatContractenGemiddeldeLopendPerMaand_returntCorrecteStatistiek(){
		/*
		 * beter om met meerdere dummies te testen, maar heb niet voldoende tijd
		 */
		
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
        //mock ticketService
        lenient().when(this.contractService.getAllContracts()).thenReturn(this.contractDummies);
        this.actemium.refreshLocalData(null);
		
		//Act
		Map<String, Double> res = actemium.getStatContractenGemiddeldeLopendPerMaand();
		
		//Assert
 		Object[] keySet = res.values().toArray();
  		Double mei = (Double) keySet[0];
  		Assertions.assertThat(mei).isEqualTo(10);
  		Double april = (Double) keySet[1];
  		Assertions.assertThat(april).isEqualTo(10);
  		Double maart = (Double) keySet[2];
  		Assertions.assertThat(maart).isEqualTo(15);
  		Double februari = (Double) keySet[3];
  		Assertions.assertThat(februari).isEqualTo(15);
	}
    
    //KPI's
    @Test
	public void getKPITicketsAfgewerktBinnenTijd_returntCorrecteKPIs() throws NoSuchFieldException, IllegalAccessException {
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, String> res = actemium.getKPITicketsAfgewerktBinnenTijd();
		
		//Assert
		Object[] keySet = res.values().toArray();
		String week = (String) keySet[0];
		Assertions.assertThat(week).isEqualTo("-");
		String maand = (String) keySet[1];
		Assertions.assertThat(maand).isEqualTo("0%");
		String jaar = (String) keySet[2];
		Assertions.assertThat(jaar).isEqualTo("33%");
		}
	
	@Test
	public void getKPITicketsOpenstaandPerDag_returntCorrecteKPIs() throws NoSuchFieldException, IllegalAccessException{
		/*
		 * beter om met meerdere dummies te testen, maar heb niet voldoende tijd
		 */
		
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
    	//inloggen
    	User eenUser = UserMother.aUserWithRole(Role.RoleType.SUPPORTMANAGER);
        when(this.userService.login("mail@gmail.com", "eenPasswoord")).thenReturn(eenUser);
        actemium.login("mail@gmail.com", "eenPasswoord");
        //mock ticketService
        lenient().when(ticketService.getAllTicketsFor(any())).thenReturn(this.ticketDummies);
        this.actemium.refreshLocalData(eenUser);
		
		//Act
		Map<String, String> res = actemium.getKPITicketsOpenstaandPerDag();
		
		//Assert
		Object[] keySet = res.values().toArray();
		String weekGemiddelde = (String) keySet[1];
		Assertions.assertThat(weekGemiddelde).isEqualTo("6,00");
		String weekMax = (String) keySet[2];
		Assertions.assertThat(weekMax).isEqualTo("6");
		String weekMin = (String) keySet[3];
		Assertions.assertThat(weekMin).isEqualTo("6");
		String maandGemiddelde = (String) keySet[5];
		Assertions.assertThat(maandGemiddelde).isEqualTo("6,00");
		String maandMax = (String) keySet[6];
		Assertions.assertThat(maandMax).isEqualTo("6");
		String maandMin = (String) keySet[7];
		Assertions.assertThat(maandMin).isEqualTo("6");
		String jaarGemiddelde = (String) keySet[9];
		Assertions.assertThat(jaarGemiddelde).isEqualTo("2,02");
		String jaarMax = (String) keySet[10];
		Assertions.assertThat(jaarMax).isEqualTo("6");
		String jaarMin = (String) keySet[11];
		Assertions.assertThat(jaarMin).isEqualTo("0");
	}
	
	@Test
	public void getKPIContractenLopendePerDag_returntCorrecteKPIs(){
		/*
		 * beter om met meerdere dummies te testen, maar heb niet voldoende tijd
		 */
		
		//tijd instellen
    	actemium.setClock(Clock.fixed(TESTDATUM.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    	
        //mock ticketService
        lenient().when(this.contractService.getAllContracts()).thenReturn(this.contractDummies);
        this.actemium.refreshLocalData(null);
		
		//Act
		Map<String, String> res = actemium.getKPIContractenLopendePerDag();
		
		//Assert
		Object[] keySet = res.values().toArray();
		String weekGemiddelde = (String) keySet[1];
		Assertions.assertThat(weekGemiddelde).isEqualTo("10,00");
		String weekMax = (String) keySet[2];
		Assertions.assertThat(weekMax).isEqualTo("10");
		String weekMin = (String) keySet[3];
		Assertions.assertThat(weekMin).isEqualTo("10");
		String maandGemiddelde = (String) keySet[5];
		Assertions.assertThat(maandGemiddelde).isEqualTo("10,00");
		String maandMax = (String) keySet[6];
		Assertions.assertThat(maandMax).isEqualTo("10");
		String maandMin = (String) keySet[7];
		Assertions.assertThat(maandMin).isEqualTo("10");
		String jaarGemiddelde = (String) keySet[9];
		Assertions.assertThat(jaarGemiddelde).isEqualTo("13,54");
		String jaarMax = (String) keySet[10];
		Assertions.assertThat(jaarMax).isEqualTo("15");
		String jaarMin = (String) keySet[11];
		Assertions.assertThat(jaarMin).isEqualTo("10");
	}

//    @Test
//    void getCompletedTicketsCountPerContractType() {
//    }
//
//    @Test
//    void getPercentageTicketsCompletedInTimePerContractType() {
//    }
//
//    @Test
//    void getTicketsCompletedPerContractType() {
//    }
//
//    @Test
//    void contractTypeHasCompletedTickets() {
//    }
}
