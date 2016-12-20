package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_repositories")
public class Repository extends NamedIdentifiable implements AuthorityListOption {	
	
	@Column(name = "authority_uri_lc")
	private String authorityUriLC;
	
	@Column(name = "authority_uri_viaf")
	private String authorityUriVIAF;
	
	@Column(name = "city_id")
	private Long cityId;
	
	public Repository() {
		super();
	}
	
	public Repository(String name) {
		setName(name);
	} 
	
	public Repository(String name, Long cityId) {
		this(name);
		this.cityId = cityId;
	}
	
	public Repository(String name, String authorityUriLC, String authorityUriVIAF, Long cityId) {
		this(name, cityId);
		this.authorityUriLC = authorityUriLC;
		this.authorityUriVIAF = authorityUriVIAF;
	}
	
	public Repository(Long id) {
		this.id = id;
	}
	
	public Repository(Long id, String name, String authorityUriLC, String authorityUriVIAF, Long cityId) {
		this(name, authorityUriLC, authorityUriVIAF, cityId);
		this.id = id;		
	}

	public String getAuthorityUriLC() {
		return authorityUriLC;
	}

	public void setAuthorityUriLC(String authorityUriLC) {
		this.authorityUriLC = authorityUriLC;
	}

	public String getAuthorityUriVIAF() {
		return authorityUriVIAF;
	}

	public void setAuthorityUriVIAF(String authorityUriVIAF) {
		this.authorityUriVIAF = authorityUriVIAF;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
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
		Repository other = (Repository) obj;
		if (cityId == null) {
			if (other.cityId != null)
				return false;
		} else if (!cityId.equals(other.cityId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
