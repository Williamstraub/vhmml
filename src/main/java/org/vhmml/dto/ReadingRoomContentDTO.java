package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.Contributor.Type;
import org.vhmml.entity.readingroom.Image;
import org.vhmml.entity.readingroom.Language;
import org.vhmml.entity.readingroom.UniformTitle;

public class ReadingRoomContentDTO {

	private Long id;
	private Integer itemNumber;
	private String itemLocation;
	private String titleInMs;	
	private String provisionalTitle;	
	private String titleNs;		
	private String runningTitle;	
	private String docket;
	private String itemCondition;
	private String rubric;
	private String incipitObsolete;
	private String incipit;
	private String explicitObsolete;
	private String explicit;
	private String itemNotes;
	private boolean revisit;
	private String acknowledgments;
	private String music;
	private String reviser;
	private String colophonText;		
	private String textBibliography;
	private String catalogerTags;		
	private String paginationStatement;			
	private String contentsDetail;
	private String decoration;
	private List<String> alternateTitles;
	private UniformTitle uniformTitle;
	private List<Long> languageIds;
	private List<ContentContributor> authors;
	private List<ContentContributor> associatedNames;
	private List<Image> images;

	public ReadingRoomContentDTO() {
		super();
	}
	
	public ReadingRoomContentDTO(Content content) {
		BeanUtils.copyProperties(content, this);
		
		if(CollectionUtils.isNotEmpty(content.getLanguages())) {
			List<Long> languageIds = new ArrayList<Long>();
			
			for(Language language : content.getLanguages()) {
				languageIds.add(language.getId());
			}
			
			this.languageIds = languageIds;
		}
		
		// all contributors are part of the same list in the database but the user enters them by type
		this.authors = content.getContributorsByType(Type.AUTHOR);
		
		if(CollectionUtils.isNotEmpty(content.getContentContributors())) {
			List<ContentContributor> otherAssociatedNames = new ArrayList<ContentContributor>();
			
			for(ContentContributor contributor : content.getContentContributors()) {
				if(Type.AUTHOR != contributor.getType()) {
					otherAssociatedNames.add(contributor);
				}
			}
			
			this.setAssociatedNames(otherAssociatedNames);
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}
	
	public String getItemLocation() {
		return itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
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
	
	public String getRunningTitle() {
		return runningTitle;
	}

	public void setRunningTitle(String runningTitle) {
		this.runningTitle = runningTitle;
	}

	public String getDocket() {
		return docket;
	}

	public void setDocket(String docket) {
		this.docket = docket;
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

	public String getIncipitObsolete() {
		return incipitObsolete;
	}

	public void setIncipitObsolete(String incipitObsolete) {
		this.incipitObsolete = incipitObsolete;
	}

	public String getIncipit() {
		return incipit;
	}

	public void setIncipit(String incipit) {
		this.incipit = incipit;
	}

	public String getExplicitObsolete() {
		return explicitObsolete;
	}

	public void setExplicitObsolete(String explicitObsolete) {
		this.explicitObsolete = explicitObsolete;
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

	public String getMusic() {
		return music;
	}

	public void setMusic(String music) {
		this.music = music;
	}

	public String getReviser() {
		return reviser;
	}

	public void setReviser(String reviser) {
		this.reviser = reviser;
	}

	public String getColophonText() {
		return colophonText;
	}

	public void setColophonText(String colophonText) {
		this.colophonText = colophonText;
	}	

	public String getTextBibliography() {
		return textBibliography;
	}

	public void setTextBibliography(String textBibliography) {
		this.textBibliography = textBibliography;
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
	
	public UniformTitle getUniformTitle() {
		return uniformTitle;
	}

	public void setUniformTitle(UniformTitle uniformTitle) {
		this.uniformTitle = uniformTitle;
	}

	public List<Long> getLanguageIds() {
		return languageIds;
	}

	public void setLanguageIds(List<Long> languageIds) {
		this.languageIds = languageIds;
	}

	public List<ContentContributor> getAuthors() {
		return authors;
	}

	public void setAuthors(List<ContentContributor> authors) {
		this.authors = authors;
	}

	public List<ContentContributor> getAssociatedNames() {
		return associatedNames;
	}

	public void setAssociatedNames(List<ContentContributor> associatedNames) {
		this.associatedNames = associatedNames;
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

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
