package org.vhmml.entity.readingroom;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.elasticsearch.common.lang3.StringUtils;
import org.vhmml.entity.Identifiable;
import org.vhmml.entity.User;
import org.vhmml.security.VhmmlSecurityUtil;
import org.vhmml.util.NumberUtil;

import com.google.common.collect.Lists;

@Entity
@Table(name = "reading_room_overview")
public class ReadingRoomObjectOverview implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "hmml_project_number")
	private String hmmlProjectNumber;

	private String titles;
	
	@Column(name = "titles_ns")
	private String titlesNs;

	@Column(name = "alt_titles")
	private String alternateTitles;

	@Column(name = "uniform_titles")
	private String uniformTitles;

	private String inputter;

	@Column(name = "input_date")
	private String inputDate;
	
	@Column(name = "assigned_scholar")
	private String assignedScholar;

	@Column(name = "shelf_mark")
	private String shelfmark;

	@Column(name = "object_type")
	private String objectType;

	private String extent;
	
	@Column(name = "alternate_surrogates")
	private String alternateSurrogates;
	
	private String provenance;
	private String binding;

	@Column(name = "processed_by")
	private String processedBy;

	@Column(name = "access_restriction")
	private String accessRestriction;

	private String format;
	private String country;
	private String city;
	private String repository;
	
	@Column(name = "holding_institution")
	private String holdingInstitution;
	
	private String centuries;

	@Column(name = "first_century")
	private Integer firstCentury;

	@Column(name = "last_century")
	private Integer lastCentury;
	
	@Column(name = "begin_date")
	private Integer beginDate;

	@Column(name = "end_date")
	private Integer endDate;
	
	private String decoration;
	
	@Column(name = "century_uncertain")
	private Boolean centuryUncertain;

	private String support;
	private String dimensions;
	private String languages;
	private String authors;
	
	@Column(name = "authors_ns")
	private String authorsNs;
	
	@Column(name = "authors_display")
	private String authorsDisplay;

	@Column(name = "other_contributors")
	private String otherContributors;
	
	@Column(name = "other_contributors_display")
	private String otherContributorsDisplay;
	
	@Column(name = "part_other_contributors")
	private String partOtherContributors;
	
	@Column(name = "part_other_contributors_display")
	private String partOtherContributorsDisplay;	

	private String subjects;
	private String genres;

	@Column(name = "icon_name")
	private String iconName;	
	
	private boolean active;	
	private boolean locked;
	
	@Column(name = "locked_by")
	private String lockedBy;
	
	private boolean downloadable;	
	private String incipit;
	private String features;
	private String scripts;
	
	@Column(name = "former_owners")
	private String formerOwners;
	
    private String artists;
    private String scribes; 
    
    @Column(name = "scribes_ns")
    private String scribesNs;     
	
	@Column(name = "writing_systems")
	private String writingSystems;
	
	@Column(name = "capture_date")
	private Date captureDate;
	
	@Column(name = "place_of_origin")
	private String placeOfOrigin;
	
	@Column(name = "common_name")
	private String commonName;
	
	private String notes;
	
	private String bibliography;
	
	@Column(name = "archival_creators")
	private String archivalCreators;
	
	@Column(name = "archival_title")
	private String archivalTitle;
	
	@Column(name = "archival_content_description")
	private String archivalContentDescription;
	
	@Column(name = "archival_content_notes")
	private String archivalContentNotes;
	
	@Column(name = "archival_bibliography")
	private String archivalBibliography;
	
	@Column(name = "archival_scope_content")
	private String archivalScopeContent;
	
	@Column(name = "archival_custodial_history")
	private String archivalCustodialHistory;
	
	@Column(name = "archival_historical_note")
	private String archivalHistoricalNote;
	
	@Column(name = "archival_contributors")
	private String archivalContributors;
	
	@Column(name = "archival_contributors_display")
	private String archivalContributorsDisplay;
	
	@Transient
	private String centuryDisplay;

	@Transient
	private String centuryList;
	
	@Transient
	private boolean lockedForCurrentUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHmmlProjectNumber() {
		return hmmlProjectNumber;
	}

	public void setHmmlProjectNumber(String hmmlProjectNumber) {
		this.hmmlProjectNumber = hmmlProjectNumber;
	}

	public String getInputter() {
		return inputter;
	}

	public void setInputter(String inputter) {
		this.inputter = inputter;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getShelfmark() {
		return shelfmark;
	}

	public void setShelfmark(String shelfmark) {
		this.shelfmark = shelfmark;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public String getProvenance() {
		return provenance;
	}

	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getCenturies() {
		return centuries;
	}

	public void setCenturies(String centuries) {
		this.centuries = centuries;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public String getAccessRestriction() {
		return accessRestriction;
	}

	public void setAccessRestriction(String accessRestriction) {
		this.accessRestriction = accessRestriction;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getAlternateTitles() {
		return alternateTitles;
	}

	public void setAlternateTitles(String alternateTitles) {
		this.alternateTitles = alternateTitles;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getUniformTitles() {
		return uniformTitles;
	}

	public void setUniformTitles(String uniformTitles) {
		this.uniformTitles = uniformTitles;
	}

	public String getOtherContributors() {
		return otherContributors;
	}

	public void setOtherContributors(String otherContributors) {
		this.otherContributors = otherContributors;
	}

	public Integer getFirstCentury() {
		return firstCentury;
	}

	public void setFirstCentury(Integer firstCentury) {
		this.firstCentury = firstCentury;
	}

	public Integer getLastCentury() {
		return lastCentury;
	}

	public void setLastCentury(Integer lastCentury) {
		this.lastCentury = lastCentury;
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

	public String getCenturyDisplay() {
		String centuryDisplay = this.centuries;

		if (firstCentury != null) {
			centuryDisplay = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(firstCentury, lastCentury), Boolean.TRUE.equals(centuryUncertain));
		} else if(Boolean.TRUE.equals(centuryUncertain)) {
			// if first century isn't populated, then they haven't selected any centuries and we're just
			// displaying the imported oliver value, here we still respect the "centuryUncertain" flag and
			// put the question mark on the end of the century string if it doesn't already contain a ?
			if(StringUtils.isEmpty(centuryDisplay)) {
				centuryDisplay = "Century (?)";
			} else if(!centuryDisplay.contains("?")) {
				centuryDisplay += " (?)";
			}
			
		}

		return centuryDisplay;
	}

	public void setCenturyDisplay(String centuryDisplay) {
		this.centuryDisplay = centuryDisplay;
	}

	public String getCenturyList() {
		String centuryList = this.centuryList;

		if (firstCentury != null) {
			centuryList = NumberUtil.getCenturyList(firstCentury, lastCentury);
		}

		return centuryList;
	}

	public void setCenturyList(String centuryList) {
		this.centuryList = centuryList;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isLocked() {
		return locked;		
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Boolean getCenturyUncertain() {
		return centuryUncertain;
	}

	public void setCenturyUncertain(Boolean centuryUncertain) {
		this.centuryUncertain = centuryUncertain;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	public String getIncipit() {
		return incipit;
	}

	public void setIncipit(String incipit) {
		this.incipit = incipit;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getScripts() {
		return scripts;
	}

	public void setScripts(String scripts) {
		this.scripts = scripts;
	}

	public String getWritingSystems() {
		return writingSystems;
	}

	public void setWritingSystems(String writingSystems) {
		this.writingSystems = writingSystems;
	}

	public String getFormerOwners() {
		return formerOwners;
	}

	public void setFormerOwners(String formerOwners) {
		this.formerOwners = formerOwners;
	}

	public String getArtists() {
		return artists;
	}

	public void setArtists(String artists) {
		this.artists = artists;
	}

	public String getScribes() {
		return scribes;
	}

	public void setScribes(String scribes) {
		this.scribes = scribes;
	}

	public String getAuthorsNs() {
		return authorsNs;
	}

	public void setAuthorsNs(String authorsNs) {
		this.authorsNs = authorsNs;
	}

	public String getAuthorsDisplay() {
		return authorsDisplay;
	}

	public void setAuthorsDisplay(String authorsDisplay) {
		this.authorsDisplay = authorsDisplay;
	}

	public String getScribesNs() {
		return scribesNs;
	}

	public void setScribesNs(String scribesNs) {
		this.scribesNs = scribesNs;
	}

	public String getOtherContributorsDisplay() {
		return otherContributorsDisplay;
	}

	public void setOtherContributorsDisplay(String otherContributorsDisplay) {
		this.otherContributorsDisplay = otherContributorsDisplay;
	}

	public String getAlternateSurrogates() {
		return alternateSurrogates;
	}

	public void setAlternateSurrogates(String alternateSurrogates) {
		this.alternateSurrogates = alternateSurrogates;
	}

	public Date getCaptureDate() {
		return captureDate;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getTitlesNs() {
		return titlesNs;
	}

	public void setTitlesNs(String titlesNs) {
		this.titlesNs = titlesNs;
	}

	public String getBibliography() {
		return bibliography;
	}

	public void setBibliography(String bibliography) {
		this.bibliography = bibliography;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getArchivalCreators() {
		return archivalCreators;
	}

	public void setArchivalCreators(String archivalCreators) {
		this.archivalCreators = archivalCreators;
	}
	
	public String getArchivalTitle() {
		return archivalTitle;
	}

	public void setArchivalTitle(String archivalTitle) {
		this.archivalTitle = archivalTitle;
	}

	public String getArchivalContentDescription() {
		return archivalContentDescription;
	}

	public void setArchivalContentDescription(String archivalContentDescription) {
		this.archivalContentDescription = archivalContentDescription;
	}

	public String getArchivalContentNotes() {
		return archivalContentNotes;
	}

	public void setArchivalContentNotes(String archivalContentNotes) {
		this.archivalContentNotes = archivalContentNotes;
	}

	public String getArchivalBibliography() {
		return archivalBibliography;
	}

	public void setArchivalBibliography(String archivalBibliography) {
		this.archivalBibliography = archivalBibliography;
	}

	public String getArchivalScopeContent() {
		return archivalScopeContent;
	}

	public void setArchivalScopeContent(String archivalScopeContent) {
		this.archivalScopeContent = archivalScopeContent;
	}

	public String getArchivalCustodialHistory() {
		return archivalCustodialHistory;
	}

	public void setArchivalCustodialHistory(String archivalCustodialHistory) {
		this.archivalCustodialHistory = archivalCustodialHistory;
	}

	public String getArchivalHistoricalNote() {
		return archivalHistoricalNote;
	}

	public void setArchivalHistoricalNote(String archivalHistoricalNote) {
		this.archivalHistoricalNote = archivalHistoricalNote;
	}

	public String getArchivalContributors() {
		return archivalContributors;
	}

	public void setArchivalContributors(String archivalContributors) {
		this.archivalContributors = archivalContributors;
	}

	public String getArchivalContributorsDisplay() {
		return archivalContributorsDisplay;
	}

	public void setArchivalContributorsDisplay(String archivalContributorsDisplay) {
		this.archivalContributorsDisplay = archivalContributorsDisplay;
	}

	public String getAssignedScholar() {
		return assignedScholar;
	}

	public void setAssignedScholar(String assignedScholar) {
		this.assignedScholar = assignedScholar;
	}

	public String getPartOtherContributors() {
		return partOtherContributors;
	}

	public void setPartOtherContributors(String partOtherContributors) {
		this.partOtherContributors = partOtherContributors;
	}

	public String getPartOtherContributorsDisplay() {
		return partOtherContributorsDisplay;
	}

	public void setPartOtherContributorsDisplay(String partOtherContributorsDisplay) {
		this.partOtherContributorsDisplay = partOtherContributorsDisplay;
	}

	public String getHoldingInstitution() {
		return holdingInstitution;
	}

	public void setHoldingInstitution(String holdingInstitution) {
		this.holdingInstitution = holdingInstitution;
	}
	
	public boolean getLockedByDifferentUser() {
		boolean isLocked = locked;
		
		if(isLocked) {
			User user = VhmmlSecurityUtil.getCurrentUser();
			// if it's locked by the current user, it's not locked for them
			isLocked = user == null || !user.getUsername().equals(lockedBy);			
		}
		
		return isLocked;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
}
