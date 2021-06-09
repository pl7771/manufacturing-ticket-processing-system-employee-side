package org.tile.ticketing_system.domein.repository;

import org.tile.ticketing_system.domein.Company;

public class CompanyDaoJpa extends GenericDaoJpa<Company> {

	public CompanyDaoJpa() {
		super(Company.class);
	}
}
