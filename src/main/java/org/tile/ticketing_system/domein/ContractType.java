package org.tile.ticketing_system.domein;

import org.tile.ticketing_system.domein.interfaces.IContractType;

import javax.persistence.*;

@Entity
@Table(name = "[Identity].[ContractType]")
public class ContractType implements IContractType {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int contractTypeId;
    @Column(name = "Naam")
    private String name;
    private ManierAanmakenTicket manierAanmakenTicket;
    private GedekteTijdstippen gedekteTijdstippen;
    private double maximaleAfhandeltijd;
    private double minimaleDoorlooptijd;
    private double prijs;
    private int aantalContracten;
    private ContractTypeStatus status;


    //TODO is eigenlijk niet correct, je kan geen leeg ContractType aanmaken in de realiteit
    public ContractType() {
    }

    public ContractType(String naam, ManierAanmakenTicket manierAanmakenTicket,
                        GedekteTijdstippen gedekteTijdstippen,
                        double maximaleAfhandeltijd, double minimaleDoorlooptijd,
                        double prijs) {
        setName(naam);
        setManierAanmakenTicket(manierAanmakenTicket);
        setGedekteTijdstippen(gedekteTijdstippen);
        setMaximaleAfhandeltijd(maximaleAfhandeltijd);
        setMinimaleDoorlooptijd(minimaleDoorlooptijd);
        setPrijs(prijs);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.trim().isEmpty()) throw new IllegalArgumentException("Naam mag niet leeg zijn");
        this.name = name;
    }

    @Override
    public int getContractTypeId() {
        return contractTypeId;
    }

    public void setContractTypeId(int contractTypeId) {
        this.contractTypeId = contractTypeId;
    }

    @Override
    public ManierAanmakenTicket getManierAanmakenTicket() {
        return manierAanmakenTicket;
    }

    public void setManierAanmakenTicket(ManierAanmakenTicket manierAanmakenTicket) {
        this.manierAanmakenTicket = manierAanmakenTicket;
    }

    @Override
    public GedekteTijdstippen getGedekteTijdstippen() {
        return gedekteTijdstippen;
    }

    public void setGedekteTijdstippen(GedekteTijdstippen gedekteTijdstippen) {
        this.gedekteTijdstippen = gedekteTijdstippen;
    }

    @Override
    public double getMaximaleAfhandeltijd() {
        return maximaleAfhandeltijd;
    }

    public void setMaximaleAfhandeltijd(double maximaleAfhandeltijd) {
    	if (maximaleAfhandeltijd < 0)
    		throw new IllegalArgumentException("Maximale afhandeltijd mag niet negatief of leeg zijn");
        this.maximaleAfhandeltijd = maximaleAfhandeltijd;
    }

    @Override
    public double getMinimaleDoorlooptijd() {
        return minimaleDoorlooptijd;
    }

    public void setMinimaleDoorlooptijd(double minimaleDoorlooptijd) {
    	if (minimaleDoorlooptijd < 0) 
    		throw new IllegalArgumentException("Minimale doorlooptijd mag niet negatief of leeg zijn");
        this.minimaleDoorlooptijd = minimaleDoorlooptijd;
    }

    @Override
    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
    	if (prijs < 0) 
    		throw new IllegalArgumentException("Prijs mag niet negatief of leeg zijn");
        this.prijs = prijs;
    }

    @Override
    public int getAantalContracten() {
        return aantalContracten;
    }

    public void setAantalContracten(int aantalContracten) {
    	if (aantalContracten < 0)
    		throw new IllegalArgumentException("Aantal contracten kan niet kleiner zijn dan 0");
        this.aantalContracten = aantalContracten;
    }

    @Override
    public ContractTypeStatus getStatus() {
        return status;
    }

    public void setStatus(ContractTypeStatus status) {
        this.status = status;
    }

    public ContractType editContractType(String name, double maxAfhaandeldTijd, double minDoorloooptijd, double prijs,
     ManierAanmakenTicket manierAanmakenTicket, GedekteTijdstippen gedekteTijdstippen, ContractTypeStatus contractTypeStatu) {
        setName(name);
        setMaximaleAfhandeltijd(maxAfhaandeldTijd);
        setMinimaleDoorlooptijd(minDoorloooptijd);
        setPrijs(prijs);
        setManierAanmakenTicket(manierAanmakenTicket);
        setGedekteTijdstippen(gedekteTijdstippen);
        setStatus(contractTypeStatu);
        return this;
    }

    public enum ContractTypeStatus {
        ACTIEF, NIET_ACTIEF
    }

    public enum ManierAanmakenTicket {
        EMAIL, TELEFOON, APPLICATIE
    }

    public enum GedekteTijdstippen {
        ALTIJD, ENKELWEEKDAGEN
    }

    @Override
    public String toString() {
        return "ContractType [name=" + name + ", contractTypeId=" + contractTypeId + ", manierAanmakenTicket="
                + manierAanmakenTicket + ", gedekteTijdstippen=" + gedekteTijdstippen + ", maximaleAfhandeltijd="
                + maximaleAfhandeltijd + ", minimaleDoorlooptijd=" + minimaleDoorlooptijd + ", prijs=" + prijs
                + ", aantalContracten=" + aantalContracten + ", status=" + status + "]";
    }


}
