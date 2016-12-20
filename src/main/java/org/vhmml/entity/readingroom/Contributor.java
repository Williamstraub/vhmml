package org.vhmml.entity.readingroom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.vhmml.entity.AuthorityControl;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "ReadingRoomContributor")
@Table(name = "reading_room_contributors")
public class Contributor extends AuthorityControl {		
	
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Type {
		ARTIST("Artist", false),
		ATTRIBUTED_NAME("Attributed name", true),
		AUTHOR("Author", false),
		BOOKSELLER("Bookseller", true),
		CARTOGRAPHER("Cartographer", true),
		COMMENTATOR("Commentator", true),
		COMMISSIONER("Commissioner", true),
		COMPILER("Compiler", true),
		CONTRIBUTOR("Contributor", true),
		DEDICATEE("Dedicatee", true),
		EDITOR("Editor", true),
		ENGRAVER("Engraver", true),
		FORMER_OWNER("Former Owner", false),
		ILLUMINATOR("Illuminator", true),
		ILLUSTRATOR("Illustrator", true),
		LICENSOR("Licensor", true),
		JOINT_AUTHOR("Joint Author", false),
		NOTARY("Notary", true),
		OTHER("Other", false),
		PATRON("Patron", true),
		PRINTER("Printer", true),
		PUBLISHER("Publisher", true),
		RECIPIENT("Recipient", true),
		SCRIBE("Scribe", false),
		TRANSLATOR("Translator", true);

		private String displayName;
		// does this contributor type belong on the "associated names" list
		private boolean associatedName;
		
		Type(String displayName, boolean associatedName) {
			this.displayName = displayName;
			this.associatedName = associatedName;
		}
		
		public String getDisplayName() {
			return displayName;
		}

		public boolean isAssociatedName() {
			return associatedName;
		}
		
		public static List<Type> getAssociatedNameTypes() {
			List<Type> associatedNameTypes = new ArrayList<>();
			
			for(Type type : values()) {
				if(type.isAssociatedName()) {
					associatedNameTypes.add(type);
				}
			}
			
			return associatedNameTypes;
		}
		
		public static Type forDisplayName(String name) {
			Type type = null;
	    	
	    	for(Type nextType: Type.values()) {
	    		if(nextType.displayName.equals(name)) {
	    			type = nextType;
	    			break;
	    		}
	    	}
	    	
	    	if (type == null) {
	    		throw new IllegalArgumentException("No contributor type for name " + name + " found on " + Type.class.getName());
	    	}
	    	
	    	return type;
		}
	}
	
	@Column(name = "display_name")
	private String displayName;

	public Contributor() {
		super();
	}
	
	public Contributor(String name) {
		this.name = name;
	}
	
	public Contributor(String name, String authorityUriLC, String authorityUriVIAF) {
		super(name, authorityUriLC, authorityUriVIAF);
	}
	
	public Contributor(String name, String authorityUriLC, String authorityUriVIAF, String displayName) {
		super(name, authorityUriLC, authorityUriVIAF);
		this.displayName = displayName;
	}
	
	public Contributor(Long id, String name, String authorityUriLC, String authorityUriVIAF, String displayName) {
		this(name, authorityUriLC, authorityUriVIAF, displayName);
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		// can't clear out displayName once it's been set because some types of contributor such as former 
		// owner don't even provide this field, so if the same person was listed as an author elsewhere
		// and a displayName value was provided, entering them as a former owner would cause this to get blanked
		// since the user doesn't even have a chance to enter a value for displayName
		if(!StringUtils.isEmpty(displayName) || StringUtils.isEmpty(this.displayName)) {
			this.displayName = displayName;
		}		
	}
	
	public String getPopulatedName() {		
		if(StringUtils.isEmpty(displayName)) {
			return name;
		} else {
			return displayName;
		}
	}
}