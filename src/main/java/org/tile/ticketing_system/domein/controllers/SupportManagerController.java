package org.tile.ticketing_system.domein.controllers;

import java.util.Map;

import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateContractTypeDto;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditContractTypeDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;
import org.tile.ticketing_system.domein.interfaces.IContractType;

public class SupportManagerController extends DomainControllerAbstraction {

	private final Actemium actemium;

	protected SupportManagerController(Actemium actemium, User loggedInUser) {
		super(actemium, loggedInUser);
		this.actemium = super.getActemium();
	}

    @Override
    public void createTicket(CreateTicketDto createTicketDto) {
        this.actemium.addTicket(createTicketDto);
    }

    @Override
    public void editTicket(EditTicketDto editTicketDto) {
        this.actemium.editTicket(editTicketDto);
    }
    
    @Override
    public void updateLastSession() {
		this.actemium.updateLastSession();
	}
    
    @Override
    public long getTicketWijzigingen() {
		return this.actemium.getTicketWijzigingen();
	}

    @Override
    public int getActiveContractCountByContractType(IContractType type) {
        return this.actemium.getActiveContractCountByContractType(type);
    }

    @Override
    public int getCompletedTicketsCountPerContractType(IContractType type) {
        return this.actemium.getCompletedTicketsCountPerContractType(type);
    }

    @Override
    public int getPercentageTicketsCompletedInTimePerContractType(IContractType type) {
        return this.actemium.getPercentageTicketsCompletedInTimePerContractType(type);
    }

    @Override
    public void editContractType(EditContractTypeDto editContractTypeDto) {
        this.actemium.editContractType(editContractTypeDto);
    }

    @Override
    public void createContractType(CreateContractTypeDto createContractTypeDto) {
        this.actemium.addContractType(createContractTypeDto);
    }
    
    //STATISTIEK
    @Override
    public Map<String, Long> getStatStatusTicket(){
		return this.actemium.getStatStatusTicket();
	}
    
    @Override
    public Map<String, Long> getStatContractTypeTicket(){
		return this.actemium.getStatContractTypeTicket();
	}
    
    @Override
    public Map<String, Double> getStatGemiddeldeLooptijd(){
		return this.actemium.getStatGemiddeldeLooptijd();
	}
    
    @Override
    public Map<String, Integer> getStatTicketsAfgewerktPerMaand(){
		return this.actemium.getStatTicketsAfgewerktPerMaand();
	}
	
    @Override
	public Map<String, Integer> getStatTicketsAangemaaktPerMaand(){
    	return this.actemium.getStatTicketsAangemaaktPerMaand();
	}
    
    @Override
    public Map<String, Integer> getStatCompletedInTimePercentageContractTypes (){
    	return this.actemium.getStatCompletedInTimePercentageContractTypes();
    }
    
    @Override
	public Map <String, Integer> getStatPercentageTicketsCompletedInTimePerMonth() {
		return this.actemium.getStatPercentageTicketsCompletedInTimePerMonth();
	}
    
    @Override
    public Map<String, Double> getStatTicketsGemiddeldeOpenstaandPerMaand(){
		return this.actemium.getStatTicketsGemiddeldeOpenstaandPerMaand();
	}
    
    @Override
    public Map<String, Double> getStatContractenGemiddeldeLopendPerMaand(){
		return this.actemium.getStatContractenGemiddeldeLopendPerMaand();
	}
    
    //KPI
    @Override
    public Map<String, String> getKPITicketsAfgewerktBinnenTijd(){
		return this.actemium.getKPITicketsAfgewerktBinnenTijd();
    }
	
    @Override
	public Map<String, String> getKPITicketsOpenstaandPerDag(){
    	return this.actemium.getKPITicketsOpenstaandPerDag();
	}
    
    @Override
    public Map<String, String> getKPIContractenLopendePerDag(){
		return this.actemium.getKPIContractenLopendePerDag();
	}
}
