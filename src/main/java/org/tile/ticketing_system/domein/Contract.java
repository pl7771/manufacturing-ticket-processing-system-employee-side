package org.tile.ticketing_system.domein;

import org.tile.ticketing_system.domein.interfaces.IContract;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "[Identity].[Contract]")
public class Contract implements Serializable, IContract {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contractId")
	private int contractId;

	private LocalDateTime startDatum;

	private LocalDateTime eindDatum;

	@OneToOne
	@JoinColumn(name = "TypeContractTypeId")
	private ContractType type;

	@Column(name = "Status")
	private ContractStatus status;

	@JoinColumn(name = "CompanyId")
	@ManyToOne
	private Company company;

	@Override
    public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Transient
	public boolean vervaltBinnenkort;

	public Contract() {
	}

	public Contract(ContractType type, ContractStatus status, LocalDateTime startDatum, LocalDateTime eindDatum) {
		setType(type);
		setStatus(status);
		setStartDatum(startDatum);
		setEindDatum(eindDatum);
	}

	@Override
    public LocalDateTime getStartDatum() {
		return startDatum;
	}

	public void setStartDatum(LocalDateTime startDatum) {
		this.startDatum = startDatum;
	}

	@Override
    public LocalDateTime getEindDatum() {
		return eindDatum;
	}

	public void setEindDatum(LocalDateTime eindDatum) {
		this.eindDatum = eindDatum;
	}

	@Override
    public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	@Override
    public ContractType getType() {
		return type;
	}

	public void setType(ContractType type) {
		this.type = type;
	}

	@Override
    public ContractStatus getStatus() {
		return status;
	}

	public void setStatus(ContractStatus status) {
		this.status = status;
	}

//	public Company getCompany() {
//		return company;
//	}
//
//	public void setCompany(Company company) {
//		this.company = company;
//	}

	@Override
    public boolean vervaltBinnenkort() {

		//10 second buffer voor systemtime
		return (this.eindDatum.minusDays(30).minusSeconds(10).isBefore(LocalDateTime.now())) && (!this.status.equals(ContractStatus.BEEINDIGD));
	}
//
//	public void setVervaltBinnenkort(boolean vervaltBinnenkort) {
//		this.vervaltBinnenkort = vervaltBinnenkort;
//	}

	@Override
	public String toString() {
		return "Contract [contractId=" + contractId + ", startDatum=" + startDatum + ", eindDatum=" + eindDatum

				+ ", type=" + type + ", status=" + status + ", company=" + company + ", vervaltBinnenkort="
				+ vervaltBinnenkort + "]";
	}


	public enum ContractStatus {
		IN_BEHANDELING, LOPEND, BEEINDIGD
	}
}
