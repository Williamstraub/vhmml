package org.vhmml.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "reference_creators")
public class ReferenceCreator {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "reference_entry_id")
    private ReferenceEntry entry;

	@OrderBy("lastName")
	@Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creator_id")
    private Creator creator;
	
	@Column(name = "creator_type")
	private String creatorType;		
	 
	public ReferenceCreator() {
		super();
	}
	
	public ReferenceCreator(ReferenceEntry entry, org.vhmml.dto.zotero.ZoteroReferenceCreator creator) {
		BeanUtils.copyProperties(creator, this);		
		this.creator = new Creator(creator);
		this.entry = entry;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReferenceEntry getEntry() {
		return entry;
	}

	public void setEntry(ReferenceEntry entry) {
		this.entry = entry;
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}

	public Creator getCreator() {
		return creator;
	}

	public void setCreator(Creator creator) {
		this.creator = creator;
	}
}
