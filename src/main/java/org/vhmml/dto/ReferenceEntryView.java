package org.vhmml.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.entity.ReferenceEntry.DisplayField;

public class ReferenceEntryView {
	
	private Long id;	
	private String author;
	private String blogTitle;	
	private String bookTitle;	
	private String bookAuthor;	
	private String conferenceName;	
	private String contributor;	
	private String displayDate;	
	private String dictionaryTitle;	
	private String director;
	private String edition;	
	private String editor;
	private String encyclopediaTitle;	
	private String episodeNumber;	
	private String institution;		
	private String issue;
	private ReferenceEntry.Type itemType;		
	private String manuscriptType;
	private String pages;
	private String place;	
	private String podcaster;
	private String proceedingsTitle;	
	private String publicationTitle;
	private String publisher;	
	private String series;	
	private String seriesNumber;		
	private String seriesTitle;		
	private String shortTitle;	
	private String studio;
	private String tags;
	private String title;
	private String translator;	
	private String type;
	private String university;
	private String url;
	private Integer version;
	private String volume;
	private String zoteroKey;
	private List<DisplayField> displayFields;
	
	public ReferenceEntryView(ReferenceEntry referenceEntry) {
		BeanUtils.copyProperties(referenceEntry, this);		
		this.setType(referenceEntry.getItemType().getDisplayName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBlogTitle() {
		return blogTitle;
	}

	public void setBlogTitle(String blogTitle) {
		this.blogTitle = blogTitle;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public String getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}

	public String getDictionaryTitle() {
		return dictionaryTitle;
	}

	public void setDictionaryTitle(String dictionaryTitle) {
		this.dictionaryTitle = dictionaryTitle;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEncyclopediaTitle() {
		return encyclopediaTitle;
	}

	public void setEncyclopediaTitle(String encyclopediaTitle) {
		this.encyclopediaTitle = encyclopediaTitle;
	}
	
	public String getEpisodeNumber() {
		return episodeNumber;
	}

	public void setEpisodeNumber(String episodeNumber) {
		this.episodeNumber = episodeNumber;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public ReferenceEntry.Type getItemType() {
		return itemType;
	}

	public void setItemType(ReferenceEntry.Type itemType) {
		this.itemType = itemType;
	}
	
	public String getManuscriptType() {
		return manuscriptType;
	}

	public void setManuscriptType(String manuscriptType) {
		this.manuscriptType = manuscriptType;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getPodcaster() {
		return podcaster;
	}

	public void setPodcaster(String podcaster) {
		this.podcaster = podcaster;
	}

	public String getProceedingsTitle() {
		return proceedingsTitle;
	}

	public void setProceedingsTitle(String proceedingsTitle) {
		this.proceedingsTitle = proceedingsTitle;
	}

	public String getPublicationTitle() {
		return publicationTitle;
	}

	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getSeriesNumber() {
		return seriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}

	public String getSeriesTitle() {
		return seriesTitle;
	}

	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	public String getZoteroKey() {
		return zoteroKey;
	}

	public void setZoteroKey(String zoteroKey) {
		this.zoteroKey = zoteroKey;
	}

	public List<DisplayField> getDisplayFields() {
		return displayFields;
	}

	public void setDisplayFields(List<DisplayField> displayFields) {
		this.displayFields = displayFields;
	}
}
