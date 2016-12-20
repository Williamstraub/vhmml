package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_countries")
public class Country extends NamedIdentifiable implements AuthorityListOption {	
	
	@Column(name = "authority_uri_lc")
	private String authorityUriLC;
	
	@Column(name = "authority_uri_viaf")
	private String authorityUriVIAF;	
	
	public Country() {
		super();
	}
	
	public Country(Long id) {
		this.id = id;
	}
	
	public Country(String name) {
		setName(name);
	}
	
	public Country(String name, String authorityUriLC, String authorityUriVIAF) {
		this(name);
		this.authorityUriLC = authorityUriLC;
		this.authorityUriVIAF = authorityUriVIAF;
	}
	
	public Country(Long id, String name, String authorityUriLC, String authorityUriVIAF) {
		this(name, authorityUriLC, authorityUriVIAF);
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
}
