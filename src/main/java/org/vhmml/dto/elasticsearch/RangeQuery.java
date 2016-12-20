package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class RangeQuery extends ElasticSearchQuery {
	
	private String field;
	private String greaterThanValue;
	private String lessThanValue;
	private boolean includeLessThanValue;
	private boolean includeGreaterThanValue;
	
	public RangeQuery(String field, String lessThanValue, String greaterThanValue) {
		this(field, lessThanValue, greaterThanValue, true, true);
	}	
	
	public RangeQuery(String field, String lessThanValue, String greaterThanValue, boolean includeLessThanValue, boolean includeGreaterThanValue) {
		this.field = field;
		this.lessThanValue = lessThanValue;
		this.greaterThanValue = greaterThanValue;
		this.includeLessThanValue = includeLessThanValue;
		this.includeGreaterThanValue = includeGreaterThanValue;
		this.parentBoolOperation = ElasticSearchQuery.BoolOperation.SHOULD;
	}	
	
	public QueryBuilder getQuery() {
		RangeQueryBuilder query = QueryBuilders.rangeQuery(field);
		
		if(includeLessThanValue) {
			query.lte(lessThanValue);
		} else {
			query.lt(lessThanValue);
		}
		
		if(includeGreaterThanValue) {
			query.gte(greaterThanValue);
		} else {
			query.gt(greaterThanValue);
		}
		
		return query;
	}

	public String getField() {
		return field;
	}

	public String getGreaterThanValue() {
		return greaterThanValue;
	}

	public String getLessThanValue() {
		return lessThanValue;
	}

	public boolean isIncludeLessThanValue() {
		return includeLessThanValue;
	}

	public boolean isIncludeGreaterThanValue() {
		return includeGreaterThanValue;
	}
}
