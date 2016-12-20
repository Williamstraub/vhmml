package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;

public abstract class ElasticSearchQuery {
	
	public enum BoolOperation {
		MUST,
		SHOULD,
		MUST_NOT
	}
	
	protected String searchText;
	// how this query will operate when wrapped in a larger boolean query
	protected BoolOperation parentBoolOperation = BoolOperation.MUST;
	protected int slop = 2;
	
	public abstract QueryBuilder getQuery();
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public BoolOperation getParentBoolOperation() {
		return parentBoolOperation;
	}

	public void setParentBoolOperation(BoolOperation parentBoolOperation) {
		this.parentBoolOperation = parentBoolOperation;
	}

	public int getSlop() {
		return slop;
	}

	public void setSlop(int slop) {
		this.slop = slop;
	}
}
