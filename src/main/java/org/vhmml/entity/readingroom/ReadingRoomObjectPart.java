package org.vhmml.entity.readingroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.Identifiable;
import org.vhmml.util.AttributeUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

@Entity
@Table(name = "reading_room_object_parts")
public class ReadingRoomObjectPart implements Identifiable {
	
	public enum Type {
		MANUSCRIPT("Manuscript"),
		PRINTED("Printed"),
		UNKNOWN("Unknown");
		
		private String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "reading_room_object_id")
    private ReadingRoomObject parentObject;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "part_type")
	private Type type;
	
	@Column(name = "part_number")
	private Integer partNumber;
	
	@Column(name = "part_location")
	private String partLocation;	
	
	@Column(name = "support_dimensions_imported")
	private String supportDimensionsImported;
	
	@Column(name = "support_imported")
	private String supportImported;
	
	@Column(name = "support_width")
	private Float supportDimensionsWidth;
	
	@Column(name = "support_height")
	private Float supportDimensionsHeight;	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentPart")
	@Fetch(FetchMode.SUBSELECT)	
	private List<ReadingRoomPartContributor> contributors = new ArrayList<ReadingRoomPartContributor>();
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reading_room_part_centuries", joinColumns = {@JoinColumn(name="part_id")})
	@Column(name = "century")
	private List<Integer> centuries;
	
	@Column(name = "century_uncertain")
	private boolean centuryUncertain;

	@Column(name = "cardinal_of_origin")
	private String cardinalOfOrigin;
	
	@Column(name = "region_of_origin")
	private String regionOfOrigin;
	
	@Column(name = "city_of_origin")
	private String cityOfOrigin;
	
	@Column(name = "city_of_origin_uncertain")
	private boolean cityOfOriginUncertain;	
	
	@Column(name = "century_imported")
	private String centuryImported;	
	
	@Column(name = "ymd_date_imported")
	private String ymdDateImported;
	
	@Column(name = "date_precise_year")
	private Integer datePreciseYear;
	
	@Column(name = "date_precise_month")
	private Integer datePreciseMonth;
	
	@Column(name = "date_precise_day")
	private Integer datePreciseDay;
	
	private String layout;
	
	// scripts are stored in the reading_room_scripts table instead of an enum because we need the ability to add them 
	// dynamically through the app, but we store them on the parts denormalized for performance since they are just a name
	private String script;
	
	@Column(name = "script_imported")
	private String scriptImported;
	
	@Column(name = "number_scribes")
	private String numberScribes;
	
	// we don't consume the scribe field, scribes are stored as part_contributors 
	// this is just here because we import the original Oliver scribe value here, which
	// is a semi-colon delimited string that get's broken up into tokens, this way we have
	// the original string in case the data was odd and didn't import well
	private String scribe;
	private String support;	
	
	private String artist;
	
	@Column(name = "part_notes")
	private String partNotes;	
	
	@Column(name = "begin_date")
	private Integer beginDate;
	
	@Column(name = "end_date")
	private Integer endDate;
	
	private Integer seq;
	private String acknowledgments;
	
	@Column(name = "writing_system_imported")
	private String writingSystemImported;
	
	// writing systems are stored in the reading_room_writing_systems table instead of an enum because we need the 
	// ability to add them dynamically through the app, but we store them on the parts denormalized for performance since they are just a name
	@Column(name = "writing_system")
	private String writingSystem;
	
	@Column(name = "native_date_precise")
	private String nativeDatePrecise;
	private boolean catchwords;	
	private String decoration;
	
	@Column(name = "colophon_part")
	private String colophonPart;
	
	@Column(name = "artist_uri")
	private String artistUri;	
		
	@Column(name="country_of_origin")
	private String countryOfOrigin;
	
	@Column(name = "country_of_origin_uncertain")
	private boolean countryOfOriginUncertain;
	
	@Column(name = "writing_space_imported")
	private String writingSpaceImported;
	
	@Column(name = "writing_space_width")
	private Float writingSpaceWidth;
	
	@Column(name = "writing_space_height")
	private Float writingSpaceHeight;
	
	private boolean signatures;
	
	private String font;
	
	@Column(name = "edition_statement")
	private String editionStatement;
	
	@Column(name = "printing_statement")
	private String printingStatement;
	
	@Column(name = "format_statement")
	private String formatStatement;
	
	@Column(name = "foliation_imported")
	private String foliationImported;
	
	@Column
	private String medium;

	@OrderBy("itemNumber")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentPart", orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	private List<Content> contents = new ArrayList<Content>();
	
	public ReadingRoomObjectPart() {
		super();
	}
	
	public ReadingRoomObjectPart(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReadingRoomObject getParentObject() {
		return parentObject;
	}

	public void setParentObject(ReadingRoomObject parentObject) {
		this.parentObject = parentObject;
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

	public String getSupportDimensionsImported() {
		return supportDimensionsImported;
	}

	public void setSupportDimensionsImported(String supportDimensionsImported) {
		this.supportDimensionsImported = supportDimensionsImported;
	}

	public String getSupportImported() {
		return supportImported;
	}

	public void setSupportImported(String supportImported) {
		this.supportImported = supportImported;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
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

	public String getCityOfOrigin() {
		return cityOfOrigin;
	}

	public void setCityOfOrigin(String cityOfOrigin) {
		this.cityOfOrigin = cityOfOrigin;
	}
	
	public String getCenturyImported() {
		return centuryImported;
	}

	public void setCenturyImported(String centuryImported) {
		this.centuryImported = centuryImported;
	}

	public String getYmdDateImported() {
		return ymdDateImported;
	}

	public void setYmdDateImported(String ymdDateImported) {
		this.ymdDateImported = ymdDateImported;
	}

	public Date getDatePrecise() {		
		return AttributeUtil.getDatePrecise(this.datePreciseYear, this.datePreciseMonth, this.datePreciseDay);		
	}
	
	public String getDatePreciseDisplay() {
		return AttributeUtil.getDatePreciseDisplay(this.datePreciseYear, this.datePreciseMonth, this.datePreciseDay);		
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getScript() {
		
		return StringUtils.isEmpty(script) ? scriptImported : script;
	}

	public void setScript(String script) {
		this.script = script;
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

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}
	
	public String getSupportDisplay() {
		return Support.getSupportDisplay(support, supportImported);		
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getPartNotes() {
		return partNotes;
	}

	public void setPartNotes(String partNotes) {
		this.partNotes = partNotes;
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
	
	public String getWritingSystemImported() {
		return writingSystemImported;
	}

	public void setWritingSystemImported(String writingSystemImported) {
		this.writingSystemImported = writingSystemImported;
	}

	public String getWritingSystem() {
		return StringUtils.isEmpty(writingSystem) ? writingSystemImported : writingSystem;
	}

	public void setWritingSystem(String writingSystem) {
		this.writingSystem = writingSystem;
	}

	public String getNativeDatePrecise() {
		return nativeDatePrecise;
	}

	public void setNativeDatePrecise(String nativeDatePrecise) {
		this.nativeDatePrecise = nativeDatePrecise;
	}

	public boolean isCatchwords() {
		return catchwords;
	}

	public void setCatchwords(boolean catchwords) {
		this.catchwords = catchwords;
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

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		
		this.contents.clear();				
		
		if(CollectionUtils.isNotEmpty(contents)) {
			this.contents.addAll(contents);
			
			for(Content content : contents) {
				if(content != null) {
					content.setParentPart(this);
				}
			}
		}
	}

	public String getArtistUri() {
		return artistUri;
	}

	public void setArtistUri(String artistUri) {
		this.artistUri = artistUri;
	}

	public boolean isCityOfOriginUncertain() {
		return cityOfOriginUncertain;
	}

	public void setCityOfOriginUncertain(boolean cityOfOriginUncertain) {
		this.cityOfOriginUncertain = cityOfOriginUncertain;
	}

	public boolean isCountryOfOriginUncertain() {
		return countryOfOriginUncertain;
	}

	public void setCountryOfOriginUncertain(boolean countryOfOriginUncertain) {
		this.countryOfOriginUncertain = countryOfOriginUncertain;
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

	public List<ReadingRoomPartContributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<ReadingRoomPartContributor> contributors) {
		this.contributors = contributors;
	}
	
	public List<ReadingRoomPartContributor> getContributorsByType(Contributor.Type type) {
		List<ReadingRoomPartContributor> contributorsOfType = new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(contributors)) {
			for(ReadingRoomPartContributor partContributor : contributors) {				
				if(partContributor.getType() == type) {
					contributorsOfType.add(partContributor);
				}
			}
		}			
		
		return contributorsOfType;
	}
	
	public void addPartContributors(List<ReadingRoomPartContributor> partContributors) {			
		
		if(CollectionUtils.isNotEmpty(partContributors)) {
			for (ReadingRoomPartContributor partContributor : partContributors) {
				partContributor.setParentPart(this);
				this.contributors.add(partContributor);
			}
		}		
	}
	
	public void addPartContributors(List<ReadingRoomPartContributor> partContributors, Contributor.Type contributorType) {				
		
		for (ReadingRoomPartContributor partContributor : partContributors) {
			partContributor.setParentPart(this);
			partContributor.setType(contributorType);
			this.contributors.add(partContributor);
		}
	}

	public Type getType() {
		return type != null ? type : Type.UNKNOWN;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}	
	
	public List<ReadingRoomPartContributor> getScribes() {
		return getContributorsByType(Contributor.Type.SCRIBE);
	}
	
	public List<ReadingRoomPartContributor> getArtists() {
		return getContributorsByType(Contributor.Type.ARTIST);
	}
	
	public List<ReadingRoomPartContributor> getAssociatedNames() {
		List<ReadingRoomPartContributor> associatedNames = new ArrayList<>();		
		
		if(CollectionUtils.isNotEmpty(contributors)) {
			for(ReadingRoomPartContributor contributor : contributors) {
				Contributor.Type type = contributor.getType();
				if(Contributor.Type.SCRIBE != type && Contributor.Type.ARTIST != type) {
					associatedNames.add(contributor);
				}
			}
		}
		
		return associatedNames;
	}
	
	// authors are at the content level, convenience method to get a unique list of authors associated with a part
	public List<Contributor> getAuthors() {
		Map<Long, Contributor> uniqueAuthors = new HashMap<>();
		
		if(CollectionUtils.isNotEmpty(contents)) {
			
			for(Content content : contents) {
				List<ContentContributor> contentAuthors = content.getAuthors();
				
				if(CollectionUtils.isNotEmpty(contentAuthors)) {
					for(ContentContributor contentAuthor : contentAuthors) {
						uniqueAuthors.put(contentAuthor.getId(), contentAuthor.getContributor());
					}
				}
			}			
		}
		
		return Lists.newArrayList(uniqueAuthors.values());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ReadingRoomObjectPart other = (ReadingRoomObjectPart) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public List<Integer> getCenturies() {
		return centuries;
	}

	public void setCenturies(List<Integer> centuries) {
		this.centuries = centuries;
	}

	public boolean isCenturyUncertain() {
		return centuryUncertain;
	}

	public void setCenturyUncertain(boolean centuryUncertain) {
		this.centuryUncertain = centuryUncertain;
	}
	
	public String getPartTypeName() {
		return this.getType().getName();
	}
	
	public String getCenturyDisplay() {
		return AttributeUtil.getCenturyDisplay(centuries, centuryUncertain, centuryImported);		
	}
	
	public String getYearRange() {
		return AttributeUtil.getYearRange(beginDate, endDate);
	}
	
	public String getSupportDimensions() {
		String supportDimensions = null;
		
		if(supportDimensionsHeight != null && supportDimensionsHeight != null) {
			supportDimensions = AttributeUtil.getDimensionAttribute(supportDimensionsHeight, supportDimensionsWidth, null);
		} else if (StringUtils.isNotEmpty(supportDimensionsImported)) {
			supportDimensions = supportDimensionsImported;
		}
		
		return supportDimensions; 
	}
	
	public String getWritingSpace() {
		return AttributeUtil.getDimensionAttribute(writingSpaceHeight, writingSpaceWidth, null);
	}

	public String getScriptImported() {
		return scriptImported;
	}

	public void setScriptImported(String scriptImported) {
		this.scriptImported = scriptImported;
	}
	
	public String getPlaceOfOrigin() {
		StringBuilder placeOfOrigin = new StringBuilder();
		
		if(StringUtils.isNotEmpty(cardinalOfOrigin)) {
			placeOfOrigin.append(cardinalOfOrigin).append(" ");
		}
		
		String country = countryOfOrigin != null && countryOfOriginUncertain ? countryOfOrigin + " (?)" : countryOfOrigin;
		String city = cityOfOrigin != null && cityOfOriginUncertain ? cityOfOrigin + " (?)" : cityOfOrigin;

		placeOfOrigin.append(Joiner.on(", ").skipNulls().join(country, regionOfOrigin, city));		
		
		return placeOfOrigin.toString().trim();
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
	
	public String getLanguageDisplay() {
		StringBuilder languageDisplay = new StringBuilder();
		List<String> languages = new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(contents)) {
			for(Content content : contents) {				
				if(CollectionUtils.isNotEmpty(content.getLanguages())) {
					for(Language lang : content.getLanguages()) {
						if(!languages.contains(lang.getName())) {
							languages.add(lang.getName());
						}
					}
				}				
			}
			
			Collections.sort(languages);
			Iterator<String> iter = languages.iterator();
			
			if (iter.hasNext()) {
				languageDisplay.append(iter.next());

				while (iter.hasNext()) {
					languageDisplay.append("; ").append(iter.next());
				}
			}			
		}
		
		return languageDisplay.toString();
	}
	
	public String getLanguageList() {
		String languageList = getLanguageDisplay();
		
		if(StringUtils.isNotEmpty(languageList)) {
			languageList.replaceAll("; ", " ");
		}
		
		return languageList;
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

	public String getWritingSpaceImported() {
		return writingSpaceImported;
	}

	public void setWritingSpaceImported(String writingSpaceImported) {
		this.writingSpaceImported = writingSpaceImported;
	}

	public String getFoliationImported() {
		return foliationImported;
	}

	public void setFoliationImported(String foliationImported) {
		this.foliationImported = foliationImported;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}
}
