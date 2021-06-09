package org.tile.ticketing_system.domein;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import org.tile.ticketing_system.domein.interfaces.IBijlage;

@Entity
@Table(name = "[Identity].[Bijlages]")
@Getter
//@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Bijlage implements Serializable, IBijlage {

	@Id
	@Column(name = "BijlageId")
	private String bijlageId;
	private String fileType;
	private String name;
	private byte[] dataFiles;

	public Bijlage(){
		this.bijlageId = UUID.randomUUID().toString();
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDataFiles(byte[] dataFiles) {
		this.dataFiles = dataFiles;
	}

	@Override
	public String toString() {
		String dataFiles = "datafiles are " + (this.dataFiles.length == 0 ? "not present" : "present");
		return "Bijlage [bijlageId=" + bijlageId + ", fileType=" + fileType + ", name=" + name + ", " + dataFiles + "]";
	}
}
