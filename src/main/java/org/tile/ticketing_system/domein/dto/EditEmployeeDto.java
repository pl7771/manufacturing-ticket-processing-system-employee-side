package org.tile.ticketing_system.domein.dto;

import org.tile.ticketing_system.domein.User;
import org.tile.ticketing_system.domein.interfaces.IRole;

import lombok.Value;

@Value
public class EditEmployeeDto {
    String userId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    IRole employeeRole;
    String address;
    User.StatusGebruiker statusGebruiker;
}
