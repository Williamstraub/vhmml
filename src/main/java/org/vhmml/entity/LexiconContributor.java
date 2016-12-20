package org.vhmml.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lexicon_contributors")
public class LexiconContributor implements Serializable {
	
	private static final long serialVersionUID = 1125117257089393528L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "lexicon_id")
	private Long lexiconId;
	
	@Column(name = "contributor_id")
	private Long contributorId;	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLexiconId() {
		return lexiconId;
	}

	public void setLexiconId(Long lexiconId) {
		this.lexiconId = lexiconId;
	}

	public Long getContributorId() {
		return contributorId;
	}

	public void setContributorId(Long contributorId) {
		this.contributorId = contributorId;
	}
}
