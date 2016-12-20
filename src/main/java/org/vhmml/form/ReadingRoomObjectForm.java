package org.vhmml.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.vhmml.dto.ReadingRoomPartDTO;
import org.vhmml.entity.Identifiable;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Contributor.Type;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.entity.readingroom.Feature;
import org.vhmml.entity.readingroom.Genre;
import org.vhmml.entity.readingroom.Manuscript;
import org.vhmml.entity.readingroom.ManuscriptAndPrinted;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.Subject;

public class ReadingRoomObjectForm {

	private Long readingRoomObjectId;
	
	@NotNull(message = "Record Type is required")
	private ReadingRoomObject.Type recordType;
	
	private String inputter;
	
	private String lastUpdatedBy;
	
	private Date lastUpdate;
	
	private String assignedScholar;
	
	@NotNull(message = "Processed By Institution is required")
	private String processedBy;
	
	@NotNull(message = "Surrogate Format is required")
	private Long surrogateFormatId;
	
	private List<String> alternateSurrogateFormats;
	
	@NotNull(message = "Access Restrictions is required")
	private String accessRestriction;
	
	private Boolean viewableOnline;
	private String downloadOption;
	private List<FacsimileUrl> externalFacsimileUrls;
	private List<BibliographyUrl> externalBibliographyUrls;
	
	@NotNull(message = "HMML Project Number is required")
	private String hmmlProjectNumber;
	
	@NotNull(message = "Country is required")
	private Long countryId;
	
	@NotNull(message = "City is required")
	private Long cityId;	
	
	@NotNull(message = "Repository is required")
	private Long repositoryId;
	
	private Long holdingInstitutionId;
	
	private String shelfMark;
	
	@NotNull(message = "Current Status is required")
	private ReadingRoomObject.Status currentStatus;
		
	private String commonName;
	private String folioImported;
	private List<Extent> extents;	
	private List<Subject> subjects;
	private List<Genre> genres;
	private List<Long> featureIds;
	private String featuresImported;
	private String notes;
	private String bibliography;
	private String acknowledgments;	
	private String collation;
	private String foliation;
	private String binding;
	private String bindingDimensionsImported;
	private Float bindingWidth;
	private Float bindingHeight;
	private Float bindingDepth;
	private String bindingNotes;
	private String provenance;
	private List<Contributor> formerOwners;		
	private String conditionNotes;	
	private boolean active;
	private boolean publicManifest;
	private boolean rightToLeft;
	private String iconName;
	private String imageServer;
	private String imagesLocation;
	private String reproductionNotes;
	private Integer captureDateYear;
	private Integer captureDateMonth;
	private Integer captureDateDay;	
	private ArchivalData archivalData;
	private List<ReadingRoomPartDTO> parts;
	
	// have to explicitly track whether the object has parts or not because spring binding puts list of empty objects on form beans
	private boolean hasParts;
	
	public ReadingRoomObjectForm() {
		super();
	}
	
	public ReadingRoomObjectForm(ReadingRoomObject object) {			
		BeanUtils.copyProperties(object, this);
		
		switch (object.getType()) {
			case MANUSCRIPT:
				setCollation(((Manuscript)object).getCollation());
				break;
			case MANUSCRIPT_PRINT:
				setCollation(((ManuscriptAndPrinted)object).getCollation());				
				break;
			case ARCHIVAL_OBJECT:
				setArchivalData(((ArchivalObject)object).getArchivalData());
				break;
			case PRINT:
			case READING_ROOM_OBJECT:
				break;
		}
		
		setReadingRoomObjectId(object.getId());
		setAccessRestriction(object.getAccessRestriction().getName());
		setSurrogateFormatId(getId(object.getSurrogateFormat()));
		setCountryId(getId(object.getCountry()));
		setCityId(getId(object.getCity()));
		setRepositoryId(getId(object.getRepository()));				
		setHoldingInstitutionId(getId(object.getHoldingInstitution()));		
		setRecordType(object.getType());				
		setAlternateSurrogateFormats(object);
		setFormerOwners(object);
		setGenres(object.getGenres());
		
		if(CollectionUtils.isNotEmpty(object.getFeatures())) {
			List<Long> featureIds = new ArrayList<Long>();
			
			for(Feature feature : object.getFeatures()) {
				featureIds.add(feature.getId());
			}
			
			setFeatureIds(featureIds);
		}				
		
		if(CollectionUtils.isNotEmpty(object.getParts())) {			
			List<ReadingRoomPartDTO> partList = new ArrayList<ReadingRoomPartDTO>();
			
			for(ReadingRoomObjectPart part : object.getParts()) {				
				partList.add(new ReadingRoomPartDTO(part));
			}
			
			this.parts = partList;
		}
		
		if(object.getCaptureDate() != null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(object.getCaptureDate());			
			this.captureDateYear = cal.get(Calendar.YEAR);
			this.captureDateMonth = cal.get(Calendar.MONTH);
			this.captureDateDay = cal.get(Calendar.DAY_OF_MONTH);
		}
	}	
	
