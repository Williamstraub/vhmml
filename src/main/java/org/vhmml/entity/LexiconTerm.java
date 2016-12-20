package org.vhmml.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.jsoup.Jsoup;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "lexicon_terms")
@Where(clause="removed = 0")
public class LexiconTerm implements Serializable, Identifiable { 
	
	private static final long serialVersionUID = 5024407338160316879L;	
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String term;
	
	@Column(name = "short_definition")
	private String shortDefinition;
	
	@Column(name = "full_definition")
	private String fullDefinition;
	
	private String bibliography;
	private String images;
	private String notes;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "lexicon_related_terms", joinColumns = {@JoinColumn(name = "term_id")}, inverseJoinColumns = {@JoinColumn(name = "related_term_id")})
	@Where(clause="removed = 0")
	@Fetch(FetchMode.SELECT) 
	private List<LexiconTerm> relatedTerms = new ArrayList<LexiconTerm>();
	
	private String classification;
	
	@Column(name = "french_terms")
	private String frenchTerms;
	
	@Column(name = "latin_terms")
	private String latinTerms;
	
	@Column(name = "german_terms")
	private String germanTerms;
	
	@Column(name = "italian_terms")
	private String italianTerms;
	
	@Column(name = "spanish_terms")
	private String spanishTerms;
	
	@Column(name = "arabic_terms")
	private String arabicTerms;
	
	@Column(name = "armenian_terms")
	private String armenianTerms;
	
	@Column(name = "syriac_terms")
	private String syriacTerms;
	
	@Column(name = "amharic_terms")
	private String amharicTerms;
	
	@Column(name = "portuguese_terms")
	private String portugueseTerms;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "lexicon_contributors", joinColumns = @JoinColumn(name = "lexicon_id"), inverseJoinColumns = @JoinColumn(name = "contributor_id"))	
	private List<Contributor> contributors = new ArrayList<Contributor>();
	
	@Column(name = "removed")
	private boolean removed;

	public LexiconTerm() {
		super();
	}
	
	public LexiconTerm(String term) {
		this.term = term;
	}
	
	public LexiconTerm(Long id, String term) {
		this.id = id;
		this.term = term;		
	}

	public Long getId() {
		return id;
	}	

	public void setId(Long id) {
		this.id = id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		// CKEditor sends up HTML, we want to strip the tags from the term
		this.term = Jsoup.parse(term).text();
	}

	public String getShortDefinition() {
		return shortDefinition;
	}

	public void setShortDefinition(String shortDefinition) {
		this.shortDefinition = shortDefinition;
	}

	public String getFullDefinition() {
		return fullDefinition;
	}

	public void setFullDefinition(String fullDefinition) {
		this.fullDefinition = fullDefinition;
	}

	public String getBibliography() {
		return bibliography;
	}

	public void setBibliography(String bibliography) {
		this.bibliography = bibliography;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<LexiconTerm> getRelatedTerms() {
		return relatedTerms;
	}
	
	public List<LexiconTerm> getRelatedTerms(boolean includeRemoved) {
		List<LexiconTerm> terms = relatedTerms;
		
		if(!includeRemoved && relatedTerms != null) {
			terms = new ArrayList<LexiconTerm>();
			
			for(LexiconTerm term : relatedTerms) {
				if(!term.isRemoved()) {
					terms.add(term);
				}
			}
		}
		
		return terms;
	}

	public void setRelatedTerms(List<LexiconTerm> relatedTerms) {
		this.relatedTerms = relatedTerms;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getFrenchTerms() {
		return frenchTerms;
	}

	public void setFrenchTerms(String frenchTerms) {
		this.frenchTerms = frenchTerms;
	}

	public String getLatinTerms() {
		return latinTerms;
	}

	public void setLatinTerms(String latinTerms) {
		this.latinTerms = latinTerms;
	}

	public String getGermanTerms() {
		return germanTerms;
	}

	public void setGermanTerms(String germanTerms) {
		this.germanTerms = germanTerms;
	}

	public String getItalianTerms() {
		return italianTerms;
	}

	public void setItalianTerms(String italianTerms) {
		this.italianTerms = italianTerms;
	}

	public String getSpanishTerms() {
		return spanishTerms;
	}

	public void setSpanishTerms(String spanishTerms) {
		this.spanishTerms = spanishTerms;
	}

	public String getArabicTerms() {
		return arabicTerms;
	}

	public void setArabicTerms(String arabicTerms) {
		this.arabicTerms = arabicTerms;
	}

	public String getArmenianTerms() {
		return armenianTerms;
	}

	public void setArmenianTerms(String armenianTerms) {
		this.armenianTerms = armenianTerms;
	}

	public String getSyriacTerms() {
		return syriacTerms;
	}

	public void setSyriacTerms(String syriacTerms) {
		this.syriacTerms = syriacTerms;
	}

	public String getAmharicTerms() {
		return amharicTerms;
	}

	public void setAmharicTerms(String amharicTerms) {
		this.amharicTerms = amharicTerms;
	}
	
	public String getPortugueseTerms() {
		return portugueseTerms;
	}

	public void setPortugueseTerms(String portugueseTerms) {
		this.portugueseTerms = portugueseTerms;
	}

	public List<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(List<Contributor> contributors) {
		this.contributors = contributors;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
	public String getRelatedTermsString() {
		return getListAsString(getRelatedTerms(false), "term");		
	}
	
	public String getContributorsString() {
		return getListAsString(contributors, "name");		
	}
	
	private String getListAsString(List<?> objects, String attributeName) {
		StringBuilder listAsString = new StringBuilder();
		
		if(!CollectionUtils.isEmpty(objects)) {
			for(int i = 0; i < objects.size(); i++) {
				Object object = objects.get(i);
				
				if(i > 0) {
					listAsString.append("|");
				}
				
				try {
					listAsString.append(BeanUtils.getProperty(object, attributeName));
				} catch (Exception e) {
					throw new RuntimeException("Unable to get value for attribute named " + attributeName + " on object of type " + object.getClass(), e);
				}
			}			
		}
		
		return listAsString.toString();
	}
	
	public Map<String, String> getOtherLanguages() {
		Map<String, String> otherLanguages = new TreeMap<String, String>();
		
		addNotEmpty(otherLanguages, "Arabic", arabicTerms);
		addNotEmpty(otherLanguages, "Armenian", armenianTerms);
		addNotEmpty(otherLanguages, "French", frenchTerms);
		addNotEmpty(otherLanguages, "G&#1241;&#x02bf;&#1241;z/Amharic", amharicTerms);
		addNotEmpty(otherLanguages, "German", germanTerms);
		addNotEmpty(otherLanguages, "Italian", italianTerms);
		addNotEmpty(otherLanguages, "Latin",  latinTerms);		
		addNotEmpty(otherLanguages, "Portuguese",  portugueseTerms);		
		addNotEmpty(otherLanguages, "Spanish", spanishTerms);		
		addNotEmpty(otherLanguages, "Syriac", syriacTerms);
		
		
		return otherLanguages;
	}
	
	private void addNotEmpty(Map<String, String> map, String name, String value) {
		if(StringUtils.isNotEmpty(value)) {
			map.put(name, value);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		LexiconTerm other = (LexiconTerm) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
