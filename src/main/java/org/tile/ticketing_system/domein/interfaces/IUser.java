package org.tile.ticketing_system.domein.interfaces;

import java.time.LocalDateTime;

import org.tile.ticketing_system.domein.Employee;
import org.tile.ticketing_system.domein.Role;

public interface IUser {
    boolean isKlant();

    boolean isTechnician();

    boolean isAdministrator();

    boolean isSupportManager();

    Role getRole();

    String getId();

    String getUserName();

    String getPasswordHashForJava();

    String getFirstName();

    String getLastName();

    String getPhoneNumber();

    LocalDateTime getLastSession();

    String getStatusGebruiker();

    String getNormalizedUserName();

    String getEmail();

    String getNormalizedEmail();

    String getPasswordHash();

    String getSecurityStamp();

    String getConcurrencyStamp();

    Boolean getEmailConfirmed();

    Boolean getPhoneNumberConfirmed();

    Boolean getTwoFactorEnabled();

    Boolean getLockoutEnabled();

    int getAccessFailedCount();

    java.util.List<Role> getUserRoles();

    Employee getEmployee();

    String toString();
}
