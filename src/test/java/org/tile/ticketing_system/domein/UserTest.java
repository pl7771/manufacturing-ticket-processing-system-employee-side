package org.tile.ticketing_system.domein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;
import static org.tile.ticketing_system.domein.Role.RoleType.CLIENT;
import static org.tile.ticketing_system.domein.Role.RoleType.SUPPORTMANAGER;
import static org.tile.ticketing_system.domein.Role.RoleType.TECHNICIAN;
import static org.tile.ticketing_system.domein.UserMother.aUserWithPassword;
import static org.tile.ticketing_system.domein.UserMother.aUserWithRole;

import org.assertj.core.api.Assertions;

class UserTest {
	
	@ParameterizedTest
	//arange
	@NullSource
	@EmptySource
	@ValueSource(strings={"_test..test@test.be",//2x dot
			"@test@test.be", //begin with other character than a-zA-Z0-9_+&*-
			"test.be", //without @
			"test@test", //second part without dot
			"test@test.b", //min 2 characters at end
			"test@test.abcdefgh" //max 7 characters at end
	})
	void newUserInvalidUserNameThrowsIllegalArgumentException(String userName) {
		//act and assert
		assertThrows(IllegalArgumentException.class, ()->
				new User(userName,
						"pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
						"firstName", "lastName")
		);
		assertThrows(IllegalArgumentException.class, ()-> new User(userName, "firstName", "lastName"));
	}
	
	@ParameterizedTest
	//arange
	@ValueSource(strings={"test@test.be",
			"_test@test.be", //begin with _+&*-
			"9test@test.be", //begin with number
			"_test.test@test.be", // with dot, not at beginning
			"_test&&test@test.be", //with _+&*- in first part
			"test@test.ab", //min 2 characters at end
			"test@test.abcdefg"}) //max 7 characters at end
	void newUserValidUserNameCreatesUser(String userName) {
		//act
		User user = new User(userName,
				"pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
				"firstName", "lastName");
		User user2 = new User(userName, "firstName", "lastName");
		//asert
		assertEquals(User.class, user.getClass());
		assertEquals(User.class, user2.getClass());
	}
	
	@ParameterizedTest
	//arange
	@NullSource
	@EmptySource
	@ValueSource(strings={"@naam",//begin with special char (ex _)
			"naam@naam.be",//contains special char (ex _)
			" naam",//space at begin
			"naam naam"})//space between
	void newUserInvalidFirstNameThrowsIllegalArgumentException(String firstName) {
		//act and assert
		assertThrows(IllegalArgumentException.class, ()->
				new User("test@test.be",
						"pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
						firstName, "lastName")
		);
		assertThrows(IllegalArgumentException.class, ()-> new User("test@test.be", firstName, "lastName"));
	}
	
	@ParameterizedTest
	//arange
	@NullSource
	@EmptySource
	@ValueSource(strings={"@naam",//begin with special char (ex _)
			"naam@naam.be",//contains special char (ex _)
			" naam",//space at begin
			"naam naam"})//space between
	void newUserInvalidLastNameThrowsIllegalArgumentException(String lastName) {
		//act and assert
		assertThrows(IllegalArgumentException.class, ()->
				new User("test@test.be",
						"pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
						"firstName", lastName)
		);
		assertThrows(IllegalArgumentException.class, ()-> new User("test@test.be", "firstName", lastName));
	}
	
	@ParameterizedTest
	//arange
	@CsvSource({"firsName, lastName", //only letters
		"_firstName, lastName", "firstName, _lastName", //with _
		"10firstName, lastName", "firstName, 10LastName", "first1234name, last1234name"}) //with digits
	void newUserValidFirstAndLastNameCreatesUser(String firstName, String lastName) {
		//act
		User user = new User("test@test.be",
				"pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
				firstName, lastName);
		User user2 = new User("test@test.be", firstName, lastName);
		//asert
		assertEquals(User.class, user.getClass());
		assertEquals(User.class, user2.getClass());
	}
	
	@ParameterizedTest
	//arange
	@ValueSource(strings={"+919367788755",
			"8989829304",
			"+16308520397",
			"786-307-3615",
			"+32470687560"
			})
	void newUserValidPhoneNumberCreatesUser(String phoneNumber) {
		//act
		User user = new User("test@test.be",
				"firstName", "lastName", phoneNumber, true);
		//asert
		assertEquals(User.class, user.getClass());
	}
	
	@ParameterizedTest
	//arange
	@NullSource
	@EmptySource
	@ValueSource(strings={"789",
			"123765",
			"1-1-1",
			"+982",
			"+324706875",})
	void newUserInvalidPhoneNumberThrowsIllegalArgumentException(String phoneNumber) {
		//act and assert
		assertThrows(IllegalArgumentException.class, ()->
				new User("test@test.be",
						"firstName", "lastName", phoneNumber, true)
		);
	}

    @Test
    void shouldValidatePassword() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithPassword("password");
        assertTrue(user.validatePassword("password"));
    }

    @Test
    void isKlant() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(CLIENT);
        assertTrue(user.isKlant());
    }

    @Test
    void isTechnician() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(TECHNICIAN);
        assertTrue(user.isTechnician());
    }

    @Test
    void isAdministrator() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(ADMINISTRATOR);
        assertTrue(user.isAdministrator());
    }

    @Test
    void isSupportManager() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(SUPPORTMANAGER);
        assertTrue(user.isSupportManager());
    }

    @Test
    void shouldAddRole() throws NoSuchFieldException, IllegalAccessException {
        User user = aUserWithRole(SUPPORTMANAGER);
		assertTrue(user.isSupportManager());

		user.setRole(new Role(ADMINISTRATOR));
        assertTrue(user.isAdministrator());
    }
}
