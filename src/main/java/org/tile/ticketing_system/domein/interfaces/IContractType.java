package org.tile.ticketing_system.domein.interfaces;

import org.tile.ticketing_system.domein.ContractType;

public interface IContractType {
    String getName();

    int getContractTypeId();

    ContractType.ManierAanmakenTicket getManierAanmakenTicket();

    ContractType.GedekteTijdstippen getGedekteTijdstippen();

    double getMaximaleAfhandeltijd();

    double getMinimaleDoorlooptijd();

    double getPrijs();

    int getAantalContracten();

    ContractType.ContractTypeStatus getStatus();

    @Override
    String toString();
}
