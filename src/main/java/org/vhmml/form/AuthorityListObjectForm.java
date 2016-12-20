package org.vhmml.form;

import org.vhmml.service.AuthorityListService;

public class AuthorityListObjectForm {

	private Long id;
	private AuthorityListService.AuthorityListType authListType;
	private String name;
	private String displayName;
	private String uri;
	private String authorityUriLC;
	private String authorityUriVIAF;

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
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

	public AuthorityListService.AuthorityListType getAuthListType() {
		return authListType;
	}

	public void setAuthListType(AuthorityListService.AuthorityListType authListType) {
		this.authListType = authListType;
	}
}
