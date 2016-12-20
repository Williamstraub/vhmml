package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.Contributor.Type;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.util.AttributeUtil;

/**
 * 
 * Transfer object used for sending up data on ReadingRoomObjectForm. While
 * there is a lot of duplication between these DTO objects and the corresponding
 * entity objects, this DTO layer is necessary to allow for the differences in
 * how the data is captured from the user vs. how it is stored in the database.
 * For example, the user enters a list of artists, scribes, etc. on the input
 * form. These are lists of people who contribute to ReadingRoom works, they are
 * stored in the database as contributors that have a type. This allows the same
 * person to be a scribe on one item and an artist on another and both items
 * reference the same "contributor" record. If we try to just use our entity
 * objects on the form, the entity objects quickly become polluted with complex
 * logic that is related to how the user creates data, e.g. we have lists of the
 * artists and such on the entity that need to be kept in sync with the master
 * entities contributors attribute and the "extra" lists like "artists" aren't
 * actually persisted. Having this layer allows us to keep the form input simple
 * and independent of the persistence layer.
 *
 */
public class ReadingRoomPartDTO {

	private Long id;
	private ReadingRoomObjectPart.Type type;
	private Integer partNumber;
	private String partLocation;
	private String supportImported;
	private String supportDimensionsImported;
	private Float supportDimensionsWidth;
	private Float supportDimensionsHeight;
	private String cardinalOfOrigin;
	private String regionOfOrigin;
	private String countryOfOrigin;
	private boolean countryOfOriginUncertain;
	private String cityOfOrigin;
	private boolean cityOfOriginUncertain;
	private boolean document;
	private boolean dated;
	private String centuryImported;
	private List<Integer> centuries;
	private boolean centuryUncertain;
	private String ymdDateImported;
	private Integer datePreciseYear;
	private Integer datePreciseMonth;
	private Integer datePreciseDay;		
	private String layout;
	private String scriptImported;
	private List<String> scripts;
	private String numberScribes;
	private String scribe;
	private String music;
	private List<String> supports;
	private String watermark;
	private String partNotes;
	private boolean revisit;
	private Integer beginDate;
	private Integer endDate;
	private Integer seq;
	private String acknowledgments;
	private String writingSystemImported;
	private List<String> writingSystems;
	private String nativeDatePrecise;
	private String reviser;
	private boolean catchwords;
	private String foliation;
	private String medium;
	private String decoration;
	private String colophonPart;
	private String writingSpaceImported;
	private Float writingSpaceWidth;
	private Float writingSpaceHeight;
	private boolean signatures;
	private List<String> fonts;	
	private String editionStatement;	
	private String printingStatement;	
	private String formatStatement;	
	private List<ReadingRoomPartContributor> scribes;
	private List<ReadingRoomPartContributor> artists;
	private List<ReadingRoomPartContributor> associatedNames;
	
	// have to manually track if the part has content because spring mvc binds a list with one empty object (object with all null attributes) if there is no content
	private boolean hasContent;
	private List<ReadingRoomContentDTO> contents;
	
	public ReadingRoomPartDTO() {
		super();
	}
	
