package org.vhmml.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "reference_tags")
public class ReferenceTag {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "reference_entry_id")
    private ReferenceEntry entry;
	
	@Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tag_id")
    private Tag tag;
	
	public ReferenceTag() {
		super();
	}
	
	public ReferenceTag(ReferenceEntry entry, org.vhmml.dto.zotero.ZoteroReferenceTag tag) {
		BeanUtils.copyProperties(tag, this);		
		this.tag = new Tag(tag);
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

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
