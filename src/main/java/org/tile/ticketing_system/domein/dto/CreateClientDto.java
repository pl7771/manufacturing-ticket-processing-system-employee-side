package org.tile.ticketing_system.domein.dto;

import lombok.Value;

@Value
public class CreateClientDto {
    String company;
    String firstName;
    String lastName;
    String email;
}
