package org.tile.ticketing_system.domein.interfaces;

import org.tile.ticketing_system.domein.Company;
import org.tile.ticketing_system.domein.User;

public interface IClient {
    String getClientId();

    User getApplicationUser();

    Company getCompany();
}
