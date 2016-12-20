package org.vhmml.entity.readingroom;

public enum Font {

	ARABIC("Arabic"),
	ARMENIAN("Armenian"),
	COPTIC("Coptic"),
	CYRILLIC("Cyrillic"),
	ETHIOPIC("Ethiopic"),
	GEORGIAN("Georgian"),
	GOTHIC("Gothic"),
	GREEK("Greek"),
	HEBREW("Hebrew"),
	ITALIC("Italic"),
	MALAYALAM("Malayalam"),
	ROMAN("Roman"),
	SYRIAC("Syriac");
	
	private String name;
	
	private Font(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Font forName(String name) {
		Font font = null;
    	
    	for(Font nextFont: Font.values()) {
    		if(nextFont.name.equals(name)) {
    			font = nextFont;
    			break;
    		}
    	}
    	
    	if (font == null) {
    		throw new IllegalStateException("No font for name " + name + " found on " + Font.class.getName());
    	}
    	
    	return font;
	}
}
