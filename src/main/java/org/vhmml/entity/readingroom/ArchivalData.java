package org.vhmml.entity.readingroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.Identifiable;
import org.vhmml.util.AttributeUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

@Entity
@Table(name = "reading_room_archival_data")	
public class ArchivalData implements Identifiable {

	public ArchivalData() {
		super();		
	}
	
	public ArchivalData(Long id) {
		this.id = id;
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "reading_room_object_id")
	private ArchivalObject parentObject;
	
	@Column(name = "collection_fond")
	private String collectionFond;
	
	@Column
	private String series;
	
	@Column(name = "sub_series")
	private String subSeries;
	
	@Column(name = "sub_sub_series")
	private String subSubSeries;
	
	@Column(name = "container_type")
	private String containerType;
	
	@Column(name = "container_count")
	private Integer containerCount;
	
	@Column
	private String title;
	
	@Column
	private String creator;
	
	@Column(name = "scope_content")
	private String scopeContent;
	
	@Column(name = "historical_note")
	private String historicalNote;
	
	@Column(name = "rec_type")
	private String recType;
	
	@Column(name = "custodial_history")
	private String custodialHistory;
	
	@Column
	private String dimensions;
	
	@Column
	private String support;
	
	@Transient
	private List<String> supportList;
	
	@Column(name = "date_precise_year")
	private Integer datePreciseYear;
	
	@Column(name = "date_precise_month")
	private Integer datePreciseMonth;
	
	@Column(name = "date_precise_day")
	private Integer datePreciseDay;
	
	@Column(name = "begin_date")
	private Integer beginDate;
	
	@Column(name = "end_date")
	private Integer endDate;
	
	@Column(name = "native_date")
	private String nativeDate;
	
	@Column
	private String decoration;
	
	@Column(name = "century_imported")
	private String centuryImported;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reading_room_archival_data_centuries", joinColumns = {@JoinColumn(name="archival_data_id")})
	@Column(name = "century")
	private List<Integer> centuries;
	
	@Column(name = "century_uncertain")
	private boolean centuryUncertain;
	
	// transient list of language ids, used to send up ids of selected language ids
	@Transient
	private List<Long> languageIds;	
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reading_room_archival_data_languages", joinColumns = @JoinColumn(name = "archival_data_id"), inverseJoinColumns = @JoinColumn(name = "language_id"))
	private List<Language> languages;
	
	// writing systems are stored in the reading_room_writing_systems table instead of an enum because we need the 
	// ability to add them dynamically through the app, but we store them denormalized for performance since they are just a name
	@Column(name = "writing_system")
	private String writingSystem;
	
	@Transient
	private List<String> writingSystemList;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentArchivalData")
	@Fetch(FetchMode.SUBSELECT)	
	private List<ArchivalContributor> associatedNames = new ArrayList<ArchivalContributor>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentArchivalData", orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("folder,item")
	private List<ArchivalContent> content = new ArrayList<ArchivalContent>();
	
	// Have to manually track if the data has content because spring mvc binds a list with one empty object 
	// (object with all null attributes) if there is no content. The reason this is on the entity is because
	// we aren't currently using a DTO to populate the add/edit form, we're just using the entity since it
	// matches the DTO would almost exactly match the entity, might end up using a DTO if this becomes a mess
	@Transient
	private boolean hasArchivalContent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectionFond() {
		return collectionFond;
	}

	public void setCollectionFond(String collectionFond) {
		this.collectionFond = collectionFond;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getSubSeries() {
		return subSeries;
	}

	public void setSubSeries(String subSeries) {
		this.subSeries = subSeries;
	}
	
	public String getSubSubSeries() {
		return subSubSeries;
	}

	public void setSubSubSeries(String subSubSeries) {
		this.subSubSeries = subSubSeries;
	}
	
	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}
	
	public Integer getContainerCount() {
		return containerCount;
	}

	public void setContainerCount(Integer containerCount) {
		this.containerCount = containerCount;
	}
	
