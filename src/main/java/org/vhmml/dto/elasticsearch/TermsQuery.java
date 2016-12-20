package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class TermsQuery extends ElasticSearchQuery {

	private String fieldName;
	private String[] fieldValues;
	
	public TermsQuery(String fieldName, String[] fieldValues) {
		this.fieldName = fieldName;
		this.fieldValues = fieldValues;
	}

	@Override
	public QueryBuilder getQuery() {
		return QueryBuilders.termsQuery(fieldName, fieldValues);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldValues(String[] fieldValues) {
		this.fieldValues = fieldValues;
	}
}
