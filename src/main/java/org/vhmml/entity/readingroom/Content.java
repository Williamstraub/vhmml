package org.vhmml.entity.readingroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;
import org.vhmml.dto.ReadingRoomContentDTO;
import org.vhmml.entity.Identifiable;
import org.vhmml.entity.readingroom.Contributor.Type;
import org.vhmml.util.FormBeanUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_content")
@Inheritance(strategy=InheritanceType.JOINED)
public class Content implements Identifiable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "reading_room_object_part_id")
    private ReadingRoomObjectPart parentPart;
	
	@Column(name = "item_number")
	private Integer itemNumber;	
	
	@Column(name = "item_location")
	private String itemLocation;	
	
	@Column(name = "title_in_ms")
	private String titleInMs;
	
	@Column(name = "provisional_title")
	private String provisionalTitle;
	
	@Column(name = "title_ns")
	private String titleNs;	
	
	@Column(name = "running_title")
	private String runningTitle;	
	
	@Column(name = "item_condition")
	private String itemCondition;
	
	private String rubric;	
	
	private String incipit;	
	
	private String explicit;
	
	@Column(name = "item_notes")
	private String itemNotes;
	
	private boolean revisit;
	private String acknowledgments;
	
	@Column(name = "colophon_text")
	private String colophonText;
	
	@Column(name = "text_bibliography")
	private String textBibliography;		
	
	@Column(name = "cataloger_tags")
	private String catalogerTags;	
	
	@Column(name = "pagination_statement")
	private String paginationStatement;	
	
	@Column(name = "contents_detail")
	private String contentsDetail;
	
	private String decoration;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reading_room_alternate_titles", joinColumns = {@JoinColumn(name="content_id")})
	@Column(name = "title")
	private List<String> alternateTitles;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uniform_title_id")
	private UniformTitle uniformTitle;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "reading_room_content_languages", joinColumns = @JoinColumn(name = "reading_room_content_id"), inverseJoinColumns = @JoinColumn(name = "language_id"))
	private List<Language> languages;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentContent", orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)	
	private List<Image> images = new ArrayList<Image>();
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "content")
	@Fetch(FetchMode.SUBSELECT)	
	private List<ContentContributor> contentContributors = new ArrayList<ContentContributor>();		

	public Content() {
		super();
	}
	
	public Content(Long id) {
		this.id = id;
	}
	
	public Content(ReadingRoomContentDTO contentDto) {
		BeanUtils.copyProperties(contentDto, this);
		
		UniformTitle uniformTitle = contentDto.getUniformTitle();
		
		// spring form will send up empty uniform title with all null fields if user didn't enter anything 
		if(uniformTitle != null && FormBeanUtil.isFieldEmpty(uniformTitle)) {
			this.setUniformTitle(null);
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitleInMs() {
		return titleInMs;
	}

	public void setTitleInMs(String titleInMs) {
		this.titleInMs = titleInMs;
	}

	public String getProvisionalTitle() {
		return provisionalTitle;
	}

	public void setProvisionalTitle(String provisionalTitle) {
		this.provisionalTitle = provisionalTitle;
	}

	public String getTitleNs() {
		return titleNs;
	}

	public void setTitleNs(String titleNs) {
		this.titleNs = titleNs;
	}

	public String getItemCondition() {
		return itemCondition;
	}

	public void setItemCondition(String itemCondition) {
		this.itemCondition = itemCondition;
	}

	public String getRubric() {
		return rubric;
	}

	public void setRubric(String rubric) {
		this.rubric = rubric;
	}

	public String getIncipit() {
		return incipit;
	}

	public void setIncipit(String incipit) {
		this.incipit = incipit;
	}

	public String getExplicit() {
		return explicit;
	}

	public void setExplicit(String explicit) {
		this.explicit = explicit;
	}

	public String getItemNotes() {
		return itemNotes;
	}

	public void setItemNotes(String itemNotes) {
		this.itemNotes = itemNotes;
	}

	public boolean isRevisit() {
		return revisit;
	}

	public void setRevisit(boolean revisit) {
		this.revisit = revisit;
	}

	public String getAcknowledgments() {
		return acknowledgments;
	}

	public void setAcknowledgments(String acknowledgments) {
		this.acknowledgments = acknowledgments;
	}

	public String getColophonText() {
		return colophonText;
	}

	public void setColophonText(String colophonText) {
		this.colophonText = colophonText;
	}

	public UniformTitle getUniformTitle() {
		return uniformTitle;
	}

	public void setUniformTitle(UniformTitle uniformTitle) {
		this.uniformTitle = uniformTitle;
	}

	public String getTextBibliography() {
		return textBibliography;
	}

	public void setTextBibliography(String textBibliography) {
		this.textBibliography = textBibliography;
	}

	public List<Image> getImages() {
		return images;
	}	

	public void setImages(List<Image> images) {
		if(images != null) {
			this.images.clear();
			this.images.addAll(images);
			
			if(CollectionUtils.isNotEmpty(images)) {
				for(Image image : images) {
					if(image != null) {
						image.setParentContent(this);
					}
				}
			}
		}		
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	public String getCatalogerTags() {
		return catalogerTags;
	}

	public void setCatalogerTags(String catalogerTags) {
		this.catalogerTags = catalogerTags;
	}
	
	public List<String> getAlternateTitles() {
		return alternateTitles;
	}

	public void setAlternateTitles(List<String> alternateTitles) {
		this.alternateTitles = alternateTitles;
	}

	public List<ContentContributor> getContentContributors() {
		return contentContributors;
	}

	public void setContentContributors(List<ContentContributor> contentContributors) {
		this.contentContributors = contentContributors;
		
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for(ContentContributor contributor : contentContributors) {
				contributor.setContent(this);
			}
		}
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public ReadingRoomObjectPart getParentPart() {
		return parentPart;
	}

	public void setParentPart(ReadingRoomObjectPart parentPart) {
		this.parentPart = parentPart;
	}

	public String getItemLocation() {
		return itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}
	
	public List<ContentContributor> getContributorsByType(Contributor.Type type) {
		List<ContentContributor> contributorsOfType = new ArrayList<>();

		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for(ContentContributor contentContributor : contentContributors) {				
				if(contentContributor.getType() == type) {
					contributorsOfType.add(contentContributor);
				}
			}
		}			
		
		return contributorsOfType;
	}
	
	public List<ContentContributor> getContributorsExcludingTypes(Contributor.Type... excludeTypes) {
		List<ContentContributor> filteredContributors = new ArrayList<>();		
		
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for(ContentContributor contributor : contentContributors) {
				Contributor.Type type = contributor.getType();
				boolean exclude = false;
				
				for(Contributor.Type excludeType : excludeTypes) {
					if(type == excludeType) {
						exclude = true;
						break;
					}
				}
				
				if(!exclude) {
					filteredContributors.add(contributor);
				}				
			}
		}
		
		return filteredContributors;
	}
	
	public void addContentContributors(List<ContentContributor> contentContributors, Type contributorType) {			
		
		for (ContentContributor contentContributor : contentContributors) {
			contentContributor.setContent(this);
			contentContributor.setType(contributorType);
			this.contentContributors.add(contentContributor);
		}
	}
	
	public void addContentContributors(List<ContentContributor> contentContributors) {			
		
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for (ContentContributor contentContributor : contentContributors) {
				contentContributor.setContent(this);
				this.contentContributors.add(contentContributor);
			}
		}		
	}

	public String getRunningTitle() {
		return runningTitle;
	}

	public void setRunningTitle(String runningTitle) {
		this.runningTitle = runningTitle;
	}	

	public String getPaginationStatement() {
		return paginationStatement;
	}

	public void setPaginationStatement(String paginationStatement) {
		this.paginationStatement = paginationStatement;
	}

	public String getContentsDetail() {
		return contentsDetail;
	}

	public void setContentsDetail(String contentsDetail) {
		this.contentsDetail = contentsDetail;
	}

	public String getDecoration() {
		return decoration;
	}

	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	
	public List<ContentContributor> getAuthors() {
		List<ContentContributor> authors = new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for(ContentContributor contrib : contentContributors) {
				if(Contributor.Type.AUTHOR == contrib.getType()) {
					authors.add(contrib);
				}
			}
		}
		
		return authors;
	}
	
	public List<ContentContributor> getAssociatedNames() {
		List<ContentContributor> associatedNames = new ArrayList<>();		
			
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			
			for(ContentContributor contentContributor : contentContributors) {
				if(Contributor.Type.AUTHOR != contentContributor.getType()) {
					associatedNames.add(contentContributor);
				}
			}				
		}
		
		return associatedNames;
	}
	
	public String getLanguageDisplay() {
		StringBuilder languageDisplay = new StringBuilder();
		
		if(CollectionUtils.isNotEmpty(languages)) {
			List<String> languageNames = new ArrayList<>();
			
			for(Language lang : languages) {
				languageNames.add(lang.getName());
			}
			
			Collections.sort(languageNames);						
			Iterator<String> iter = languageNames.iterator();
			
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
}
