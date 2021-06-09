package org.tile.ticketing_system.domein.services;

import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.commitTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.rollbackTransaction;
import static org.tile.ticketing_system.domein.repository.GenericDaoJpa.startTransaction;

import java.util.ArrayList;
import java.util.List;

import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.ContractType;
import org.tile.ticketing_system.domein.Ticket;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.interfaces.ICompany;
import org.tile.ticketing_system.domein.interfaces.IContract;
import org.tile.ticketing_system.domein.interfaces.IContractType;
import org.tile.ticketing_system.domein.interfaces.ITicket;
import org.tile.ticketing_system.domein.repository.CompanyDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractDaoJpa;
import org.tile.ticketing_system.domein.repository.ContractTypeDaoJpa;

import lombok.extern.java.Log;

@Log
public class ContractService {

    private final CompanyDaoJpa companyRepo;
    private final ContractDaoJpa contractRepo;
    private final ContractTypeDaoJpa contractTypeRepo;


    public ContractService(CompanyDaoJpa companyRepo, ContractDaoJpa contractRepo, ContractTypeDaoJpa contractTypeRepo) {
        this.companyRepo = companyRepo;
        this.contractRepo = contractRepo;
        this.contractTypeRepo = contractTypeRepo;
    }

    public List<ICompany> getAllCompanies() {
        startTransaction();
        var allCompanies = this.companyRepo.findAll();
        List<ICompany> allCompaniesPerInterface = new ArrayList<>();
        for (Company company : allCompanies) {
        	var icompany = (ICompany) (Object) company;
        	allCompaniesPerInterface.add(icompany);
        }
        commitTransaction();
        return allCompaniesPerInterface;
    }

    public List<IContract> getAllContracts() {
        startTransaction();
        var allContracts = this.contractRepo.findAll();
        List<IContract> allContractsPerInterface = new ArrayList<>();
        for (Contract contract : allContracts) {
        	var icontract = (IContract) (Object) contract;
        	allContractsPerInterface.add(icontract);
        }
        commitTransaction();
        return allContractsPerInterface;
    }

    public List<IContractType> getAllContractTypes() {
        startTransaction();
        
        List<IContractType> allContractTypesPerInterface = new ArrayList<>();
        
        var allContractTypes = this.contractTypeRepo.findAll();
        
        for (ContractType type : allContractTypes) {
        	var iContractType = (IContractType) (Object) type;
        	allContractTypesPerInterface.add(iContractType);
        }
        
        commitTransaction();
        return allContractTypesPerInterface;
    }

    public void editContractType(EditContractTypeDto command, ContractType contractTypeToEdit) {
        (contractTypeToEdit).editContractType(command.getName(), command.getMaxAfhaandeldTijd(), command.getMinDoorloooptijd(),
                                            command.getPrijs(),command.getManierAanmakenTicket(),
                command.getGedekteTijdstippen(), command.getContractTypeStatu());
        try {
            startTransaction();
            this.contractTypeRepo.update(contractTypeToEdit);
            commitTransaction();
        } catch (Exception exception) {
            rollbackTransaction();
        }
        log.warning("Contract type successfully edited to: " + contractTypeToEdit);
    }

    public ContractType createContractType(CreateContractTypeDto command) {
        var contractType = new ContractType(command.getName(), command.getManierAanmakenTicket(), command.getGedekteTijdstippen(),
                command.getMaxAfhaandeldTijd(),command.getMinDoorloooptijd(),
                command.getPrijs());
        contractType.setStatus(ContractType.ContractTypeStatus.NIET_ACTIEF); //niet actief bij het aanmaken?
        try {
            startTransaction();
            this.contractTypeRepo.insert(contractType);
            commitTransaction();
        } catch (Exception exception) {
            rollbackTransaction();
        }
        return contractType;
    }
}
