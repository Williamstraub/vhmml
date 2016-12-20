package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_cities")
public class City extends NamedIdentifiable implements AuthorityListOption {
	
	@Column(name = "authority_uri_lc")
	private String authorityUriLC;
	
	@Column(name = "authority_uri_viaf")
	private String authorityUriVIAF;
	
	@Column(name = "country_id")
	private Long countryId;
	
	public City() {
		super();
	}
	
	public City(Long id) {
		this.id = id;
	}
	
	public City(String name) {
		setName(name);
	}
	
	public City(String name, Long countryId) {
		this(name);
		this.countryId = countryId;
	}	
	
	public City(String name, String authorityUrlLC, String authorityUrlVIAF, Long countryId) {
		this(name, countryId);
		this.authorityUriLC = authorityUrlLC;
		this.authorityUriVIAF = authorityUrlVIAF;
	}
	
	public City(Long id, String name, String authorityUrlLC, String authorityUrlVIAF, Long countryId) {
		this(name, authorityUrlLC, authorityUrlVIAF, countryId);
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

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
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
		City other = (City) obj;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}