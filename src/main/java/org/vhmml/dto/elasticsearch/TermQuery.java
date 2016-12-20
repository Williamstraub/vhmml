package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class TermQuery extends ElasticSearchQuery {

	private String fieldName;
	private String comparisonValue;
	
	public TermQuery(String fieldName, String comparisonValue) {
		this.fieldName = fieldName;
		this.comparisonValue = comparisonValue;
	}

	@Override
	public QueryBuilder getQuery() {
		return QueryBuilders.termQuery(fieldName, comparisonValue);
	}

}
