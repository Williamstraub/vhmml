package org.vhmml.dto.zotero;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.util.ZoteroReferenceItemDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ZoteroReferenceItemDeserializer.class)
public class ZoteroReferenceItem {
	
	private String blogTitle;
	private String bookTitle;
	private List<String> collections;
	private String conferenceName;
	private String date;
	private String dictionaryTitle;
	private String director;
	private String edition;	
	private String encyclopediaTitle;
	private String episodeNumber;
	private String institution;
	private String issue;
	private String itemType;
	private String key;
	private String manuscriptType;
	private String pages;
	private String place;
	private String proceedingsTitle;
	private String publicationTitle;
	private String publisher;
	private String series;
	private String seriesNumber;
	private String seriesTitle;
	private String shortTitle;
	private String studio;
	private String title;
	private String type;
	private String university;
	private String url;
	private Integer version;
	private String volume;
	private boolean imported;
	private List<ZoteroReferenceCreator> creators;
	private List<ZoteroReferenceTag> tags;

	public ZoteroReferenceItem() {
		super();
	}
	
	public ZoteroReferenceItem(ReferenceEntry entry) {
		BeanUtils.copyProperties(entry, this);
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
	
	public List<String> getCollections() {
		return collections;
	}

	public void setCollections(List<String> collections) {
		this.collections = collections;
	}

	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public boolean isImported() {
		return imported;
	}

	public void setImported(boolean imported) {
		this.imported = imported;
	}
	
	public List<ZoteroReferenceCreator> getCreators() {
		return creators;
	}

	public void setCreators(List<ZoteroReferenceCreator> creators) {
		this.creators = creators;
	}

	public List<ZoteroReferenceTag> getTags() {
		return tags;
	}

	public void setTags(List<ZoteroReferenceTag> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((shortTitle == null) ? 0 : shortTitle.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		ZoteroReferenceItem other = (ZoteroReferenceItem) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (shortTitle == null) {
			if (other.shortTitle != null)
				return false;
		} else if (!shortTitle.equals(other.shortTitle))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
