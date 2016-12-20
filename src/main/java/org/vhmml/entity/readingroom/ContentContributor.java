package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_content_contributors")
public class ContentContributor {

	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "contributor_type")	
	private Contributor.Type type;
	
	// name native script is stored specific to the content because the same contributor might have Arabic native 
	// script name on one content item and some other language on another content item
	@Column(name = "name_ns")
	private String nameNs;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "reading_room_content_id")
    private Content content;

	@OrderBy("name")
	@Fetch(FetchMode.JOIN)
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reading_room_contributor_id")
    private Contributor contributor;	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ContentContributor() {
		super();
	}
	
	public ContentContributor(Content content, Contributor contributor, Contributor.Type type, String nameNs) {
		this.content = content;
		this.contributor = contributor;
		this.type = type;
		this.nameNs = nameNs;
	}

	public Contributor.Type getType() {
		return type;
	}

	public void setType(Contributor.Type type) {
		this.type = type;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public Contributor getContributor() {
		return contributor;
	}

	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	public String getNameNs() {
		return nameNs;
	}

	public void setNameNs(String nameNs) {
		this.nameNs = nameNs;
	}
}