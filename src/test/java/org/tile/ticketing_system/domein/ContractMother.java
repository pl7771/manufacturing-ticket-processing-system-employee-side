package org.tile.ticketing_system.domein;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContractMother {

    public static ContractType aContractTypeWithId(int id) throws NoSuchFieldException, IllegalAccessException {
       ContractType contractType = new ContractType("eenContractType", ContractType.ManierAanmakenTicket.EMAIL, ContractType.GedekteTijdstippen.ALTIJD, 20, 20, 20);

        Field contractTypeIdField = contractType.getClass().getDeclaredField("contractTypeId");
        contractTypeIdField.setAccessible(true);
        contractTypeIdField.set(contractType, id);
        return contractType;
    }

    public static Contract anActiveContract(ContractType contractType) throws NoSuchFieldException, IllegalAccessException {
        return new Contract(contractType, Contract.ContractStatus.LOPEND, LocalDateTime.now(), LocalDateTime.now().plusDays(10));
    }
}
