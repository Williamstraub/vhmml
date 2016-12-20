package org.vhmml.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

	private static final long serialVersionUID = -1749355252541494970L;
	
	public enum Name {
		ROLE_ADMIN("System Admin"),		
		ROLE_REFERENCE_CREATOR("Reference Creator"),
		ROLE_LEXICON_CREATOR("Lexicon Creator"),
		ROLE_FOLIO_CREATOR("Folio Creator"),
		ROLE_SCRIPTORIUM_USER("Scriptorium User"),
		ROLE_LEVEL_I_CATALOGER("Level 1 Cataloger"),
		ROLE_LEVEL_II_CATALOGER("Level 2 Cataloger"),
		ROLE_LEVEL_III_CATALOGER("Level 3 Cataloger"),
		ROLE_SCHOLAR("Scholar");
		
		private String displayName;
		
		Name(String displayName) {
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public String getName() {
			return this.name();
		}
	}

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	private String description;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
