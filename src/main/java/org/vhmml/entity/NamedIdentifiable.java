package org.vhmml.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public class NamedIdentifiable implements Identifiable {

	@Id
	@GeneratedValue
	protected Long id;
	
	protected String name;

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
		
		if(StringUtils.isNotEmpty(name)) {
			name = name.trim();
		}
		
		this.name = name;
	}
	
	public static List<Long> getIds(List<? extends Identifiable> identifiables) {
		List<Long> ids = new ArrayList<Long>();
		
		for(Identifiable identifiable : identifiables) {
			ids.add(identifiable.getId());
		}
		
		return ids;
	}
	
	public static List<String> getNames(List<? extends NamedIdentifiable> identifiables) {
		List<String> names = new ArrayList<String>();
		
		for(NamedIdentifiable identifiable : identifiables) {
			names.add(identifiable.getName());
		}
		
		return names;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		NamedIdentifiable other = (NamedIdentifiable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
