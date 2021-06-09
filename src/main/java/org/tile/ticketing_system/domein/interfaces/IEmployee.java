package org.tile.ticketing_system.domein.interfaces;

import org.tile.ticketing_system.domein.User;

public interface IEmployee {
    String getEmployeeId();

    User getApplicationUser();

    String getAddress();

    String toString();
}