	private Long getId(Identifiable identifiable) {
		Long id = null;
		
		if(identifiable != null) {
			id = identifiable.getId();
		}
		
		return id;
	}

	public ReadingRoomObject.Type getRecordType() {
		return recordType;
	}

	public void setRecordType(ReadingRoomObject.Type recordType) {
		this.recordType = recordType;
	}
	
	public String getInputter() {
		return inputter;
	}

	public void setInputter(String inputter) {
		this.inputter = inputter;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getHmmlProjectNumber() {
		return hmmlProjectNumber;
	}

	public void setHmmlProjectNumber(String hmmlProjectNumber) {
		this.hmmlProjectNumber = hmmlProjectNumber;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getHoldingInstitutionId() {
		return holdingInstitutionId;
	}

	public void setHoldingInstitutionId(Long holdingInstitutionId) {
		this.holdingInstitutionId = holdingInstitutionId;
	}

	public Long getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(Long repositoryId) {
		this.repositoryId = repositoryId;
	}

	public ReadingRoomObject.Status getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(ReadingRoomObject.Status currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getShelfMark() {
		return shelfMark;
	}

	public void setShelfMark(String shelfMark) {
		this.shelfMark = shelfMark;
	}
	
	public String getFolioImported() {
		return folioImported;
	}

	public void setFolioImported(String folioImported) {
		this.folioImported = folioImported;
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

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public List<Genre> getGenres() {		
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;		
	}
	
	public List<Genre> getUniqueGenres() {
		List<Genre> uniqueGenres = new ArrayList<>();
		
		// hack to get around the fact that the user can add the same genre multiple times
		if(CollectionUtils.isNotEmpty(genres)) {
			
			List<Long> genreIds = new ArrayList<>();
			
			for(Genre genre : genres) {
				Long genreId = genre.getId();
		
				// have to compare ids because genre's equals method only compares name and the names will be all null because the form only sends up the id
				if(genreId != null && !genreIds.contains(genreId)) {
					genreIds.add(genreId);
					uniqueGenres.add(genre);
				}
			}					
		}
		
		return uniqueGenres;
	}

	public List<Long> getFeatureIds() {
		return featureIds;
	}

	public void setFeatureIds(List<Long> featureIds) {
		this.featureIds = featureIds;
	}
	
	public String getFeaturesImported() {
		return featuresImported;
	}

	public void setFeaturesImported(String featuresImported) {
		this.featuresImported = featuresImported;
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

	public String getAcknowledgments() {
		return acknowledgments;
	}

	public void setAcknowledgments(String acknowledgments) {
		this.acknowledgments = acknowledgments;
	}

	public String getAccessRestriction() {
		return accessRestriction;
	}

	public void setAccessRestriction(String accessRestriction) {
		this.accessRestriction = accessRestriction;
	}

	public Boolean getViewableOnline() {
		// default viewable online to true
		return viewableOnline == null ? true : viewableOnline;
	}

	public void setViewableOnline(Boolean viewableOnline) {
		this.viewableOnline = viewableOnline;
	}

	public Long getSurrogateFormatId() {
		return surrogateFormatId;
	}

	public void setSurrogateFormatId(Long surrogateFormatId) {
		this.surrogateFormatId = surrogateFormatId;
	}

	public List<String> getAlternateSurrogateFormats() {
		return alternateSurrogateFormats;
	}

	public void setAlternateSurrogateFormats(List<String> alternateSurrogateFormats) {
		this.alternateSurrogateFormats = alternateSurrogateFormats;
	}
	
	public void setAlternateSurrogateFormats(ReadingRoomObject readingRoomObject) {
		List<AlternateSurrogate> alternateSurrogates = readingRoomObject.getAlternateSurrogates();
		
		if(CollectionUtils.isNotEmpty(alternateSurrogates)) {
			List<String> altSurrogates = new ArrayList<String>();
			
			for(AlternateSurrogate surrogate : alternateSurrogates) {
				altSurrogates.add(surrogate.getName());
			}
			
			setAlternateSurrogateFormats(altSurrogates);
		}
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

	public List<BibliographyUrl> getExternalBibliographyUrls() {
		return externalBibliographyUrls;
	}

	public void setExternalBibliographyUrls(List<BibliographyUrl> externalBibliographyUrls) {
		this.externalBibliographyUrls = externalBibliographyUrls;
	}	

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}
	
	public String getBindingDimensionsImported() {
		return bindingDimensionsImported;
	}

	public void setBindingDimensionsImported(String bindingDimensionsImported) {
		this.bindingDimensionsImported = bindingDimensionsImported;
	}

	public String getBinding() {
		return binding;
	}

	public void setBinding(String binding) {
		this.binding = binding;
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

	public String getBindingNotes() {
		return bindingNotes;
	}

	public void setBindingNotes(String bindingNotes) {
		this.bindingNotes = bindingNotes;
	}

	public String getProvenance() {
		return provenance;
	}

	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	public List<Contributor> getFormerOwners() {
		return formerOwners;
	}

	public void setFormerOwners(List<Contributor> formerOwners) {
		this.formerOwners = formerOwners;
	}
	
	public void setFormerOwners(ReadingRoomObject readingRoomObject) {
		List<ReadingRoomObjectContributor> objectContributors = readingRoomObject.getContributors();
		
		if(CollectionUtils.isNotEmpty(objectContributors)) {
			List<Contributor> formerOwners = new ArrayList<Contributor>();
			
			for(ReadingRoomObjectContributor objectContributor : objectContributors) {
				Contributor contributor = objectContributor.getContributor();
				
				if(objectContributor.getType() == Type.FORMER_OWNER && contributor != null) {
					formerOwners.add(contributor);
				}
			}
			
			setFormerOwners(formerOwners);
		}
	}

	public String getConditionNotes() {
		return conditionNotes;
	}

	public void setConditionNotes(String conditionNotes) {
		this.conditionNotes = conditionNotes;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Long getReadingRoomObjectId() {
		return readingRoomObjectId;
	}

	public void setReadingRoomObjectId(Long readingRoomObjectId) {
		this.readingRoomObjectId = readingRoomObjectId;
	}

	public List<ReadingRoomPartDTO> getParts() {
		return parts;
	}

	public void setParts(List<ReadingRoomPartDTO> parts) {
		this.parts = parts;
	}

	public String getIconName() {		
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
	public String getIconUrl() {
		String iconUrl = "";
		
		if(StringUtils.isNotEmpty(iconName)) {
			iconUrl = "/image/thumbnail/READING_ROOM/" + getHmmlProjectNumber() + "/" + getIconName();
		}
		
		return iconUrl;
	}

	public String getImageServer() {
		return imageServer;
	}

	public void setImageServer(String imageServer) {
		this.imageServer = imageServer;
	}

	public String getImagesLocation() {
		return imagesLocation;
	}

	public void setImagesLocation(String imagesLocation) {
		this.imagesLocation = imagesLocation;
	}

	public boolean getHasParts() {
		return hasParts;
	}

	public void setHasParts(Boolean hasParts) {
		this.hasParts = hasParts;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public String getReproductionNotes() {
		return reproductionNotes;
	}

	public void setReproductionNotes(String reproductionNotes) {
		this.reproductionNotes = reproductionNotes;
	}

	public Integer getCaptureDateYear() {
		return captureDateYear;
	}

	public void setCaptureDateYear(Integer captureDateYear) {
		this.captureDateYear = captureDateYear;
	}

	public Integer getCaptureDateMonth() {
		return captureDateMonth;
	}

	public void setCaptureDateMonth(Integer captureDateMonth) {
		this.captureDateMonth = captureDateMonth;
	}

	public Integer getCaptureDateDay() {
		return captureDateDay;
	}

	public void setCaptureDateDay(Integer captureDateDay) {
		this.captureDateDay = captureDateDay;
	}
	
	public boolean getHasContent() {
		boolean hasContent = false;
		
		if(CollectionUtils.isNotEmpty(parts)) {
			for(ReadingRoomPartDTO part : parts) {
				if(part.isHasContent()) {
					hasContent = true;
					break;
				}
			}
		}
		
		return hasContent;
	}

	public String getFoliation() {
		return foliation;
	}

	public void setFoliation(String foliation) {
		this.foliation = foliation;
	}

	public ArchivalData getArchivalData() {
		return archivalData;
	}

	public void setArchivalData(ArchivalData archivalData) {
		this.archivalData = archivalData;
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
}
