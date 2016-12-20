package org.vhmml.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "creators")
public class Creator {

	public enum Type {
		AUTHOR("author"),
		BOOKAUTHOR("bookAuthor"),
		CONTRIBUTOR("contributor"),
		DIRECTOR("director"),
		EDITOR("editor"),		
		PODCASTER("podcaster"),
		TRANSLATOR("translator");
		
		private String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static Type forName(String name) {
        	Type type = null;
        	
        	for(Type nextType : Type.values()) {
        		if(nextType.name.equals(name)) {
        			type = nextType;
        			break;
        		}
        	}
        	if (type == null) {
        		throw new IllegalStateException("No creator type for name " + name + " found on " + Creator.Type.class.getName());
        	}
        	
        	return type;
        }    
	}
	
	@Id
	@GeneratedValue
	private Long id;	
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@OneToMany(mappedBy = "creator")
    private List<ReferenceCreator> referenceCreators = new ArrayList<ReferenceCreator>();
	
	public Creator() {
		super();
	}
	
	public Creator(org.vhmml.dto.zotero.ZoteroReferenceCreator creator) {
		BeanUtils.copyProperties(creator, this);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		String creatorName = name;
		
		if(StringUtils.isEmpty(creatorName)) {
			boolean hasLastName = StringUtils.isNotEmpty(lastName);
			boolean hasFirstName = StringUtils.isNotEmpty(firstName);
			
			if(hasLastName && hasFirstName) {
				creatorName = lastName + ", " + firstName;
			} else if(hasLastName) {
				creatorName = lastName;
			} else if(hasFirstName) {
				creatorName = firstName;
			}
		}
		
		return creatorName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
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
		Creator other = (Creator) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
