package org.tile.ticketing_system.domein;

import static org.tile.ticketing_system.domein.Role.RoleType.ADMINISTRATOR;
import static org.tile.ticketing_system.domein.Role.RoleType.CLIENT;
import static org.tile.ticketing_system.domein.Role.RoleType.SUPPORTMANAGER;
import static org.tile.ticketing_system.domein.Role.RoleType.TECHNICIAN;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.tile.ticketing_system.domein.interfaces.IRole;
import org.tile.ticketing_system.domein.interfaces.IUser;

import com.amdelamar.jhash.Hash;
import com.amdelamar.jhash.exception.InvalidHashException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.java.Log;

@Entity
@Table(name = "[Identity].[User]")
//@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log
public class User implements Serializable, IUser {

	@Id
	private String id;
	private String userName;
	private String passwordHashForJava;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime lastSession;
	private String statusGebruiker;

	// .NET
	private String normalizedUserName;
	private String email;
	private String normalizedEmail;
	private String passwordHash;
	private String securityStamp;
	private String concurrencyStamp;
	private Boolean emailConfirmed;
	private Boolean phoneNumberConfirmed;
	private Boolean twoFactorEnabled;
	private Boolean lockoutEnabled;
	private int accessFailedCount;

	@OneToMany
	@JoinTable(name = "[Identity].[UserRoles]", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
	private List<Role> userRoles = new ArrayList<>();

	@OneToOne(mappedBy = "applicationUser")
	private Employee employee;

	// Constructors
	public User(String userName, String passwordHashForJava, String firstName, String lastName) {
		this.id = UUID.randomUUID().toString();
		setUserName(userName);
		setFirstName(firstName);
		setLastName(lastName);
		setLastSession(new Date());
		this.passwordHashForJava = passwordHashForJava;
		setStatus(StatusGebruiker.GEBLOKKEERD); // bij creatie Geblokkeerd

		// .NET
		this.securityStamp = "34DTUCSWLZBFVFMUF3RIXUIXDRBPI4FH";
		this.concurrencyStamp = "173c4421-9d18-4d01-afce-f8e3d5646edb";
		this.passwordHash = "AQAAAAEAACcQAAAAENLQcxQU14Ieh7Px8SAExHnlNWXucg5LP5pre38fyyjv/wh/cON/s6nI+8PgpTNRRQ==";
		this.emailConfirmed = true;
		this.phoneNumberConfirmed = true;
		this.twoFactorEnabled = false;
		this.lockoutEnabled = true;
		this.accessFailedCount = 0;

	}

	public User(String userName, String firstName, String lastName) { // standaard wachtwoord, de gebruiker dient zijn wacthwoord opnieuw in te stellen
		this(userName, "pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
				firstName, lastName);
	}

	public User(String userName, String firstName, String lastName, String phoneNumber, boolean phoneNumberConfirmed) { // indien tel voor Employee niet in Employee class aangezien in .NET dit standaard reeds is ingebouwd in de IdentityUser
		this(userName, "pbkdf2sha256:64000:18:24:n:zd3QoH+pMenpqco380Tdy0fb75e2A5dn:6KkSFnEnXTWfaW2BAjhhYV1n",
				firstName, lastName);
		setPhoneNumber(phoneNumber);
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}

	private void setUserName(String userName) {
		if (!isEmailValid(userName)) // UserName = email
			throw new IllegalArgumentException("Username(email) is niet correct");
		this.userName = userName;
		// .NET
		this.normalizedUserName = userName.trim().toUpperCase();
		this.email = userName;
		this.normalizedEmail = this.normalizedUserName;
	}

	private void setFirstName(String firstName) {
		if (!isNameValid(firstName))
			throw new IllegalArgumentException("Voornaam is verplicht");
		this.firstName = firstName;
	}

	private void setLastName(String lastName) {
		if (!isNameValid(lastName))
			throw new IllegalArgumentException("Naam is verplicht");
		this.lastName = lastName;
	}

	private void setPhoneNumber(String phoneNumber) {
		if (!isPhoneNumberValid(phoneNumber))
			throw new IllegalArgumentException("Telefoonnummer is niet correct");
		this.phoneNumber = phoneNumber;
	}

	public void setLastSession(Date lastSession) {
		this.lastSession = Instant.ofEpochMilli(lastSession.getTime())
			     .atZone(ZoneId.systemDefault())
			     .toLocalDateTime();
	}
	
	public void setLastSession(LocalDateTime lastSession) {
		this.lastSession = lastSession;
	}

	private void setStatus(StatusGebruiker statusGebruiker) {
		this.statusGebruiker = statusGebruiker.toString();
	}

	// private methods
	private boolean isNameValid(String string) {
		return (string != null) && string.matches("[A-Za-z0-9_]+");
	}

	private boolean isEmailValid(String email)// UserName = email
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	private boolean isPasswordHashValid(String password) throws InvalidHashException {
		return Hash.password(password.toCharArray()).verify(this.passwordHashForJava);
	}

	private boolean isUserActive() {
		return StatusGebruiker.valueOf(this.statusGebruiker).equals(StatusGebruiker.ACTIEF);
	}

	private boolean isPhoneNumberValid(String phoneNumber) {
		String phoneNumberRegex = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";
		Pattern pat = Pattern.compile(phoneNumberRegex);
		if (phoneNumber == null)
			return false;
		return pat.matcher(phoneNumber).matches();
	}

	// public methods
	public boolean validatePassword(String passwordHash) {
		try {
			return isPasswordHashValid(passwordHash) && isUserActive();
		} catch (InvalidHashException e) {
			log.info("Incorrectly formatted hash was given");
			return false;
		}
	}

	public boolean isKlant() {
		return userRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(CLIENT.name()));
	}

	public boolean isTechnician() {
		return userRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(TECHNICIAN.name()));
	}

	public boolean isAdministrator() {
		return userRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(ADMINISTRATOR.name()));
	}

	public boolean isSupportManager() {
		return userRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase(SUPPORTMANAGER.name()));
	}

	public Role getRole() {
		return this.userRoles.stream().findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Geen rollen gevonden!"));
	}

	public void setRole(IRole role) {
		//validatie role verschillend van null? //ja
		if(role == null)throw new IllegalArgumentException("Je moet een role toekennen aan de werknemer");
		this.userRoles.clear();
		this.userRoles.add((Role) role);
	}

	public void editGebruiker(String firstName, String lastName, String email, StatusGebruiker statusGebruiker) {
		setUserName(email);
		setFirstName(firstName);
		setLastName(lastName);
		setStatus(statusGebruiker);
	}

	public void editGebruiker(String firstName, String lastName, String email, StatusGebruiker statusGebruiker,
			String phoneNumber, IRole role) { // voor Employee
		setUserName(email);
		setFirstName(firstName);
		setLastName(lastName);
		setStatus(statusGebruiker);
		setPhoneNumber(phoneNumber);
		setRole(role);
	}

    public void setEmployee(Employee employee) {
		this.employee = employee;
    }

    public enum StatusGebruiker {
		GEBLOKKEERD, ACTIEF;
	}
}
