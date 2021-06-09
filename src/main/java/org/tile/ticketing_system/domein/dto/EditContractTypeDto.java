package org.tile.ticketing_system.domein.dto;

import lombok.Value;
import org.tile.ticketing_system.domein.ContractType;

@Value
public class EditContractTypeDto {
    int contractTypeId;
    String name;
    double maxAfhaandeldTijd;
    double minDoorloooptijd;
    double prijs;
    ContractType.ManierAanmakenTicket manierAanmakenTicket;
    ContractType.GedekteTijdstippen gedekteTijdstippen;
    ContractType.ContractTypeStatus contractTypeStatu;
}
