package org.vhmml.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contributors")
public class Contributor extends NamedIdentifiable implements Serializable {
	
	private static final long serialVersionUID = 1125117257089393528L;
	
	public Contributor() {
		super();
	}
	
	public Contributor(String name) {
		setName(name);
	}
}
