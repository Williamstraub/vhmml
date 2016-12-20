package org.vhmml.entity.readingroom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.Identifiable;
import org.vhmml.entity.User;
import org.vhmml.security.VhmmlSecurityUtil;
import org.vhmml.util.AttributeUtil;
import org.vhmml.util.NumberUtil;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

@Entity
@Table(name = "reading_room_objects")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "object_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "READING_ROOM_OBJECT")
public class ReadingRoomObject implements Identifiable {
	
	public enum Type {
		READING_ROOM_OBJECT("READING_ROOM_OBJECT", "Not Specified"),
		MANUSCRIPT("MANUSCRIPT", "Manuscript"),
		PRINT("PRINT", "Print"),
		MANUSCRIPT_PRINT("MANUSCRIPT_PRINT", "Manuscript & Print"),
		ARCHIVAL_OBJECT("ARCHIVAL_OBJECT", "Archival Material");
		
		Type(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}
		
		private String name;
		private String displayName;
		
		public String getName() {
			return name;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		
		public static Type forName(String name) {
        	Type type = READING_ROOM_OBJECT;
        	
        	for(Type nextType : Type.values()) {
        		if(name != null && nextType.getName().equals(name)) {
        			type = nextType;
        			break;
        		}
        	}        	
        	
        	return type;
        }
		
		public static Type forDisplayName(String displayName) {
        	Type type = READING_ROOM_OBJECT;
        	
        	for(Type nextType : Type.values()) {
        		if(displayName != null && nextType.getDisplayName().equals(displayName)) {
        			type = nextType;
        			break;
        		}
        	}        	
        	
        	return type;
        }
	}
	
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum Status {
		IN_SITU("In situ"),
		DESTROYED("Destroyed"),
		RELOCATED("Relocated"),
		MISSING("Missing"),
		SOLD("Sold"),
		UNKNOWN("Unknown");		
		
		Status(String displayName) {
			this.displayName = displayName;
		}
		
		private String displayName;
		
		public String getDisplayName() {
			return displayName;
		}
		
		public static Status forDisplayName(String name) {
			Status status = null;
        	
        	for(Status nextStatus : Status.values()) {
        		if(nextStatus.displayName.equals(name)) {
        			status = nextStatus;
        			break;
        		}
        	}        	
        	
        	return status;
        }
	}
	
	public enum ExtentType {
		PAGES("page(s)"),
		LEAVES("leaf(ves)"),
		PLATES("plate(s)"),
		SHEETS("sheet(s)");		
		
		ExtentType(String displayName) {
			this.displayName = displayName;
		}
		
		private String displayName;
		
		public String getDisplayName() {
			return displayName;
		}
	}
	
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum AccessRestriction {		
		REGISTERED("REGISTERED", "Registered or order a digital copy"),
		REGISTERED_ONLY("REGISTERED_ONLY", "Registered"), // can't order a digital copy
		UNREGISTERED("UNREGISTERED", "Unregistered or order a digital copy"),
		ON_SITE_ONLY_ORDER_SCAN("ON_SITE_ONLY_ORDER_SCAN", "On-site only or order a scan"),
		ON_SITE_ONLY("ON_SITE_ONLY", "On-site only");

		AccessRestriction(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}
		
		private String name;
		private String displayName;
		
		public String getName() {
			return name;
		}
		
		public String getDisplayName() {
			return displayName;
		} 
		
		public static AccessRestriction forName(String name) {
			AccessRestriction accessRestriction = null;
        	
        	for(AccessRestriction nextAccessRestriction : AccessRestriction.values()) {
        		if(nextAccessRestriction.name.equals(name)) {
        			accessRestriction = nextAccessRestriction;
        			break;
        		}
        	}        	
        	
        	return accessRestriction;
        }
		
		public static AccessRestriction forDisplayName(String displayName) {
			AccessRestriction accessRestriction = null;
        	
        	for(AccessRestriction nextAccessRestriction : AccessRestriction.values()) {
        		if(nextAccessRestriction.displayName.equals(displayName)) {
        			accessRestriction = nextAccessRestriction;
        			break;
        		}
        	}        	
        	
        	return accessRestriction;
        }
	}
	
	public enum DownloadOption {
		NO("No"),
		SMALL("Small"),
		MEDIUM("Medium"),
		LARGE("Large"),
		ALL("All");

		DownloadOption(String displayName) {
			this.displayName = displayName;
		}
		
		private String displayName;
		
		public String getDisplayName() {
			return displayName;
		}
	}

	@Id
	@GeneratedValue
	@Column(insertable = true)
	private Long id;
	
