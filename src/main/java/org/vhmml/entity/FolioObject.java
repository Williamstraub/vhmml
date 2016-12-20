package org.vhmml.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.vhmml.util.AttributeUtil;

@Entity
@Table(name = "folio_objects")
public class FolioObject implements Identifiable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@NotNull
	@Column(name = "folio_object_number")
	private String folioObjectNumber;
	
	@Column(name = "icon_name")
	private String iconName;
	
	@Column
	private String country;
	
	@Column
	private String city;
	
	@Column
	private String repository;
	
	@Column(name = "shelf_mark")
	private String shelfMark;
	
	@Column(name = "common_name")
	private String commonName;
	
	@Column
	private String provenance;
	
	@Column
	private String bibliography;
	
	@Column(name = "external_url")
	private String externalUrl;
	
	@Column(name = "permanent_link")
	private String permanentLink;
	
	@Column
	private String acknowledgements;
	
	@Column(name = "place_of_origin")
	private String placeOfOrigin;
	
	@Column(name = "date_precise")
	private String datePrecise;
	
	@Column(name = "begin_date")
	private Integer beginDate;
	
	@Column(name = "end_date")
	private Integer endDate;
	
	@Column(name = "date_century")
	private String dateCentury;
	
	@Column
	private String language;
	
	@Column(name = "writing_system")
	private String writingSystem;
	
	@Column
	private String script;
	
	@Column
	private String title;
	
	@Column
	private String text;
	
	@Column
	private String description;
	
	@Column(name = "special_features")
	private String specialFeatures;
	
	@Column
	private String transcription;
	
	@Column
	private Boolean active;
	
	@Transient
	private Boolean editable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getFolioObjectNumber() {
		return folioObjectNumber;
	}

	public void setFolioObjectNumber(String folioObjectNumber) {
		this.folioObjectNumber = folioObjectNumber;
	}
	
	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
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
	
	public String getExternalUrl() {
		return externalUrl;
	}

	public void setExternalUrl(String externalUrl) {
		this.externalUrl = externalUrl;
	}

	public String getPermanentLink() {
		return permanentLink;
	}

	public void setPermanentLink(String permanentLink) {
		this.permanentLink = permanentLink;
	}

	public String getAcknowledgements() {
		return acknowledgements;
	}

	public void setAcknowledgements(String acknowledgements) {
		this.acknowledgements = acknowledgements;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getDatePrecise() {
		return datePrecise;
	}

	public void setDatePrecise(String datePrecise) {
		this.datePrecise = datePrecise;
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

	public String getDateCentury() {
		return dateCentury;
	}

	public void setDateCentury(String dateCentury) {
		this.dateCentury = dateCentury;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getWritingSystem() {
		return writingSystem;
	}

	public void setWritingSystem(String writingSystem) {
		this.writingSystem = writingSystem;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getYearRange() {
		return AttributeUtil.getYearRange(beginDate, endDate);
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}
