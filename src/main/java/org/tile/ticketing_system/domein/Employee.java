package org.tile.ticketing_system.domein;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tile.ticketing_system.domein.dto.EditEmployeeDto;
import org.tile.ticketing_system.domein.interfaces.IEmployee;


@Entity
@Table(name = "[Identity].[Employees]")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee implements Serializable, IEmployee {

	@Id
	private String employeeId;
	@OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	@JoinColumn(name = "ApplicationUserId", referencedColumnName = "Id")
	private User applicationUser;
	private String address;

	public Employee(User applicationUser, String address) {
		this.employeeId = UUID.randomUUID().toString();
		this.applicationUser = applicationUser;
		this.address = address;
	}

	public Employee editEmployee(EditEmployeeDto dto) {
		this.applicationUser.editGebruiker(
				dto.getFirstName(),
				dto.getLastName(),
				dto.getEmail(),
				dto.getStatusGebruiker(),
				dto.getPhoneNumber(),
				dto.getEmployeeRole());
		this.address = dto.getAddress();
		return this;
	}

}
