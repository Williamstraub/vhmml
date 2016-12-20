package org.vhmml.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public class AuthorityControl extends NamedIdentifiable {

	public AuthorityControl() {
		super();
	}
	
	public AuthorityControl(String name, String authorityUriLC, String authorityUriVIAF) {
		setName(name);
		this.authorityUriLC = authorityUriLC;
		this.authorityUriVIAF = authorityUriVIAF;
	}
	
	@Column(name = "authority_uri_lc")
	private String authorityUriLC;
	
	@Column(name = "authority_uri_viaf")
	private String authorityUriVIAF;

	public String getAuthorityUriLC() {
		return authorityUriLC;
	}

	public void setAuthorityUriLC(String authorityUriLC) {
		// can't clear out an authority URI once it's been set
		if(!StringUtils.isEmpty(authorityUriLC) || StringUtils.isEmpty(this.authorityUriLC)) {
			this.authorityUriLC = authorityUriLC;
		}		
	}

	public String getAuthorityUriVIAF() {
		return authorityUriVIAF;
	}

	public void setAuthorityUriVIAF(String authorityUriVIAF) {
		if(!StringUtils.isEmpty(authorityUriVIAF) || StringUtils.isEmpty(this.authorityUriVIAF)) {
			this.authorityUriVIAF = authorityUriVIAF;
		}		
	}
}
