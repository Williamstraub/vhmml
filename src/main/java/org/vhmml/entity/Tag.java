package org.vhmml.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;
import org.vhmml.util.TagSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TagSerializer.class)
@Entity
@Table(name = "tags")
public class Tag {

	@Id
	@GeneratedValue
	private Long id;
	
	private String tag;
	
	public Tag() {
		super();
	}
	
	public Tag(String tag) {
		this.tag = tag;
	}
	
	public Tag(org.vhmml.dto.zotero.ZoteroReferenceTag tag) {
		BeanUtils.copyProperties(tag, this);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		Tag other = (Tag) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
}
