package org.tile.ticketing_system.domein.dto;

import lombok.Value;
import org.tile.ticketing_system.domein.User;

@Value
public class EditClientDto {

	String userId;
	String companyName;
	String firstName;
	String lastName;
	String email;
	User.StatusGebruiker statusGebruiker;
}