	private String summary;
	
	@Column(name = "processed_by")
	private String processedBy;	
	
	@Column(name = "shelf_mark")
	private String shelfMark;
	
	@Column(name = "common_name")
	private String commonName;	
	
	@Column(name = "physical_notes")
	private String physicalNotes;
	
	@Column(name = "condition_notes")
	private String conditionNotes;
	 
	private String collation;
	
	private String binding;
	
	@Column(name = "binding_dimensions_imported")
	private String bindingDimensionsImported;
	
	@Column(name = "binding_width")
	private Float bindingWidth;
	
	@Column(name = "binding_height")
	private Float bindingHeight;
	
	@Column(name = "binding_depth")
	private Float bindingDepth;
	
	private String provenance;
	private String bibliography;
	private String notes;
	private String colophon;
	private String medium;
	private String foliation;
	
	@Column(name = "hmml_project_number")
	private String hmmlProjectNumber;
	
	@Column(name = "inputter")
	private String inputter;
	
	@Column(name = "input_date")
	private String inputDate;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;	
	
	@Column(name = "reproduction_notes")
	private String reproductionNotes;
	
	private String acknowledgments;			
	
	@Column(name = "capture_date")
	private Date captureDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "object_type", insertable = false, updatable = false)
	private Type objectType;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "access_restriction")
	private AccessRestriction accessRestriction;
	
	@Column(name = "viewable_online")
	private boolean viewableOnline;	
	
	@Column(name = "download_option")
	private String downloadOption;	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="reading_room_object_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<FacsimileUrl> externalFacsimileUrls;
	
	@Column(name = "icon_name")
	private String iconName;
	
	@Column(name = "cite_as")
	private String citeAs;			
	
	@Column(name = "data_source")
	private String dataSource;	
	
	@Column(name = "images_location")
	private String imagesLocation;
	
	// support actually are an attribute of parts but we store them denormalized here for performance
	private String support;
	
	private boolean active;
	
	private boolean locked;
	
	@Column(name = "locked_by")
	private String lockedBy;
	
	@Column(name = "public_manifest")
	private boolean publicManifest;
	
	@Column(name = "right_to_left")
	private boolean rightToLeft;	
	
	@Column(name = "assigned_scholar")
	private String assignedScholar;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="country_id")
	private Country country;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="city_id")
	private City city;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="holding_institution_id")
	private Institution holdingInstitution;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="repository_id")
	private Repository repository;	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentObject", orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("partNumber")
	private List<ReadingRoomObjectPart> parts = new ArrayList<ReadingRoomObjectPart>();
		
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="surrogate_format_id")
	private Format surrogateFormat;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="reading_room_object_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<AlternateSurrogate> alternateSurrogates;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="reading_room_object_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<BibliographyUrl> externalBibliographyUrls;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="reading_room_object_id")
	@Fetch(FetchMode.SUBSELECT)
	private List<Extent> extents;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reading_room_object_genres", joinColumns = @JoinColumn(name = "reading_room_object_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private List<Genre> genres;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reading_room_object_subjects", joinColumns = @JoinColumn(name = "reading_room_object_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private List<Subject> subjects;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reading_room_object_features", joinColumns = @JoinColumn(name = "reading_room_object_id"), inverseJoinColumns = @JoinColumn(name = "feature_id"))
	private List<org.vhmml.entity.readingroom.Feature> features;
	
	@Column(name = "features_imported")
	private String featuresImported;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "current_status")
	private Status currentStatus;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="reading_room_object_id")
	@Fetch(FetchMode.SUBSELECT)	
	private List<ReadingRoomObjectContributor> contributors = new ArrayList<ReadingRoomObjectContributor>();
	
	@Transient
	private boolean editable;
	
	public ReadingRoomObject() {
		super();
	}
	
	public ReadingRoomObject(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public String getShelfMark() {
		return shelfMark;
	}

	public void setShelfMark(String shelfMark) {
		this.shelfMark = shelfMark;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public List<Extent> getExtents() {
		return extents;
	}

	public void setExtents(List<Extent> extents) {
		this.extents = extents;
	}

	public String getPhysicalNotes() {
		return physicalNotes;
	}

	public void setPhysicalNotes(String physicalNotes) {
		this.physicalNotes = physicalNotes;
	}
	
	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
	}

	public String getProvenance() {
		return provenance;
	}

	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	public String getBibliography() {
		return bibliography;
	}

	public void setBibliography(String bibliography) {
		this.bibliography = bibliography;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public String getReproductionNotes() {
		return reproductionNotes;
	}

	public void setReproductionNotes(String reproductionNotes) {
		this.reproductionNotes = reproductionNotes;
	}

	public String getAcknowledgments() {
		return acknowledgments;
	}

	public void setAcknowledgments(String acknowledgments) {
		this.acknowledgments = acknowledgments;
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Date getCaptureDate() {
		return captureDate;
	}
	
	public String getCaptureDateDisplay() {
		String captureDateDisplay = "";
		
		if(captureDate != null) {
			captureDateDisplay = new SimpleDateFormat("yyyy/MM/dd").format(captureDate);
		}
		
		return captureDateDisplay;
	}

	public void setCaptureDate(Date captureDate) {
		this.captureDate = captureDate;
	}

	public AccessRestriction getAccessRestriction() {
		return accessRestriction;
	}

	public void setAccessRestriction(AccessRestriction accessRestriction) {
		this.accessRestriction = accessRestriction;
	}

	public boolean isViewableOnline() {
		return viewableOnline;
	}

	public void setViewableOnline(boolean viewableOnline) {
		this.viewableOnline = viewableOnline;
	}

	public String getDownloadOption() {
		return downloadOption;
	}

	public void setDownloadOption(String downloadOption) {
		this.downloadOption = downloadOption;
	}

	public List<FacsimileUrl> getExternalFacsimileUrls() {
		return externalFacsimileUrls;
	}

	public void setExternalFacsimileUrls(List<FacsimileUrl> externalFacsimileUrls) {
		this.externalFacsimileUrls = externalFacsimileUrls;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getCiteAs() {
		return citeAs;
	}

	public void setCiteAs(String citeAs) {
		this.citeAs = citeAs;
	}

	public List<ReadingRoomObjectPart> getParts() {
		return parts;
	}

	public void setParts(List<ReadingRoomObjectPart> parts) {
		this.parts.clear();
		this.parts.addAll(parts);
		
		// have to set parent object in bi-directional relationship for cascade persist to work
		if(CollectionUtils.isNotEmpty(parts)) {
			for(ReadingRoomObjectPart part : parts) {
				if(part != null) {
					part.setParentObject(this);
					List<Content> partContents = part.getContents();
					if(CollectionUtils.isNotEmpty(partContents)) {
						for(Content content : partContents) {
							content.setParentPart(part);
						}
					}
				}
			}
		}
	}

	public Format getSurrogateFormat() {
		return surrogateFormat;
	}

	public void setSurrogateFormat(Format surrogateFormat) {
		this.surrogateFormat = surrogateFormat;
	}
	
	public String getFormat() {
		String format = null;
		
		if(surrogateFormat != null ){
			format = surrogateFormat.getName();
		}
			
		return format;
	}
	
	public Type getType() {
		Type type = Type.READING_ROOM_OBJECT;
		
		if(this.getClass().getName().equals(ArchivalObject.class.getName())) {
			type = Type.ARCHIVAL_OBJECT;
		} else if(this.getClass().getName().equals(Manuscript.class.getName())) {
			type = Type.MANUSCRIPT;
		} else if(this.getClass().getName().equals(ManuscriptAndPrinted.class.getName())) {
			type = Type.MANUSCRIPT_PRINT;
		} else if(this.getClass().getName().equals(PrintedObject.class.getName())) {
			type = Type.PRINT;
		}
		
		return type;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	public List<Content> getAllContent() {
		List<Content> allContent = new ArrayList<Content>();
		
		if(CollectionUtils.isNotEmpty(parts)) {
			for(ReadingRoomObjectPart part : parts) {
				
				List<Content> partContent = part.getContents();
				allContent.addAll(partContent);				
			}
		}
		
		return allContent;
	}
	
	public List<ContentContributor> getAssociatedNames() {
		List<ContentContributor> associatedNames = new ArrayList<>();		
		List<Content> allContent = getAllContent();
				
		for(Content content : allContent) {
			List<ContentContributor> contentContributors = content.getContentContributors();
			
			if(CollectionUtils.isNotEmpty(contentContributors)) {
				
				for(ContentContributor contentContributor : contentContributors) {
					if(Contributor.Type.AUTHOR != contentContributor.getType()) {
						associatedNames.add(contentContributor);
					}
				}				
			}
		}
		
		return associatedNames;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Institution getHoldingInstitution() {
		return holdingInstitution;
	}

	public void setHoldingInstitution(Institution holdingInstitution) {
		this.holdingInstitution = holdingInstitution;
	}

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public List<org.vhmml.entity.readingroom.Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<org.vhmml.entity.readingroom.Feature> features) {
		this.features = features;
	}	

	public List<AlternateSurrogate> getAlternateSurrogates() {
		return alternateSurrogates;
	}

	public void setAlternateSurrogates(List<AlternateSurrogate> alternateSurrogates) {
		this.alternateSurrogates = alternateSurrogates;
	}

	public List<BibliographyUrl> getExternalBibliographyUrls() {
		return externalBibliographyUrls;
	}

	public void setExternalBibliographyUrls(List<BibliographyUrl> externalBibliographyUrls) {
		this.externalBibliographyUrls = externalBibliographyUrls;
	}

	public Float getBindingWidth() {
		return bindingWidth;
	}

	public void setBindingWidth(Float bindingWidth) {
		this.bindingWidth = bindingWidth;
	}

	public Float getBindingHeight() {
		return bindingHeight;
	}

	public void setBindingHeight(Float bindingHeight) {
		this.bindingHeight = bindingHeight;
	}

	public Float getBindingDepth() {
		return bindingDepth;
	}

	public void setBindingDepth(Float bindingDepth) {
		this.bindingDepth = bindingDepth;
	}

	public List<ReadingRoomObjectContributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<ReadingRoomObjectContributor> contributors) {
		this.contributors = contributors;
	}
	
	public List<Contributor> getContributorsByType(Contributor.Type type) {
		List<Contributor> contributorsOfType = new ArrayList<Contributor>();
		
		if(CollectionUtils.isNotEmpty(contributors)) {
			for(ReadingRoomObjectContributor objectContributor : contributors) {				
				if(objectContributor.getType() == type) {
					contributorsOfType.add(objectContributor.getContributor());
				}
			}
		}			
		
		return contributorsOfType;
	}

	public String getConditionNotes() {
		return conditionNotes;
	}

	public void setConditionNotes(String conditionNotes) {
		this.conditionNotes = conditionNotes;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getColophon() {
		return colophon;
	}

	public void setColophon(String colophon) {
		this.colophon = colophon;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getImagesLocation() {
		return imagesLocation;
	}

	public void setImagesLocation(String imagesLocation) {
		this.imagesLocation = imagesLocation;
	}

	public String getSupport() {
		return support;
	}

	public void setSupport(String support) {
		this.support = support;
	}
	
	public String getCenturyDisplay() {
		
		String centuryDisplay = null;
		
		if(CollectionUtils.isNotEmpty(parts)) {
			boolean centuryUncertain = false;
			List<Integer> allCenturies = new ArrayList<Integer>();
			
			for(ReadingRoomObjectPart part : parts) {
				centuryUncertain = part.isCenturyUncertain() || centuryUncertain;
				allCenturies.addAll(part.getCenturies());				
			}
			
			if(CollectionUtils.isNotEmpty(allCenturies)) {
				Integer firstCentury = (Integer)Collections.min(allCenturies);
				Integer lastCentury = (Integer)Collections.max(allCenturies);
				
				if (firstCentury != null) {
					centuryDisplay = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(firstCentury, lastCentury), centuryUncertain);
				}
			}			
		}
		
		return centuryDisplay;
	}
	
	public String getLanguageDisplay() {
		StringBuilder languageDisplay = new StringBuilder();
		List<String> languages = new ArrayList<String>();
		
		if(CollectionUtils.isNotEmpty(parts)) {
			for(ReadingRoomObjectPart part : parts) {
				
				if(CollectionUtils.isNotEmpty(part.getContents())) {
					for(Content content : part.getContents()) {
						
						if(CollectionUtils.isNotEmpty(content.getLanguages())) {
							for(Language lang : content.getLanguages()) {
								if(!languages.contains(lang.getName())) {
									languages.add(lang.getName());
								}
							}
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
			languageList = languageList.replaceAll("; ", " ");
		}
		
		return languageList;
	}
	
	public List<Contributor> getFormerOwners() {
		return getContributorsByType(Contributor.Type.FORMER_OWNER);
	}
	
	// authors are at the content level, this is a convenience method to get all unique authors for an object
	public List<Contributor> getAuthors() {
		Map<Long, Contributor> uniqueAuthors = new HashMap<>();
		
		if(CollectionUtils.isNotEmpty(parts)) {
			
			for(ReadingRoomObjectPart part : parts) {
				List<Contributor> partAuthors = part.getAuthors();
				
				if(CollectionUtils.isNotEmpty(partAuthors)) {
					for(Contributor partAuthor : partAuthors) {
						uniqueAuthors.put(partAuthor.getId(), partAuthor);
					}
				}
			}			
		}
		
		return Lists.newArrayList(uniqueAuthors.values());
	}
	
	public String getRecordTypeName() {
		return this.getType().displayName;
	}	
	
	public String getExtentList() {
		StringBuilder extentList = new StringBuilder();
		
		if(CollectionUtils.isNotEmpty(extents)) {
			Iterator<Extent> iter = extents.iterator();
			
			if (iter.hasNext()) {
				Extent extent = iter.next();
				
				if(extent.getCount() == null && extent.getDisplayName() == null && extent.getFolioImported() != null) {
					extentList.append(extent.getFolioImported());
				} else {					
					extentList.append(getExtentString(extent));										
				}				

				while (iter.hasNext()) {
					String extentString = getExtentString(iter.next());					
					if(StringUtils.isNotEmpty(extentString)) {
						extentList.append("; ").append(extentString);
					}					
				}
			}
		}
		
		return extentList.toString();
	}
	
	private String getExtentString(Extent extent) {
		StringBuilder extentString = new StringBuilder();
		
		String extentType = extent.getDisplayName();
		
		if(StringUtils.isNotEmpty(extentType)) {
			
			if(extent.getCount() != null) {
				extentString.append(extent.getCount()).append(" ");
			}
			
			extentString.append(extentType);
		}	
		
		return extentString.toString();
	}
	
	public String getBindingDimensions() {
		return AttributeUtil.getDimensionAttribute(bindingHeight, bindingWidth, bindingDepth);		
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}
	
	public String getAccessRestrictionPreLinkText() {
		String preLinkText = null;
		
		if(accessRestriction != null) {
			String accessRestrictionDisplay = accessRestriction.displayName;
			int indexOfOrder = accessRestrictionDisplay.indexOf("order");
			
			if(indexOfOrder != -1) {
				preLinkText = accessRestrictionDisplay.substring(0, indexOfOrder); 
			}
		}
		
		return preLinkText;
	}
	
	public String getAccessRestrictionLinkText() {
		String linkText = null;				
		
		if(accessRestriction != null) {
			String accessRestrictionDisplay = accessRestriction.displayName;
			int indexOfOrder = accessRestrictionDisplay.indexOf("order");
			
			if(indexOfOrder != -1) {
				linkText = accessRestrictionDisplay.substring(indexOfOrder); 
			}
			
			// TODO: clean this up, this text needs to be determined by the combination of format + access restriction 
			// we shouldn't have the text tied to the access restriction only
			// access restrictions should just be "On-site only", "On-site or order a copy", etc. (as opposed to "On-site or order a scan") 
			if(linkText != null && surrogateFormat != null && "Digital".equals(surrogateFormat.getName())) {
				linkText = linkText.replace("scan", "copy");
			}
		}
		
		return linkText;
	}

	public String getFoliation() {
		return foliation;
	}

	public void setFoliation(String foliation) {
		this.foliation = foliation;
	}

	public String getFeaturesImported() {
		return featuresImported;
	}

	public void setFeaturesImported(String featuresImported) {
		this.featuresImported = featuresImported;
	}
	
	public String getBindingDimensionsImported() {
		return bindingDimensionsImported;
	}

	public void setBindingDimensionsImported(String bindingDimensionsImported) {
		this.bindingDimensionsImported = bindingDimensionsImported;
	}

	public String getAssignedScholar() {
		return assignedScholar;
	}

	public void setAssignedScholar(String assignedScholar) {
		this.assignedScholar = assignedScholar;
	}

	public boolean isPublicManifest() {
		return publicManifest;
	}

	public void setPublicManifest(boolean publicManifest) {
		this.publicManifest = publicManifest;
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

	public Type getObjectType() {
		return objectType;
	}

	public void setObjectType(Type objectType) {
		this.objectType = objectType;
	}
	
	public String getCollection() {
		StringBuilder collection = new StringBuilder();
		
		// split on space
		// look at each token, if it's a number, all the tokens before that one are the collection
		if(StringUtils.isNotEmpty(hmmlProjectNumber)) {
			String[] tokens = hmmlProjectNumber.split(" ");
			
			for(int i = 0; i < tokens.length; i++) {
				String token = tokens[i];
				
				if(NumberUtils.isDigits(token)) {
					for(int j = 0; j < i; j++) {
						collection.append(tokens[j]);
						collection.append(j + 1 < i ? " " : "");
					}
				}
			}			
		}
		
		return collection.length() == 0 ? hmmlProjectNumber : collection.toString();
	}

	@Override
	public String toString() {
		return "ReadingRoomObject [id=" + id + ", hmmlProjectNumber=" + hmmlProjectNumber + "]";
	}
}