	public String getContainer() {
		StringBuilder container = new StringBuilder();
		
		if(this.containerCount != null) {
			container.append(this.containerCount);
		}
		
		if(StringUtils.isNotEmpty(this.containerType)) {
			if(container.length() > 0) {
				container.append(" ");
			}
			
			container.append(this.containerType);
		}
		
		return container.toString();
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getScopeContent() {
		return scopeContent;
	}

	public void setScopeContent(String scopeContent) {
		this.scopeContent = scopeContent;
	}
	
	public String getHistoricalNote() {
		return historicalNote;
	}

	public void setHistoricalNote(String historicalNote) {
		this.historicalNote = historicalNote;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public String getCustodialHistory() {
		return custodialHistory;
	}

	public void setCustodialHistory(String custodialHistory) {
		this.custodialHistory = custodialHistory;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getSupport() {
		return support;
	}
	
	public String getSupportDisplay() {
		return Support.getSupportDisplay(support, null);		
	}

	public void setSupport(String support) {
		this.support = support;
	}

	public List<ArchivalContent> getContent() {
		return content;
	}

	public void setContent(List<ArchivalContent> content) {
		
		if(this.content != null) {
			this.content.clear();
		}				
		
		// have to set parent object in bi-directional relationship for cascade persist to work
		if(CollectionUtils.isNotEmpty(content)) {
			this.content.addAll(content);
			
			for(ArchivalContent nextContent : content) {
				if(nextContent != null) {
					nextContent.setParentArchivalData(this);					
				}
			}
		}
		
		this.content = content;
	}

	public List<ArchivalContributor> getAssociatedNames() {
		return associatedNames;
	}

	public void setAssociatedNames(List<ArchivalContributor> associatedNames) {
		this.associatedNames = associatedNames;
		
		if(CollectionUtils.isNotEmpty(associatedNames)) {
			for (ArchivalContributor associatedName : associatedNames) {
				associatedName.setParentArchivalData(this);
			}
		}
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

	public void setCenturies(List<Integer> centuries) {
		this.centuries = centuries;
	}
	
	public String getCenturyDisplay() {
		return AttributeUtil.getCenturyDisplay(centuries, centuryUncertain, centuryImported);		
	}

	public String getNativeDate() {
		return nativeDate;
	}

	public void setNativeDate(String nativeDate) {
		this.nativeDate = nativeDate;
	}

	public List<String> getSupportList() {
		
		if(supportList == null && StringUtils.isNotEmpty(support)) {
			supportList = AttributeUtil.getStringAttributeAsList(support);
		}
		
		return supportList;
	}

	public void setSupportList(List<String> supportList) {
		this.supportList = supportList;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
	
	public String getLanguagesDisplay() {
		List<String> languageNames = new ArrayList<>();
		
		for(Language language : languages) {
			languageNames.add(language.getName());
		}
		
		return AttributeUtil.getListAttributeAsString(languageNames);
	}

	public List<Long> getLanguageIds() {
		
		if(languageIds == null && languages != null) {
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

	public boolean getHasArchivalContent() {
		return this.hasArchivalContent;
	}

	public void setHasArchivalContent(boolean hasArchivalContent) {
		this.hasArchivalContent = hasArchivalContent;
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
		return writingSystemList;
	}

	public void setWritingSystemList(List<String> writingSystemList) {
		this.writingSystemList = writingSystemList;
	}

	public boolean isCenturyUncertain() {
		return centuryUncertain;
	}

	public void setCenturyUncertain(boolean centuryUncertain) {
		this.centuryUncertain = centuryUncertain;
	}	
	
	public String getScripts() {
		String scripts = null;
		
		if(CollectionUtils.isNotEmpty(content)) {
			Map<String, String> uniqueScripts = new HashMap<>();
			
			for(ArchivalContent nextContent : content) {
				List<String> scriptList = nextContent.getScriptList();
				
				if(CollectionUtils.isNotEmpty(scriptList)) {
					for(String script : scriptList) {
						uniqueScripts.put(script, script);
					}					
				}
			}
			
			List<String> allScripts = Lists.newArrayList(uniqueScripts.keySet());
			Collections.sort(allScripts);
			scripts = StringUtils.join(allScripts, ",");
		}
		
		return scripts;
	}

	public ArchivalObject getParentObject() {
		return parentObject;
	}

	public void setParentObject(ArchivalObject parentObject) {
		this.parentObject = parentObject;
	}

	public String getCenturyImported() {
		return centuryImported;
	}

	public void setCenturyImported(String centuryImported) {
		this.centuryImported = centuryImported;
	}
}
