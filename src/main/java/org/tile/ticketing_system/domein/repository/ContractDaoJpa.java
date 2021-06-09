package org.tile.ticketing_system.domein.repository;

import org.tile.ticketing_system.domein.Contract;

public class ContractDaoJpa extends GenericDaoJpa<Contract> {

	public ContractDaoJpa() {
		super(Contract.class);
	}
}
