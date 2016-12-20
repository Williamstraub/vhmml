package org.vhmml.dto;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;

public abstract class SearchResult {

	private Integer pageNumber;
	private Integer pageSize;
	private Integer offset;
	private Long totalElements = 0L;
	private Map<String, String> fieldDisplayNames = new HashMap<>();
	
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}	
	
	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getTotalPages() {
		Integer totalPages = 0;
		
		if(totalElements != null && pageSize != null) {
			totalPages = new Long(totalElements % pageSize == 0 ? totalElements/pageSize : totalElements/pageSize + 1).intValue();
		}
		
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		// total pages is calcuated, this method is here to make jackson happy
	}
	
	public void setPagingInformation(SearchResponse searchResponse, Pageable pageable) {
		if(pageable != null) {
			if(pageable.getOffset() > 0) {
				setPageNumber(pageable.getOffset()/pageable.getPageSize());
			}
			
			setOffset(pageable.getOffset());
			setPageSize(pageable.getPageSize());
		}
		
		if(searchResponse != null) {
			setTotalElements(searchResponse.getHits().getTotalHits());
		}		
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Map<String, String> getFieldDisplayNames() {
		return fieldDisplayNames;
	}

	public void setFieldDisplayNames(Map<String, String> fieldDisplayNames) {
		this.fieldDisplayNames = fieldDisplayNames;
	}
}
