package org.tile.ticketing_system.domein.interfaces;

import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.Contract;
import org.tile.ticketing_system.domein.ContractType;

import java.time.LocalDateTime;

public interface IContract {
    Company getCompany();

    LocalDateTime getStartDatum();

    LocalDateTime getEindDatum();

    int getContractId();

    ContractType getType();

    Contract.ContractStatus getStatus();

    boolean vervaltBinnenkort();

    @Override
    String toString();
}
