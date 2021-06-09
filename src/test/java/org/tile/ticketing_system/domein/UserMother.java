package org.tile.ticketing_system.domein;

import java.lang.reflect.Field;
import java.util.List;

import com.amdelamar.jhash.Hash;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mockito.Mockito;
import org.tile.ticketing_system.domein.interfaces.IRole;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;
import static org.tile.ticketing_system.domein.Role.RoleType.CLIENT;
import static org.tile.ticketing_system.domein.Role.RoleType.SUPPORTMANAGER;
import static org.tile.ticketing_system.domein.Role.RoleType.TECHNICIAN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMother {

    public static User aUserWithPassword(String password) throws IllegalAccessException, NoSuchFieldException {
        String passwordhash = Hash.password(password.toCharArray()).create();
        User newUser = new User("aUserName@gmail.com", passwordhash, "Jan", "Janssens");

        Field statusField = newUser.getClass().getDeclaredField("statusGebruiker");
        statusField.setAccessible(true);
        statusField.set(newUser, "ACTIEF");
        return newUser;
    }

    public static User aUserWithRole(Role.RoleType roleType) throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithPassword("password");
        Role role;
        switch (roleType) {
            case CLIENT:
                role = new Role(CLIENT);
                break;
            case ADMINISTRATOR:
                role = new Role(ADMINISTRATOR);
                break;
            case TECHNICIAN:
                role = new Role(TECHNICIAN);
                break;
            default:
                role = new Role(SUPPORTMANAGER);
                break;
        }
        user.setRole(role);
        return user;
    }

    public static User aUserWithIdAndRole(String id, Role.RoleType roleType) throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(roleType);

        Field idField = user.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
        return user;
    }

    public static Employee anEmployeeWithUserId(String id) throws NoSuchFieldException, IllegalAccessException {
        return new Employee(aUserWithIdAndRole(id, TECHNICIAN), "technicianAddress");
    }

    public static Client aClientWithUserId(String id) throws NoSuchFieldException, IllegalAccessException {
        return new Client(aUserWithIdAndRole(id, CLIENT), new Company("Coca", "eenAddress", "0488556677"));
    }

    public static List<IRole> allRoles() {
        return List.of(
                new Role(CLIENT),
                new Role(SUPPORTMANAGER),
                new Role(ADMINISTRATOR),
                new Role(TECHNICIAN));
    }
}
