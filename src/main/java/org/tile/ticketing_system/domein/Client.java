package org.tile.ticketing_system.domein;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.tile.ticketing_system.domein.dto.EditClientDto;
import org.tile.ticketing_system.domein.interfaces.IClient;


@Entity
@Table(name = "[Identity].[Clients]")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client implements IClient {

	@Id
	private String clientId;
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name="ApplicationUserId")
	private User applicationUser;
	@OneToOne
	@JoinColumn(name="CompanyId")
	private Company company;
	
	public Client(User applicationUser, Company company) {
		this.applicationUser = applicationUser;
		this.company = company;
	}
	
	private void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@PrePersist
	private void ensureId(){
	    this.setClientId(UUID.randomUUID().toString());
	}

	public void editClient(Company company, EditClientDto dto) {
		this.applicationUser.editGebruiker(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getStatusGebruiker());
		this.company = company;
	}
}
