package org.tile.ticketing_system.domein.repository;

import org.tile.ticketing_system.domein.ContractType;

public class ContractTypeDaoJpa extends GenericDaoJpa<ContractType> {

	public ContractTypeDaoJpa() {
		super(ContractType.class);
	}
}
