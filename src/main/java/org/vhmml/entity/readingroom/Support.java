package org.vhmml.entity.readingroom;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.vhmml.util.AttributeUtil;

public enum Support {
	PAPER("Paper"),
	PARCHMENT("Parchment"),
	CLAY("Clay"),
	FABRIC("Fabric"),
	METAL("Metal"),
	PALM_LEAF("Palm leaf"),
	PAPYRUS("Papyrus"),	
	STONE("Stone"),
	WAX("Wax"),
	WOOD("Wood"),
	OTHER("Other");
	
	String name;
	
	Support(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	
	
	public static Support forName(String name) {
		Support support = null;
    	
    	for(Support nextSupport : Support.values()) {
    		if(nextSupport.name.equalsIgnoreCase(name)) {
    			support = nextSupport;
    			break;
    		}
    	}
    	
    	if (support == null) {
    		throw new IllegalArgumentException("No support for name " + name + " found on " + Support.class.getName());
    	}
    	
    	return support;
	}
	
	/**
	 * This method will return a comma separated list of Support display names based on
	 * a comma separated list of Support enum values (as is stored in the database).
	 * 
	 * @param supportListString
	 * @return
	 */
	public static String getSupportDisplay(String supportListString, String defaultValue) {
		StringBuilder supportDisplay = new StringBuilder();
		
		if(StringUtils.isNotEmpty(supportListString)) {
			List<String> supports = AttributeUtil.getStringAttributeAsList(supportListString);
			
			for(int i = 0; i < supports.size(); i++) {
				if(i > 0) {
					supportDisplay.append(", ");
				}
				
				supportDisplay.append(Support.valueOf(supports.get(i)).getName());
			}
		} else if(StringUtils.isNotEmpty(defaultValue)) {
			supportDisplay.append(defaultValue);
		}
		
		return supportDisplay.toString();
	}
}
