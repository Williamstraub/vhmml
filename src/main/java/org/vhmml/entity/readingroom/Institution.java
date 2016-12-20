package org.vhmml.entity.readingroom;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_institutions")
public class Institution extends NamedIdentifiable {	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="holding_institution_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<Repository> repositories;
	
	public Institution() {
		super();
	}
	
	public Institution(Long id) {
		this.id = id;
	}
	
	public Institution(String name) {
		setName(name);
	}
	
	public Institution(Long id, String name) {
		this(name);
		this.id = id;
	}

	public List<Repository> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<Repository> repositories) {
		this.repositories = repositories;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Institution other = (Institution) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