	public ReadingRoomPartDTO(ReadingRoomObjectPart part) {
		BeanUtils.copyProperties(part, this);
		
		// we store these attributes denormalized for performance, but the user enters them as a list
		this.scripts = AttributeUtil.getStringAttributeAsList(part.getScript());
		this.supports = AttributeUtil.getStringAttributeAsList(part.getSupport());
		this.writingSystems = AttributeUtil.getStringAttributeAsList(part.getWritingSystem());
		this.fonts = AttributeUtil.getStringAttributeAsList(part.getFont());		
		
		// all contributors are part of the same list in the database but the user enters them by type
		this.scribes = part.getContributorsByType(Type.SCRIBE);
		this.artists = part.getContributorsByType(Type.ARTIST);
		
		if(CollectionUtils.isNotEmpty(part.getContents())) {
			setHasContent(true);
			List<ReadingRoomContentDTO> contentList = new ArrayList<ReadingRoomContentDTO>();
			
			for(Content content : part.getContents()) {
				contentList.add(new ReadingRoomContentDTO(content));
			}
			
			this.contents = contentList;			
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(Integer partNumber) {
		this.partNumber = partNumber;
	}

	public String getPartLocation() {
		return partLocation;
	}

	public void setPartLocation(String partLocation) {
		this.partLocation = partLocation;
	}

	public Float getSupportDimensionsWidth() {
		return supportDimensionsWidth;
	}

	public void setSupportDimensionsWidth(Float supportDimensionsWidth) {
		this.supportDimensionsWidth = supportDimensionsWidth;
	}

	public Float getSupportDimensionsHeight() {
		return supportDimensionsHeight;
	}

	public void setSupportDimensionsHeight(Float supportDimensionsHeight) {
		this.supportDimensionsHeight = supportDimensionsHeight;
	}

	public String getCardinalOfOrigin() {
		return cardinalOfOrigin;
	}

	public void setCardinalOfOrigin(String cardinalOfOrigin) {
		this.cardinalOfOrigin = cardinalOfOrigin;
	}

	public String getRegionOfOrigin() {
		return regionOfOrigin;
	}

	public void setRegionOfOrigin(String regionOfOrigin) {
		this.regionOfOrigin = regionOfOrigin;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public String getCityOfOrigin() {
		return cityOfOrigin;
	}

	public void setCityOfOrigin(String cityOfOrigin) {
		this.cityOfOrigin = cityOfOrigin;
	}

	public boolean isDocument() {
		return document;
	}

	public void setDocument(boolean document) {
		this.document = document;
	}

	public boolean isDated() {
		return dated;
	}

	public void setDated(boolean dated) {
		this.dated = dated;
	}

	public List<Integer> getCenturies() {
		return centuries;
	}

	public void setCenturies(List<Integer> centuries) {
		this.centuries = centuries;
	}

	public Integer getDatePreciseYear() {
		return datePreciseYear;
	}

	public void setDatePreciseYear(Integer datePreciseYear) {
		this.datePreciseYear = datePreciseYear;
	}

	public Integer getDatePreciseMonth() {
		return datePreciseMonth;
	}

	public void setDatePreciseMonth(Integer datePreciseMonth) {
		this.datePreciseMonth = datePreciseMonth;
	}

	public Integer getDatePreciseDay() {
		return datePreciseDay;
	}

	public void setDatePreciseDay(Integer datePreciseDay) {
		this.datePreciseDay = datePreciseDay;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<String> getScripts() {
		return scripts;
	}

	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}

	public String getNumberScribes() {
		return numberScribes;
	}

	public void setNumberScribes(String numberScribes) {
		this.numberScribes = numberScribes;
	}

	public String getScribe() {
		return scribe;
	}

	public void setScribe(String scribe) {
		this.scribe = scribe;
	}

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public List<String> getSupports() {
		return supports;
	}

	public void setSupports(List<String> supports) {
		this.supports = supports;
	}

	public String getWatermark() {
		return watermark;
	}

	public void setWatermark(String watermark) {
		this.watermark = watermark;
	}

	public String getPartNotes() {
		return partNotes;
	}

	public void setPartNotes(String partNotes) {
		this.partNotes = partNotes;
	}

	public boolean isRevisit() {
		return revisit;
	}

	public void setRevisit(boolean revisit) {
		this.revisit = revisit;
	}

	public Integer getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Integer beginDate) {
		this.beginDate = beginDate;
	}

	public Integer getEndDate() {
		return endDate;
	}

	public void setEndDate(Integer endDate) {
		this.endDate = endDate;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getAcknowledgments() {
		return acknowledgments;
	}

	public void setAcknowledgments(String acknowledgments) {
		this.acknowledgments = acknowledgments;
	}

	public List<String> getWritingSystems() {
		return writingSystems;
	}

	public void setWritingSystems(List<String> writingSystems) {
		this.writingSystems = writingSystems;
	}

	public String getNativeDatePrecise() {
		return nativeDatePrecise;
	}

	public void setNativeDatePrecise(String nativeDatePrecise) {
		this.nativeDatePrecise = nativeDatePrecise;
	}

	public String getReviser() {
		return reviser;
	}

	public void setReviser(String reviser) {
		this.reviser = reviser;
	}

	public boolean isCatchwords() {
		return catchwords;
	}

	public void setCatchwords(boolean catchwords) {
		this.catchwords = catchwords;
	}

	public String getFoliation() {
		return foliation;
	}

	public void setFoliation(String foliation) {
		this.foliation = foliation;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	public String getColophonPart() {
		return colophonPart;
	}

	public void setColophonPart(String colophonPart) {
		this.colophonPart = colophonPart;
	}

	public boolean isCountryOfOriginUncertain() {
		return countryOfOriginUncertain;
	}

	public void setCountryOfOriginUncertain(boolean countryOfOriginUncertain) {
		this.countryOfOriginUncertain = countryOfOriginUncertain;
	}
	
	public String getWritingSpaceImported() {
		return writingSpaceImported;
	}

	public void setWritingSpaceImported(String writingSpaceImported) {
		this.writingSpaceImported = writingSpaceImported;
	}

	public Float getWritingSpaceWidth() {
		return writingSpaceWidth;
	}

	public void setWritingSpaceWidth(Float writingSpaceWidth) {
		this.writingSpaceWidth = writingSpaceWidth;
	}

	public Float getWritingSpaceHeight() {
		return writingSpaceHeight;
	}

	public void setWritingSpaceHeight(Float writingSpaceHeight) {
		this.writingSpaceHeight = writingSpaceHeight;
	}

	public boolean isSignatures() {
		return signatures;
	}

	public void setSignatures(boolean signatures) {
		this.signatures = signatures;
	}

	public List<ReadingRoomPartContributor> getScribes() {
		return scribes;
	}

	public void setScribes(List<ReadingRoomPartContributor> scribes) {
		this.scribes = scribes;
	}

	public List<ReadingRoomPartContributor> getArtists() {
		return artists;
	}

	public void setArtists(List<ReadingRoomPartContributor> artists) {
		this.artists = artists;
	}

	public List<ReadingRoomContentDTO> getContents() {
		return contents;
	}

	public void setContents(List<ReadingRoomContentDTO> contents) {
		this.contents = contents;
	}

	public String getSupportImported() {
		return supportImported;
	}

	public void setSupportImported(String supportImported) {
		this.supportImported = supportImported;
	}

	public String getSupportDimensionsImported() {
		return supportDimensionsImported;
	}

	public void setSupportDimensionsImported(String supportDimensionsImported) {
		this.supportDimensionsImported = supportDimensionsImported;
	}

	public String getCenturyImported() {
		return centuryImported;
	}

	public void setCenturyImported(String centuryImported) {
		this.centuryImported = centuryImported;
	}

	public String getWritingSystemImported() {
		return writingSystemImported;
	}

	public void setWritingSystemImported(String writingSystemImported) {
		this.writingSystemImported = writingSystemImported;
	}

	public String getYmdDateImported() {
		return ymdDateImported;
	}

	public void setYmdDateImported(String ymdDateImported) {
		this.ymdDateImported = ymdDateImported;
	}
	
	public ReadingRoomObjectPart.Type getType() {
		return type;
	}

	public void setType(ReadingRoomObjectPart.Type type) {
		this.type = type;
	}

	public List<String> getFonts() {
		return fonts;
	}

	public void setFonts(List<String> fonts) {
		this.fonts = fonts;
	}

	public boolean isHasContent() {
		return hasContent;
	}

	public void setHasContent(boolean hasContent) {
		this.hasContent = hasContent;
	}

	public boolean isCityOfOriginUncertain() {
		return cityOfOriginUncertain;
	}

	public void setCityOfOriginUncertain(boolean cityOfOriginUncertain) {
		this.cityOfOriginUncertain = cityOfOriginUncertain;
	}

	public boolean isCenturyUncertain() {
		return centuryUncertain;
	}

	public void setCenturyUncertain(boolean centuryUncertain) {
		this.centuryUncertain = centuryUncertain;
	}

	public String getScriptImported() {
		return scriptImported;
	}

	public void setScriptImported(String scriptImported) {
		this.scriptImported = scriptImported;
	}

	public List<ReadingRoomPartContributor> getAssociatedNames() {
		return associatedNames;
	}

	public void setAssociatedNames(List<ReadingRoomPartContributor> associatedNames) {
		this.associatedNames = associatedNames;
	}

	public String getEditionStatement() {
		return editionStatement;
	}

	public void setEditionStatement(String editionStatement) {
		this.editionStatement = editionStatement;
	}

	public String getPrintingStatement() {
		return printingStatement;
	}

	public void setPrintingStatement(String printingStatement) {
		this.printingStatement = printingStatement;
	}

	public String getFormatStatement() {
		return formatStatement;
	}

	public void setFormatStatement(String formatStatement) {
		this.formatStatement = formatStatement;
	}
}
