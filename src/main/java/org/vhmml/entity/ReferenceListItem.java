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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.ReferenceEntry.Type;
import org.vhmml.util.ElasticSearchUtil;

import com.google.common.net.PercentEscaper;

@Entity
@Table(name = "reference_list")
public class ReferenceListItem implements Identifiable {
	
	@Id
	@GeneratedValue
	private Long id;	
	
	private String title;
	
	@Column(name = "short_title")
	private String shortTitle;
	
	// this is short title if it exists, regular title otherwise, if there is no short title or regular title then it's empty
	// i.e. the sql that creates the column in the reference_list view is coalesce(nullif(le.short_title, ''), le.title) display_title,
	@Column(name = "display_title")
	private String displayTitle;
	
	@Column(name = "book_title")
	private String bookTitle;
		
	@Column(name = "series_title")
	private String seriesTitle;
	
	@Column(name = "encyclopedia_title")
	private String encyclopediaTitle;	
	
	private String date;	
	private String author;
	
	@Column(name = "publication_title")
	private String publicationTitle;
	
	@Column(name = "proceedings_title")
	private String proceedingsTitle;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "item_type")
	private ReferenceEntry.Type itemType;
	
	private String place;
	private String university;
	private String publisher;
	private String series;
	private String url;	
	private String edition;
	private String issue;
	private String volume;
	private String version;
	private String pages;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reference_creators", joinColumns = @JoinColumn(name = "reference_entry_id"), inverseJoinColumns = @JoinColumn(name = "creator_id"))
	private List<Creator> creators;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reference_tags", joinColumns = @JoinColumn(name = "reference_entry_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;
	
	@Transient
	private Map<String, String> highlightFields;	
	
	@Transient
	private PercentEscaper percentEscaper = new PercentEscaper(".-()", false);
	
	public ReferenceListItem() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getSeriesTitle() {
		return seriesTitle;
	}

	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}
	
	public String getEncyclopediaTitle() {
		return encyclopediaTitle;
	}

	public void setEncyclopediaTitle(String encyclopediaTitle) {
		this.encyclopediaTitle = encyclopediaTitle;
	}

	public String getDate() {
		return date;
	}	

	public void setDate(String date) {
		this.date = date;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}	

	public String getPublicationTitle() {
		return publicationTitle;
	}

	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
	
	public String getProceedingsTitle() {
		return proceedingsTitle;
	}

	public void setProceedingsTitle(String proceedingsTitle) {
		this.proceedingsTitle = proceedingsTitle;
	}

	public ReferenceEntry.Type getItemType() {
		return itemType;
	}

	public void setItemType(ReferenceEntry.Type itemType) {
		this.itemType = itemType;
	}
	
	public String getItemTypeDisplay() {
		String itemTypeDisplay = null;
		
		if(itemType != null) {
			itemTypeDisplay = itemType.getDisplayName();
		}
		
		return itemTypeDisplay;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
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

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Creator> getCreators() {
		return creators;
	}

	public void setCreators(List<Creator> creators) {
		this.creators = creators;
	}

	public Map<String, String> getHighlightFields() {
		return highlightFields;
	}

	public void setHighlightFields(Map<String, String> highlightFields) {
		this.highlightFields = highlightFields;
	}	
	
	public void setHighlightFieldsForDisplay(SearchHit searchHit) {
		Map<String, Object> searchHitSource = searchHit.getSource();
		Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
		
		if(MapUtils.isNotEmpty(highlightFields)) {
			Map<String, String> highlightFieldStrings = new HashMap<String, String>();
			
			for(HighlightField field : highlightFields.values()) {
				String value = ElasticSearchUtil.addEllipsesToHighlightedFieldValue(searchHitSource, field.getName(), field.getFragments()[0].string());
				highlightFieldStrings.put(field.getName(), value);
			}
			
			this.highlightFields = highlightFieldStrings;
		}
	}
	
	public Integer getHitCount() {
		Integer hitCount = 0;
		
		if(MapUtils.isNotEmpty(highlightFields)) {
			List<String> hitFields = new ArrayList<String>();
			
			for(String fieldName : highlightFields.keySet()) {
				String strippedFieldName = fieldName.replaceAll(".folded", "");
				
				if(!hitFields.contains(strippedFieldName)) {
					hitFields.add(strippedFieldName);
					hitCount++;
				}
			}
		}
		
		return hitCount;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}	
	
	public String getCoinsData() {
		String coinsData = null;
		
		if(itemType != null) {
			StringBuilder html = new StringBuilder();
			html.append("<span class='Z3988' title='");
			html.append("url_ver=Z39.88-2004&amp;");
			html.append(getCoinsAttribute("ctx_ver", "Z39.88-2004"));
			html.append(getCoinsAttribute("rfr_id", "info:sid/zotero.org:2"));
			
			switch(itemType) {
				case BOOK:
				case MANUSCRIPT:
					html.append(getBookCoinsData(itemType));			
					break;
				case BOOK_SECTION:
					html.append(getBookCoinsData(itemType));
					html.append(getCoinsAttribute("rft.genre", "bookitem"));
					html.append(getPagesCoinsData());
					break;
				case CONFERENCE_PAPER:
					html.append(getBookCoinsData(itemType));
					html.append(getCoinsAttribute("rft.genre", "proceeding"));
					html.append(getPagesCoinsData());
					break;
				case ENCYCLOPEDIA_ARTICLE:
				case JOURNAL_ARTICLE:
				case MAGAZINE_ARTICLE:
				case PODCAST:
				case VIDEO_RECORDING:
				case WEBPAGE:
					html.append(getJournalArticleCoinsData(itemType));
					html.append(getPagesCoinsData());
					break;
				case REPORT:
					html.append(getBookCoinsData(itemType));
					html.append(getCoinsAttribute("rft.genre", "report"));
					break;
				case THESIS:
					html.append(getThesisCoinsData());
					break;
				default:
					html.append(getCoinsAttribute("rft_val_fmt", "info:ofi/fmt:kev:mtx:book"));
					html.append(getCoinsAttribute("rft.title", title));
			}
					
			// there is no way to specify an editor or other contributor type in COinS so we just list them 
			// as authors so the user at least gets the info (that's what Zotero does so we're following suit)
			for(Creator creator : creators) {
				html.append(getCoinsAttribute("rft.au", creator.getName()));
			}
					
			html.append(getCoinsAttribute("rft.place", place));
			html.append(getCoinsAttribute("rft.date", date));
			coinsData = StringUtils.removeEnd(html.toString(), "&amp;") + "'></span>";
		}
		
		return coinsData; 
	}
	
	private String getBookCoinsData(ReferenceEntry.Type bookType) {
		StringBuilder html = new StringBuilder();
		String bTitle = bookType == Type.CONFERENCE_PAPER ? proceedingsTitle : bookTitle;
		bTitle = StringUtils.isEmpty(bTitle) ? title : bTitle;
		html.append(getCoinsAttribute("rft_val_fmt", "info:ofi/fmt:kev:mtx:book"));						
		html.append(getCoinsAttribute("rft.atitle", title));
		html.append(getCoinsAttribute("rft.btitle", bTitle));
		html.append(getCoinsAttribute("rft.edition", edition));
		html.append(getCoinsAttribute("rft.publisher", publisher));
		html.append(getCoinsAttribute("rft.series", series));		
		
		return html.toString();
	}
	
	private String getJournalArticleCoinsData(ReferenceEntry.Type bookType) {
		StringBuilder html = new StringBuilder();
		String genra = bookType == Type.JOURNAL_ARTICLE ? "article" : "unknown";
		html.append(getCoinsAttribute("rft_val_fmt", "info:ofi/fmt:kev:mtx:journal"));
		html.append(getCoinsAttribute("rft.genre", genra));
		html.append(getCoinsAttribute("rft.atitle", title));
		html.append(getCoinsAttribute("rft.jtitle", publicationTitle));
		html.append(getCoinsAttribute("rft.volume", volume));
		html.append(getCoinsAttribute("rft.issue", issue));
		
		return html.toString();
	}
	
	/*Didn't work – William trying to fix jira1289
	private String getEncyclopediaCoinsData() {
		StringBuilder html = new StringBuilder();
		html.append(getCoinsAttribute("rft.title", title));
		return html.toString();
	}
	*/
	
	private String getThesisCoinsData() {
		StringBuilder html = new StringBuilder();
		html.append(getCoinsAttribute("rft_val_fmt", "info:ofi/fmt:kev:mtx:dissertation"));
		html.append(getCoinsAttribute("rft.title", title));
		html.append(getCoinsAttribute("rft.inst", university));
		
		return html.toString();

	}
	
	private String getPagesCoinsData() {
		StringBuilder html = new StringBuilder();
		
		html.append(getCoinsAttribute("rft.pages", pages));
		html.append(getCoinsAttribute("rft.spage", StringUtils.substringBefore(pages, "-")));
		html.append(getCoinsAttribute("rft.epage", StringUtils.substringAfterLast(pages, "-")));
		
		return html.toString();
	}
	
	private String getCoinsAttribute(String coinsAttName, String value) {
		String coinsAtt = "";
		
		if(StringUtils.isNotEmpty(value)) {
			coinsAtt = coinsAttName + "=" + percentEscaper.escape(value) + "&amp;";
		}
		
		return coinsAtt;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
}
