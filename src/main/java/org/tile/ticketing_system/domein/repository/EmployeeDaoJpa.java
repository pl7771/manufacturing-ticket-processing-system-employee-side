package org.tile.ticketing_system.domein.repository;

import org.tile.ticketing_system.domein.Employee;

public class EmployeeDaoJpa extends GenericDaoJpa<Employee> {

	public EmployeeDaoJpa() {
		super(Employee.class);
	}
}
