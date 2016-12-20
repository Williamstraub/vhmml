package org.vhmml.entity.readingroom;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.vhmml.entity.Identifiable;
import org.vhmml.util.AttributeUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_archival_content")
public class ArchivalContent extends Content implements Identifiable {

	public enum Type {
		NOT_SPECIFIED("Not Specified"),
		MANUSCRIPT("Manuscript"),
		PRINTED("Printed"),
		MANUSCRIPT_PRINT("Manuscript & Print");
		
		private String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "archival_data_id")
	private ArchivalData parentArchivalData;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "item_type")
	private Type type;
	
	@Column
	private Integer folder;
	
	@Column
	private Integer item;
	
	@Column
	private String extent;
	
	@Column
	private String description;
	
	@Column(name = "date_precise_year")
	private Integer datePreciseYear;
	
	@Column(name = "date_precise_month")
	private Integer datePreciseMonth;
	
	@Column(name = "date_precise_day")
	private Integer datePreciseDay;
	
	@Column(name = "native_date")
	private String nativeDate;
	
	@Column(name = "begin_date")
	private Integer beginDate;
	
	@Column(name = "end_date")
	private Integer endDate;
	
	@Column
	private String support;
	
	@Column(name = "support_width")
	private Float supportDimensionsWidth;
	
	@Column(name = "support_height")
	private Float supportDimensionsHeight;
	
	@Transient
	private List<String> supportList;
	
	@Column
	private String medium;
	
	@Column(name = "page_layout")
	private String pageLayout;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reading_room_archival_content_centuries", joinColumns = {@JoinColumn(name="archival_content_id")})
	@Column(name = "century")
	private List<Integer> centuries;
	
	@Column(name = "century_uncertain")
	private boolean centuryUncertain;
	
	@Column
	private String content;
	
	@Column(name = "content_ns")
	private String contentNs;
	
	@Column(name = "total_folios")
	private String totalFolios;
	
	@Column(name = "span_of_folios")
	private String spanOfFolios;
	
	@Column(name = "country_of_origin")
	private String countryOfOrigin;
	
	@Column(name = "city_of_origin")
	private String cityOfOrigin;
	
	@Column
	private String language;
	
	@Column(name = "status_of_text")
	private String statusOfText;
	
	@Column(name = "scribe_notary")
	private String scribeNotary;
	
	@Column(name = "item_condition")
	private String itemCondition;
	
	@Column
	private String notes;
	
	@Column
	private String bibliography;
	
	@Column
	private String decoration;
	
	// scripts are stored in the reading_room_scripts table instead of an enum because we need the ability to add them 
	// dynamically through the app, but we store them on the parts denormalized for performance since they are just a name
	@Column
	private String script;
	
	@Transient
	private List<String> scriptList;
	
	@Transient
	private List<Long> languageIds;
	
	// writing systems are stored in the reading_room_writing_systems table instead of an enum because we need the 
	// ability to add them dynamically through the app, but we store them denormalized for performance since they are just a name
	@Column(name = "writing_system")
	private String writingSystem;
	
	@Transient
	private List<String> writingSystemList;
	
	// the following are all just subsets of the contributors list that contain specific types of contributor
	// that aren't persisted, they are just set by Spring MVC when the form is deserialized.	
	@Transient
	private List<ContentContributor> scribes;
	
	@Transient
	private List<ContentContributor> artists;
	
	@Transient
	private List<ContentContributor> associatedNames;	

	public Integer getFolder() {
		return folder;
	}

	public void setFolder(Integer folder) {
		this.folder = folder;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentNs() {
		return contentNs;
	}

	public void setContentNs(String contentNs) {
		this.contentNs = contentNs;
	}

	public String getTotalFolios() {
		return totalFolios;
	}

	public void setTotalFolios(String totalFolios) {
		this.totalFolios = totalFolios;
	}

	public String getSpanOfFolios() {
		return spanOfFolios;
	}

	public void setSpanOfFolios(String spanOfFolios) {
		this.spanOfFolios = spanOfFolios;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguagesDisplay() {
		List<String> languageNames = new ArrayList<>();
		List<Language> languages = getLanguages();
		
		for(Language language : languages) {
			languageNames.add(language.getName());
		}
		
		return AttributeUtil.getListAttributeAsString(languageNames);
	}
	
	public String getStatusOfText() {
		return statusOfText;
	}

	public void setStatusOfText(String statusOfText) {
		this.statusOfText = statusOfText;
	}

	public String getScribeNotary() {
		return scribeNotary;
	}

	public void setScribeNotary(String scribeNotary) {
		this.scribeNotary = scribeNotary;
	}

	public ArchivalData getParentArchivalData() {
		return parentArchivalData;
	}

	public void setParentArchivalData(ArchivalData parentArchivalData) {
		this.parentArchivalData = parentArchivalData;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getDatePreciseDisplay() {
		return AttributeUtil.getDatePreciseDisplay(this.datePreciseYear, this.datePreciseMonth, this.datePreciseDay);		
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
	
	public String getYearRange() {
		return AttributeUtil.getYearRange(beginDate, endDate);
	}

	public List<Integer> getCenturies() {
		return centuries;
	}
	
	public String getCenturyDisplay() {
		return AttributeUtil.getCenturyDisplay(centuries, centuryUncertain, null);		
	}

	public void setCenturies(List<Integer> centuries) {
		this.centuries = centuries;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}
	
	public List<String> getSupportList() {
		
		if(supportList == null && StringUtils.isNotEmpty(support)) {
			supportList = AttributeUtil.getStringAttributeAsList(support);
		}
		
		return supportList;
	}
	
	public String getSupportDisplay() {
		return Support.getSupportDisplay(support, null);		
	}

	public void setSupportList(List<String> supportList) {
		this.supportList = supportList;
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
	
	public String getSupportDimensions() {
		String supportDimensions = null;
		
		if(supportDimensionsHeight != null && supportDimensionsHeight != null) {
			supportDimensions = AttributeUtil.getDimensionAttribute(supportDimensionsHeight, supportDimensionsWidth, null);
		}
		
		return supportDimensions; 
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(String pageLayout) {
		this.pageLayout = pageLayout;
	}
	
	public List<ContentContributor> getScribes() {
		return this.scribes;
	}

	public void setScribes(List<ContentContributor> scribes) {
		this.scribes = scribes;
	}

	public List<ContentContributor> getArtists() {
		return this.artists;
	}

	public void setArtists(List<ContentContributor> artists) {
		this.artists = artists;		
	}

	public List<ContentContributor> getAssociatedNames() {
		return this.associatedNames;		
	}

	public void setAssociatedNames(List<ContentContributor> associatedNames) {
		this.associatedNames = associatedNames;
	}
	
	public List<Long> getLanguageIds() {
		List<Language> languages = getLanguages();
		
		if(languageIds == null && CollectionUtils.isNotEmpty(languages)) {
			List<Long> languageIds = new ArrayList<Long>();
			
			for(Language language : languages) {
				languageIds.add(language.getId());
			}
			
			this.languageIds = languageIds;
		}
		
		return languageIds;
	}

	public void setLanguageIds(List<Long> languageIds) {
		this.languageIds = languageIds;
	}

	public String getScript() {
		return script;
	}
	
	public List<String> getScriptList() {
		
		if(scriptList == null && StringUtils.isNotEmpty(script)) {
			scriptList = AttributeUtil.getStringAttributeAsList(script);
		}
		
		return scriptList;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public void setScriptList(List<String> scriptList) {
		this.scriptList = scriptList;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBibliography() {
		return bibliography;
	}

	public void setBibliography(String bibliography) {
		this.bibliography = bibliography;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}

	public String getWritingSystem() {
		return writingSystem;
	}

	public void setWritingSystem(String writingSystem) {
		this.writingSystem = writingSystem;
	}
	
	public List<String> getWritingSystemList() {
		return this.writingSystemList;
	}
	
	public void setWritingSystemList(List<String> writingSystemList) {
		this.writingSystemList = writingSystemList;
	}

	public String getNativeDate() {
		return nativeDate;
	}

	public void setNativeDate(String nativeDate) {
		this.nativeDate = nativeDate;
	}

	public boolean isCenturyUncertain() {
		return centuryUncertain;
	}

	public void setCenturyUncertain(boolean centuryUncertain) {
		this.centuryUncertain = centuryUncertain;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getTypeName() {
		return this.getType().getName();
	}
}
