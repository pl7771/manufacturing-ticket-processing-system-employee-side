package org.tile.ticketing_system.domein.repository;

import org.tile.ticketing_system.domein.Client;

public class ClientDaoJpa extends GenericDaoJpa<Client> {

	public ClientDaoJpa() {
		super(Client.class);
	}
}
