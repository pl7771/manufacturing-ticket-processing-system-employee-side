package org.tile.ticketing_system.domein;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tile.ticketing_system.domein.interfaces.IRole;

@Entity
@Table(name = "[Identity].[Role]")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role implements IRole {

	@Id
	private String id;
	private String name;

	public Role(RoleType roleType) {
		this.id = UUID.randomUUID().toString();
		this.name = roleType.getLabel();
	}

	public enum RoleType {
		CLIENT("Client"), ADMINISTRATOR("Administrator"), TECHNICIAN("Technician"), SUPPORTMANAGER("SupportManager");

		public final String label;

		RoleType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}


	private void setId(String id) {
		this.id = id;
	}

	@PrePersist
	private void ensureId() {
		this.setId(UUID.randomUUID().toString());
	}

	@Override
    public String getName() {



		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}

}
