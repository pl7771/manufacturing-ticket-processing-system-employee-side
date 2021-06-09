package org.tile.ticketing_system.domein.controllers;

import org.tile.ticketing_system.domein.Actemium;
import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.dto.CreateTicketDto;
import org.tile.ticketing_system.domein.dto.EditTicketDto;

public class TechnicianController extends DomainControllerAbstraction {

	private final Actemium actemium;

	protected TechnicianController(Actemium actemium, User loggedInUser) {
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
}
