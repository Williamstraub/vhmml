package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class MatchQuery extends ElasticSearchQuery {

	private String field;
	private Object value;
	
	public MatchQuery(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	@Override
	public QueryBuilder getQuery() {
		return QueryBuilders.matchQuery(field, value);
	}
}
