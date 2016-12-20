package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_languages")
public class Language extends NamedIdentifiable {
	
	public Language() {
		super();
	}
	
	public Language(String name) {
		setName(name);
	}
	
	public Language(Long id) {
		this.id = id;
	}
	
	public Language(Long id, String name) {
		this(name);
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Language other = (Language) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
