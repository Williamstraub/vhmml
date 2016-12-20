package org.vhmml.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.vhmml.dto.zotero.ZoteroReferenceItem;
import org.vhmml.util.DisplayFieldSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "reference_entries")
public class ReferenceEntry {
	
	// this map & the following enums exists because we need to display the item fields in a specific order based on the type of item
	// both on the UI and also in the email we send to the user when the click the "email this to yourself" link
	private static Map<Type, List<DisplayField>> displayFieldsByEntryType = new HashMap<Type, List<DisplayField>>();
	
	static {		
		initFieldsByTypeMap();	
	}
	
	public enum Type {
		ATTACHMENT("attachment", "Attachment"),
		BLOG_POST("blogPost", "Blog Post"),
		BOOK("book", "Book"),
		BOOK_SECTION("bookSection", "Book Section"),
		CONFERENCE_PAPER("conferencePaper", "Conference Paper"),
		DICTIONARY_ENTRY("dictionaryEntry", "Dictionary Entry"),
		ENCYCLOPEDIA_ARTICLE("encyclopediaArticle", "Encyclopedia Article"),		
		JOURNAL_ARTICLE("journalArticle", "Journal Article"),
		MAGAZINE_ARTICLE("magazineArticle", "Magazine Article"),
		MANUSCRIPT("manuscript", "Manuscript"),
		NOTE("node", "Note"),
		PODCAST("podcast", "Podcast"),
		REPORT("report", "Report"),
		THESIS("thesis", "Thesis"),		
		VIDEO_RECORDING("videoRecording", "Video Recording"),
		WEBPAGE("webpage", "Web Page");		
		
		private String name;
		private String displayName;
		
		Type(String name, String displayName) {
			this.name = name;
			this.displayName = displayName;
		}
		
		public String getName() {
			return name;
		}

		public String getDisplayName() {
			return displayName;
		}
		
		public static Type forName(String name) {
        	Type type = null;
        	
        	for(Type nextType : Type.values()) {
        		if(nextType.name.equals(name)) {
        			type = nextType;
        			break;
        		}
        	}
        	if (type == null) {
        		throw new IllegalStateException("No entry type for name " + name + " found on " + ReferenceEntry.Type.class.getName());
        	}
        	
        	return type;
        }
	};
	
	@JsonSerialize(using = DisplayFieldSerializer.class)
	public enum DisplayField {
		AUTHORS("Author(s)", "author"),
		BLOG_TITLE("Blog Title", "blogTitle"),
		BOOK_TITLE("Book Title", "bookTitle"),
		CONFERENCE("Conference", "conferenceName"),
		CONTRIBUTORS("Contributor(s)", "contributor"),
		DATE("Date", "displayDate"),
		DICTIONARY_TITLE("Dictionary Title", "dictionaryTitle"),
		DIRECTORS("Director(s)", "director"),
		EDITION("Edition", "edition"),
		EDITORS("Editor(s)", "editor"),
		ENCYCLOPEDIA_TITLE("Encyclopedia Title", "encyclopediaTitle"),
		EPISODE_NUMBER("Episode Number", "episodeNumber"),
		INSTITUTION("Institution", "institution"),
		ISSUE("Issue", "issue"),
		PAGES("Page(s)", "pages"),
		PLACE("Place", "place"),
		PODCASTER("Podcaster", "podcaster"),
		PROCEEDINGS_TITLE("Proceedings Title", "proceedingsTitle"),
		PUBLICATION("Publication", "publicationTitle"),
		PUBLISHER("Publisher", "publisher"),
		SERIES_NUMBER("Series Number", "seriesNumber"),
		SERIES_TITLE("Series Title", "seriesTitle"),
		SERIES("Series", "series"),
		SHORT_TITLE("Short Title", "shortTitle"),
		STUDIO("Studio", "studio"),
		TITLE("Title", "title"),
		MANUSCRIPT_TYPE("Type", "manuscriptType"),
		TYPE("Type", "type"),
		UNIVERSITY("University", "university"),
		VOLUME("Volume", "volume"),
		TAGS("Tag(s)", "tags"),
		URL("URL","url");
		
		private String name;
		private String label;
		
		DisplayField(String label, String name) {			
			this.label = label;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public String getLabel() {
			return label;
		}
	};

	@Id
	@GeneratedValue
	private Long id;	

	@Column(name = "blog_title")
	private String blogTitle;

	@Column(name = "book_title")
	private String bookTitle;

	@Column(name = "conference_name")
	private String conferenceName;

	private String date;

	@Column(name = "dictionary_title")
	private String dictionaryTitle;

	private String edition;

	@Column(name = "encyclopedia_title")
	private String encyclopediaTitle;
	
