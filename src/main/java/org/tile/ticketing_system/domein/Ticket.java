package org.tile.ticketing_system.domein;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.tile.ticketing_system.domein.interfaces.ITicket;
@Entity // entity class
@Table(name = "[Identity].[Ticket]")
public class Ticket implements Serializable, ITicket{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TicketId")
	private int ticketId;

	private String employeeId;

	@OneToOne
	@JoinColumn(name = "CompanyId")
	private Company company;
	@OneToOne
	@JoinColumn(name = "ContractId")
	private Contract contract;
	private String imageDescription;
	@Column(name = "Titel")
	private String titel;
	// verplicht
	private String omschrijving;
	private String opmerkingen;
	private boolean isGewijzigd;
	private LocalDateTime datumAangemaakt;
	private LocalDateTime datumAfgesloten;
	private LocalDateTime datumGewijzigd;

	@OneToOne
	@JoinColumn(name = "BijlageId")
	public Bijlage bijlage;

	private Status status;
	private int aantalWijzigingen;



	protected Ticket() {
	} // default constructor voor JPA

	public Ticket(LocalDateTime datumAangemaakt, String titel, String omschrijving) {
		setDatumAangemaakt(datumAangemaakt);

		// setDatumAfgesloten(??)

		setTitel(titel);
		setOmschrijving(omschrijving);
	}

	public Ticket(LocalDateTime datumAangemaakt, String titel, String omschrijving, Status status) {
		setDatumAangemaakt(datumAangemaakt);

		// setDatumAfgesloten(??)

		setTitel(titel);
		setOmschrijving(omschrijving);
		setStatus(status);
		this.isGewijzigd = false;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public int getTicketId() {
		return ticketId;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		if (titel.trim().isEmpty() || titel.length() > 100)
			throw new IllegalArgumentException("Titel mag niet leeg zijn of langer dan 100 karakters");
		this.titel = titel;
	}

	public String getOmschrijving() {
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		if (omschrijving.trim().isEmpty())
			throw new IllegalArgumentException("Omschrijving mag niet leeg zijn");
		this.omschrijving = omschrijving;
	}

	public String getOpmerkingen() {
		return opmerkingen;
	}

	public void setOpmerkingen(String opmerkingen) {
		this.opmerkingen = opmerkingen;
	}

	public boolean isGewijzigd() {
		return isGewijzigd;
	}

	public void setGewijzigd(boolean isGewijzigd) {
		this.isGewijzigd = isGewijzigd;
	}

	public LocalDateTime getDatumAangemaakt() {
		return datumAangemaakt;
	}

	public void setDatumAangemaakt(LocalDateTime datumAangemaakt) {

		if (datumAangemaakt == null) {
			this.datumAangemaakt = LocalDateTime.now();
		} else if (datumAangemaakt != null && datumAangemaakt.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Datum aanmaak ticket mag niet in de toekomst liggen");
		} else {
			this.datumAangemaakt = datumAangemaakt;
		}
	}

	public LocalDateTime getDatumAfgesloten() {
		return datumAfgesloten;
	}

	public void setImageDescription(String imageDescription) {
		this.imageDescription = imageDescription;
	}

	public void setBijlage(Bijlage bijlage) {
		this.bijlage = bijlage;
	}

	public void setDatumAfgesloten(LocalDateTime datumAfgesloten) {
		if (datumAfgesloten.isBefore(this.datumAangemaakt))
			throw new IllegalArgumentException("Ongeldige afsluitdatum");
		this.datumAfgesloten = datumAfgesloten;
	}

	public LocalDateTime getDatumGewijzigd() {
		return datumGewijzigd;
	}

	public void setDatumGewijzigd(LocalDateTime datumGewijzigd) {
		// if (datumGewijzigd.after(new Date()))
		// throw new IllegalArgumentException("Datum wijziging moet het huidige tijdstip
		// zijn");
		this.datumGewijzigd = datumGewijzigd;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getAantalWijzigingen() {
		return aantalWijzigingen;
	}

	public void setAantalWijzigingen(int aantalWijzigingen) {
		this.aantalWijzigingen = aantalWijzigingen;
	}

	public String getImageDescription() {
		return imageDescription;
	}
	
	public Bijlage getBijlage() {
		return bijlage;
	}

	public boolean kanWijzigen() {
		if (this.status == Status.AANGEMAAKT || status == Status.IN_BEHANDELING)
			return true;
		else
			return false;
	}

	private boolean ticketUpdated(Ticket ticket, Ticket tevm) // voorlopig als tweede parameter gewoon ticket
	{
		return tevm.titel.equals(ticket.titel) || tevm.omschrijving.equals(tevm.omschrijving)
				|| tevm.opmerkingen.equals(tevm.opmerkingen) || tevm.status.equals(tevm.status)
				|| tevm.imageDescription.equals(ticket.imageDescription);
		// || tevm.MyFile != null;
	}



	public Ticket editTicket(String newTitel, String newOmschrijving, String newOpmerkingen, Ticket.Status newStatus,
			String newBijlageDescription, Bijlage bijlage) {
		setGewijzigd(true);
		setTitel(newTitel);
		setOmschrijving(newOmschrijving);
		setOpmerkingen(newOpmerkingen);
		setStatus(newStatus);
		setImageDescription(newBijlageDescription);
		setBijlage(bijlage);
		setDatumGewijzigd(LocalDateTime.now());
		this.aantalWijzigingen++;
		return this;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 41 * hash + this.ticketId;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Ticket other = (Ticket) obj;
		return this.ticketId == other.ticketId;
	}

	@Override
	public String toString() {
		return "Ticket [ticketId=" + ticketId + ", titel=" + titel + ", omschrijving=" + omschrijving + ", opmerkingen="
				+ opmerkingen + ", isGewijzigd=" + isGewijzigd + ", datumAangemaakt=" + datumAangemaakt
				+ ", datumAfgesloten=" + datumAfgesloten + ", datumGewijzigd=" + datumGewijzigd + ", status=" + status
				+ ", aantalWijzigingen=" + aantalWijzigingen + ", company=" + company + ", Contract=" + contract
				+ ", imageDescription=" + imageDescription
				+ ", BIJLAGE="+ bijlage +"]";
	}

	public void setEmployeeId(String s) {
		this.employeeId = s;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public enum Status {
		AANGEMAAKT, IN_BEHANDELING, AFGEHANDELD, GEANNULEERD
	}

}
