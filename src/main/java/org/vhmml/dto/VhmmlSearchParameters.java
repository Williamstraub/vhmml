package org.vhmml.dto;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

/**
 * This wrapper class is required for Spring MVC to correctly map the JSON search parameters map
 * using @ModelAttribute.
 *
 */
public class VhmmlSearchParameters {

	private String searchType;
	private Pageable pagingData;
	Map<String, String> searchTerms;	

	public Map<String, String> getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(Map<String, String> searchTerms) {
		this.searchTerms = searchTerms;
	}
	
	public boolean contains(String term, String value) {
		boolean containsValue = false;
		
		if(MapUtils.isNotEmpty(searchTerms)) {
			String savedValue = searchTerms.get(term);
			
			if(StringUtils.isNotEmpty(savedValue) && savedValue.equals(value)) {
				containsValue = true;				
			}
		}
		
		return containsValue;		
	}
	
	public boolean listTermContains(String term, String value) {
		boolean containsValue = false;
		
		if(MapUtils.isNotEmpty(searchTerms)) {
			String listValue = searchTerms.get(term);
			
			if(StringUtils.isNotEmpty(listValue)) {
				String[] values = listValue.split("</search-term>");
				
				for(String nextValue : values) {
					if(nextValue.equals(value)) {
						containsValue = true;
						break;
					}
				}
			}
		}
		
		return containsValue;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public Pageable getPagingData() {
		return pagingData;
	}

	public void setPagingData(Pageable pagingData) {
		this.pagingData = pagingData;
	}
}
