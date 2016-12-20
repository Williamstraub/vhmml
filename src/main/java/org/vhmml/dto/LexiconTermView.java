package org.vhmml.dto;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.vhmml.entity.Identifiable;
import org.vhmml.entity.LexiconTerm;
import org.vhmml.util.ElasticSearchUtil;

public class LexiconTermView implements Identifiable {

	private Long id;
	private String term;
	private String shortDefinition;
	private String fullDefinition;
	private String displayDefinition;	
	private String frenchTerms;
	private String latinTerms;
	private String germanTerms;
	private String italianTerms;
	private String spanishTerms;
	private String arabicTerms;
	private String armenianTerms;
	private String syriacTerms;
	private String amharicTerms;
	private String portugueseTerms;
	private Map<String, String> highlightFields;
	
	public LexiconTermView(LexiconTerm lexiconTerm) {
		BeanUtils.copyProperties(lexiconTerm, this);
	}
	
	public LexiconTermView() {
		super();	
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
		this.term = term;
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

	public String getDisplayDefinition() {
		String definition = displayDefinition;
		
		if(displayDefinition == null) {
			if(shortDefinition != null) {
				definition = shortDefinition;
			} else if(fullDefinition != null) {
				definition = fullDefinition;
			} else {
				definition = "";				
			}
		}
		
		definition = StringUtils.replace(definition, "<p>", "");
		definition = StringUtils.replace(definition, "</p>", "");
		definition = StringUtils.replace(definition, "<div>", "");
		definition = StringUtils.replace(definition, "</div>", "");
		definition = StringUtils.replace(definition, "<br/>", "");
		definition = StringUtils.replace(definition, "<br>", "");
		
		return definition;
	}

	public void setDisplayDefinition(String displayDefinition) {
		this.displayDefinition = displayDefinition;
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
}
