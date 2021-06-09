package org.tile.ticketing_system.domein;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.tile.ticketing_system.domein.interfaces.ICompany;

@Entity
@Table(name = "[Identity].[Companies]")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company implements Serializable, ICompany {

    @Id
    private String companyId;
    private String name;
    private String adress;
    private String phoneNumber;

    public Company(String name, String adress, String phoneNumber) {
        this.companyId = UUID.randomUUID().toString();
        this.name = name;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
    }
}