	@Column(name = "episode_number")
	private String episodeNumber;

	private String institution;
	private String issue;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "item_type")
	private ReferenceEntry.Type itemType;

	@Column(name = "manuscript_type")
	private String manuscriptType;
		
	private String pages;
	private String place;

	@Column(name = "proceedings_title")
	private String proceedingsTitle;

	@Column(name = "publication_title")
	private String publicationTitle;
	
	private String publisher;
	private String series;

	@Column(name = "series_number")
	private String seriesNumber;

	@Column(name = "series_title")
	private String seriesTitle;

	@Column(name = "short_title")
	private String shortTitle;

	private String studio;
	private String title;
	private String type;
	private String university;
	private String url;
	private Integer version;
	private String volume;
	
	@Column(name = "zotero_group_id")
	private Integer zoteroGroupId;
	
	@Column(name = "zotero_collection_key")
	private String zoteroCollectionKey;
	
	@Column(name = "zotero_key")
	private String zoteroKey;	

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "entry")
	private List<ReferenceCreator> referenceCreators = new ArrayList<ReferenceCreator>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "entry")
	// we do a subselect here so getting the tags runs as a separate query, thus avoiding a cartesian join that results in too many rows when there are multiple tags or authors
	@Fetch(FetchMode.SUBSELECT)  
	private List<ReferenceTag> referenceTags = new ArrayList<ReferenceTag>();

	public ReferenceEntry() {
		super();
	}

	public ReferenceEntry(ZoteroReferenceItem item, Integer zoteroGroupId, String zoteroCollectionKey) {
		BeanUtils.copyProperties(item, this);
		setItemType(Type.forName(item.getItemType()));
		this.zoteroGroupId = zoteroGroupId;
		this.zoteroCollectionKey = zoteroCollectionKey;
		this.zoteroKey = item.getKey();
		
		if (!CollectionUtils.isEmpty(item.getCreators())) {
			List<ReferenceCreator> creators = new ArrayList<ReferenceCreator>();

			for (org.vhmml.dto.zotero.ZoteroReferenceCreator creator : item.getCreators()) {
				creators.add(new ReferenceCreator(this, creator));
			}

			this.referenceCreators = creators;
		}
		
		if (!CollectionUtils.isEmpty(item.getTags())) {
			List<ReferenceTag> tags = new ArrayList<ReferenceTag>();

			for (org.vhmml.dto.zotero.ZoteroReferenceTag tag : item.getTags()) {
				tags.add(new ReferenceTag(this, tag));
			}

			this.referenceTags = tags;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
	public String getDisplayDate() {
		if(date == null) {
			return "";
		} else {
			return date;
		}
	}

	public String getDictionaryTitle() {
		return dictionaryTitle;
	}

	public void setDictionaryTitle(String dictionaryTitle) {
		this.dictionaryTitle = dictionaryTitle;
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

	public List<ReferenceCreator> getReferenceCreators() {
		return referenceCreators;
	}

	public void setReferenceCreators(List<ReferenceCreator> referenceCreators) {
		this.referenceCreators = referenceCreators;
	}

	public String getAuthor() {
		return getCreatorString(Creator.Type.AUTHOR);
	}
	
	public String getBookAuthor() {
		return getCreatorString(Creator.Type.BOOKAUTHOR);
	}
	
	public String getContributor() {
		return getCreatorString(Creator.Type.CONTRIBUTOR);
	}
	
	public String getDirector() {
		return getCreatorString(Creator.Type.DIRECTOR);
	}
	
	public String getEditor() {
		return getCreatorString(Creator.Type.EDITOR);
	}
	
	public String getTranslator() {
		return getCreatorString(Creator.Type.TRANSLATOR);
	}
	
	public String getPodcaster() {
		return getCreatorString(Creator.Type.PODCASTER);
	}
	
	public Integer getZoteroGroupId() {
		return zoteroGroupId;
	}

	public void setZoteroGroupId(Integer zoteroGroupId) {
		this.zoteroGroupId = zoteroGroupId;
	}

	public String getZoteroCollectionKey() {
		return zoteroCollectionKey;
	}

	public void setZoteroCollectionKey(String zoteroCollectionKey) {
		this.zoteroCollectionKey = zoteroCollectionKey;
	}
	
	public String getZoteroKey() {
		return zoteroKey;
	}

	public void setZoteroKey(String zoteroKey) {
		this.zoteroKey = zoteroKey;
	}
	
	public List<ReferenceTag> getReferenceTags() {
		return referenceTags;
	}

	public void setReferenceTags(List<ReferenceTag> referenceTags) {
		this.referenceTags = referenceTags;
	}
	
	public String getTags() {
		StringBuilder tags = new StringBuilder();
		
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(referenceTags)) {
			for(int i = 0; i < referenceTags.size(); i++) {
				if(i > 0) {
					tags.append(", ");
				}
				tags.append(referenceTags.get(i).getTag().getTag().toUpperCase());
			}
		}
		
		return tags.toString();
	}

	private String getCreatorString(Creator.Type creatorType) {
		StringBuilder creatorString = new StringBuilder();

		if (!CollectionUtils.isEmpty(referenceCreators)) {

			for (int i = 0; i < referenceCreators.size(); i++) {
				ReferenceCreator libCreator = referenceCreators.get(i);

				if (org.vhmml.entity.Creator.Type.forName(libCreator.getCreatorType()) == creatorType) {
					if (creatorString.length() > 0) {
						creatorString.append("; ");
					}
					Creator creator = libCreator.getCreator();
					String name = creator.getName();

					if (StringUtils.isNotEmpty(creator.getLastName())) {
						name = StringUtils.isNotEmpty(creator.getFirstName()) ? creator.getLastName() + ", " + creator.getFirstName() : creator.getLastName();
					}

					creatorString.append(name);
				}
			}
		}

		return creatorString.toString();
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
		ReferenceEntry other = (ReferenceEntry) obj;
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
	
	public List<DisplayField> getDisplayFields() {		
		return displayFieldsByEntryType.get(this.itemType);		
	}	
	
	private static void initFieldsByTypeMap() {
		for(Type entryType : Type.values()) {
			List<DisplayField> displayFields = new ArrayList<DisplayField>();
			
			switch(entryType) {
				case ATTACHMENT:
				case NOTE:
					break;
				case BLOG_POST:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.BLOG_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.DATE);					
					break;
				case BOOK:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.EDITORS);
					displayFields.add(DisplayField.CONTRIBUTORS);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.SERIES_NUMBER);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.EDITION);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.PUBLISHER);
					displayFields.add(DisplayField.DATE);					
					break;
				case BOOK_SECTION:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.EDITORS);
					displayFields.add(DisplayField.BOOK_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.SERIES_NUMBER);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.EDITION);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.PUBLISHER);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case CONFERENCE_PAPER:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.PROCEEDINGS_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.CONFERENCE);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.PUBLISHER);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case DICTIONARY_ENTRY:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.DICTIONARY_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.SERIES_NUMBER);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.EDITION);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.PUBLISHER);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case ENCYCLOPEDIA_ARTICLE:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.ENCYCLOPEDIA_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.SERIES_NUMBER);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.EDITION);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.PUBLISHER);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case JOURNAL_ARTICLE:
					displayFields.add(DisplayField.PUBLICATION);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.EDITORS);
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.SERIES_TITLE);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.ISSUE);
					displayFields.add(DisplayField.SERIES);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case MAGAZINE_ARTICLE:
					displayFields.add(DisplayField.PUBLICATION);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.ISSUE);
					displayFields.add(DisplayField.DATE);
					displayFields.add(DisplayField.PAGES);
					break;
				case MANUSCRIPT:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.TYPE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.DATE);
					break;
				case PODCAST:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.PODCASTER);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.EPISODE_NUMBER);
					displayFields.add(DisplayField.SERIES_TITLE);
					break;
				case REPORT:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.INSTITUTION);
					break;
				case THESIS:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.AUTHORS);
					//displayFields.add(DisplayField.TYPE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.UNIVERSITY);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.DATE);			
					break;
				case VIDEO_RECORDING:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.SERIES_TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.STUDIO);
					displayFields.add(DisplayField.DIRECTORS);
					displayFields.add(DisplayField.VOLUME);
					displayFields.add(DisplayField.PLACE);
					displayFields.add(DisplayField.DATE);
					break;
				case WEBPAGE:
					displayFields.add(DisplayField.TITLE);
					displayFields.add(DisplayField.SHORT_TITLE);
					displayFields.add(DisplayField.AUTHORS);
					displayFields.add(DisplayField.CONTRIBUTORS);
					displayFields.add(DisplayField.DATE);
					break;
			}
			
			displayFields.add(DisplayField.TAGS);
			displayFields.add(DisplayField.URL);
			displayFieldsByEntryType.put(entryType, displayFields);
		}	
	}

	public static Map<Type, List<DisplayField>> getDisplayFieldsByEntryType() {
		return displayFieldsByEntryType;
	}

	public static void setDisplayFieldsByEntryType(Map<Type, List<DisplayField>> displayFieldsByEntryType) {
		ReferenceEntry.displayFieldsByEntryType = displayFieldsByEntryType;
	}
}
