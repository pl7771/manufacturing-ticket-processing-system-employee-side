package org.tile.ticketing_system.domein.services;

import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.Contract.ContractStatus;
import org.tile.ticketing_system.domein.ContractType.ContractTypeStatus;
import org.tile.ticketing_system.domein.ContractType.GedekteTijdstippen;
import org.tile.ticketing_system.domein.ContractType.ManierAanmakenTicket;
import org.tile.ticketing_system.domein.Ticket.Status;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.interfaces.ICompany;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.domein.repository.CompanyDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractTypeDaoJpa;
import org.tile.ticketing_system.domein.repository.GenericDaoJpa;
import org.tile.ticketing_system.domein.repository.interfaces.GenericDao;

import javafx.collections.FXCollections;

import static org.mockito.ArgumentMatchers.refEq;

@Log
@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {
	@Mock
	private CompanyDaoJpa companyRepo;
	@Mock
    private ContractDaoJpa contractRepo;
	@Mock
    private ContractTypeDaoJpa contractTypeRepo;
	@Mock
	private ContractType contractTypeMock;
	@InjectMocks
	private ContractService contractService;
	
	private List<Company> companies;
	private List<Contract> contracts;
	private List<ContractType> contractTypes;
	
	public ContractServiceTest() {
		//company list
		companies = new ArrayList<>();
		companies.add(new Company("CompanyName1", "Address1", "phoneNumber1"));
		companies.add(new Company("CompanyName2", "Address2", "phoneNumber2"));
		companies.add(new Company("CompanyName3", "Address3", "phoneNumber3"));
		
		//list contractTypes
		contractTypes = new ArrayList<>();
		contractTypes.add(new ContractType("naam", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500));
		contractTypes.add(new ContractType("naam2", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500));
		contractTypes.add(new ContractType("naam3", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500));
		
		//contract list
		contracts = new ArrayList<>();
		contracts.add(new Contract(contractTypes.get(0), ContractStatus.LOPEND, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
		contracts.add(new Contract(contractTypes.get(1), ContractStatus.LOPEND, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
		contracts.add(new Contract(contractTypes.get(1), ContractStatus.IN_BEHANDELING, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
		contracts.add(new Contract(contractTypes.get(2), ContractStatus.IN_BEHANDELING, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
		contracts.add(new Contract(contractTypes.get(2), ContractStatus.IN_BEHANDELING, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
		contracts.add(new Contract(contractTypes.get(2), ContractStatus.IN_BEHANDELING, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
	}
	
	private static Stream<Arguments> contractTypeCountFixture(){ 
        return Stream.of( 
                Arguments.of(new ContractType("naam", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500), 1), 
                Arguments.of(new ContractType("naam2", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500), 2), 
                Arguments.of(new ContractType("naam3", ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, 20, 10, 500), 3) 
        ); 
}
	
	@Test
    public void getAllCompanies_returnsAllCompanies(){
		Mockito.when(companyRepo.findAll()).thenReturn(companies);
		List<ICompany> result = contractService.getAllCompanies();
		Mockito.verify(companyRepo, Mockito.times(1)).findAll();
		Assertions.assertThat(result).isEqualTo(companies);
        Assertions.assertThat(result.size()).isEqualTo(3);
	}
	
	@Test
    public void getAllContracts_returnsAllContracts(){
		Mockito.when(contractRepo.findAll()).thenReturn(contracts);
		List<IContract> result = contractService.getAllContracts();
		Mockito.verify(contractRepo, Mockito.times(1)).findAll();
		Assertions.assertThat(result).isEqualTo(contracts);
        Assertions.assertThat(result.size()).isEqualTo(6);
	}
	
	@Test
    public void getAllContractTypes_returnsAllContractTypes(){
		Mockito.when(contractTypeRepo.findAll()).thenReturn(contractTypes);
		List<IContractType> result = contractService.getAllContractTypes();
		
		Assertions.assertThat(result).isEqualTo(contractTypes);
        Assertions.assertThat(result.size()).isEqualTo(3);
	}
	
//	@ParameterizedTest
//	@CsvSource({"0,1","1,2","2,3"})
//	public void getActiveContractCountByContractType_returnsCount(int indexContractType, int expected) {
//		int result = contractService.getActiveContractCountByContractType(FXCollections.observableArrayList(contracts), this.contractTypes.get(indexContractType));
//		Assertions.assertThat(result).isEqualTo(expected);
//	}
	
	@Test
	public void editContractType_updatesContractTypeRepo() {
		ContractType contractType = contractTypes.get(0);
		EditContractTypeDto command = new EditContractTypeDto(1, "naam", 20, 10, 500, ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, ContractTypeStatus.ACTIEF);
		contractService.editContractType(command, contractType);
		Mockito.verify(contractTypeRepo, Mockito.times(1)).update(contractType);
	}
	
	@Test
	public void createContractType_insertsContractType() {
		ContractType contractType = contractTypes.get(0);
		CreateContractTypeDto command = new CreateContractTypeDto("naam", 20, 10, 500, ManierAanmakenTicket.APPLICATIE, GedekteTijdstippen.ALTIJD, ContractTypeStatus.ACTIEF);
		contractType.setStatus(ContractType.ContractTypeStatus.NIET_ACTIEF);
		contractService.createContractType(command);
		Mockito.verify(contractTypeRepo, Mockito.times(1)).insert(refEq(contractType));
	}

}
