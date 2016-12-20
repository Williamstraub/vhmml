package org.vhmml.entity.readingroom;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reading_room_imports")
public class Import {

	public enum Status {
		CREATED("Created"),
		STARTED("Started"),
		FAILED("Failed"),
		COMPLETED("Completed"),
		DELETED("Deleted");
		
		Status(String displayName) {
			this.displayName = displayName;
		}
		
		private String displayName;
		
		public String getDisplayName() {
			return displayName;
		}
	}	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "import_file")
	private String importFile;
	
	@Column(name = "import_date")
	private Date importDate;
	
	@Enumerated
	private Status status;
	
	@Column(name = "error_message")
	private String errorMessage;
	
	@Column(name = "objects_from_source")	
	private Integer objectsFromSource;
	
	@Column(name = "objects_before_import")
	private Integer objectsBeforeImport;
	
	@Column(name = "objects_after_import")
	private Integer objectsAfterImport;	
	
	@Column(name = "parts_from_source")
	private Integer partsFromSource;
	
	@Column(name = "parts_before_import")
	private Integer partsBeforeImport;
	
	@Column(name = "parts_after_import")
	private Integer partsAfterImport;
	
	@Column(name = "contents_from_source")
	private Integer contentsFromSource;
	
	@Column(name = "contents_before_import")
	private Integer contentsBeforeImport;
	
	@Column(name = "contents_after_import")
	private Integer contentsAfterImport;
	
	@Column(name = "images_from_source")
	private Integer imagesFromSource;
	
	@Column(name = "images_before_import")
	private Integer imagesBeforeImport;
	
	@Column(name = "images_after_import")
	private Integer imagesAfterImport;
	
	@Column(name = "content_contributors_before_import")
	private Integer contentContributorsBeforeImport;
	
	@Column(name = "content_contributors_after_import")
	private Integer contentContributorsAfterImport;
	
	@Column(name = "content_languages_before_import")
	private Integer contentLanguagesBeforeImport;
	
	@Column(name = "content_languages_after_import")
	private Integer contentLanguagesAfterImport;
	
	@Column(name = "duration_seconds")	
	private Long durationSeconds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImportFile() {
		return importFile;
	}

	public void setImportFile(String importFile) {
		this.importFile = importFile;
	}

	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}

	public Integer getObjectsFromSource() {
		return objectsFromSource;
	}

	public void setObjectsFromSource(Integer objectsFromSource) {
		this.objectsFromSource = objectsFromSource;
	}

	public Integer getObjectsBeforeImport() {
		return objectsBeforeImport;
	}

	public void setObjectsBeforeImport(Integer objectsBeforeImport) {
		this.objectsBeforeImport = objectsBeforeImport;
	}

	public Integer getObjectsAfterImport() {
		return objectsAfterImport;
	}

	public void setObjectsAfterImport(Integer objectsAfterImport) {
		this.objectsAfterImport = objectsAfterImport;
	}

	public Integer getPartsFromSource() {
		return partsFromSource;
	}

	public void setPartsFromSource(Integer partsFromSource) {
		this.partsFromSource = partsFromSource;
	}

	public Integer getPartsBeforeImport() {
		return partsBeforeImport;
	}

	public void setPartsBeforeImport(Integer partsBeforeImport) {
		this.partsBeforeImport = partsBeforeImport;
	}

	public Integer getPartsAfterImport() {
		return partsAfterImport;
	}

	public void setPartsAfterImport(Integer partsAfterImport) {
		this.partsAfterImport = partsAfterImport;
	}

	public Integer getContentsFromSource() {
		return contentsFromSource;
	}

	public void setContentsFromSource(Integer contentsFromSource) {
		this.contentsFromSource = contentsFromSource;
	}

	public Integer getContentsBeforeImport() {
		return contentsBeforeImport;
	}

	public void setContentsBeforeImport(Integer contentsBeforeImport) {
		this.contentsBeforeImport = contentsBeforeImport;
	}

	public Integer getContentsAfterImport() {
		return contentsAfterImport;
	}

	public void setContentsAfterImport(Integer contentsAfterImport) {
		this.contentsAfterImport = contentsAfterImport;
	}

	public Integer getImagesFromSource() {
		return imagesFromSource;
	}

	public void setImagesFromSource(Integer imagesFromSource) {
		this.imagesFromSource = imagesFromSource;
	}

	public Integer getImagesBeforeImport() {
		return imagesBeforeImport;
	}

	public void setImagesBeforeImport(Integer imagesBeforeImport) {
		this.imagesBeforeImport = imagesBeforeImport;
	}

	public Integer getImagesAfterImport() {
		return imagesAfterImport;
	}

	public void setImagesAfterImport(Integer imagesAfterImport) {
		this.imagesAfterImport = imagesAfterImport;
	}

	public Integer getContentContributorsBeforeImport() {
		return contentContributorsBeforeImport;
	}

	public void setContentContributorsBeforeImport(Integer contentContributorsBeforeImport) {
		this.contentContributorsBeforeImport = contentContributorsBeforeImport;
	}

	public Integer getContentContributorsAfterImport() {
		return contentContributorsAfterImport;
	}

	public void setContentContributorsAfterImport(Integer contentContributorsAfterImport) {
		this.contentContributorsAfterImport = contentContributorsAfterImport;
	}

	public Integer getContentLanguagesBeforeImport() {
		return contentLanguagesBeforeImport;
	}

	public void setContentLanguagesBeforeImport(Integer contentLanguagesBeforeImport) {
		this.contentLanguagesBeforeImport = contentLanguagesBeforeImport;
	}

	public Integer getContentLanguagesAfterImport() {
		return contentLanguagesAfterImport;
	}

	public void setContentLanguagesAfterImport(Integer contentLanguagesAfterImport) {
		this.contentLanguagesAfterImport = contentLanguagesAfterImport;
	}

	public Long getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(Long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getDisplayDate() {
		String displayDate = "";
		
		if(importDate != null) {
			displayDate = new SimpleDateFormat("yyyy/MM/dd 'at' hh:mm aaa z").format(importDate);
		}
		
		return displayDate;
	}
}
