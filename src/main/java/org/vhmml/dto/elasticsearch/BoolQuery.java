package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class BoolQuery extends ElasticSearchQuery {

	private BoolQueryBuilder query = QueryBuilders.boolQuery();
	
	public BoolQuery() {
		super();
	}

	@Override
	public QueryBuilder getQuery() {
		return query;
	}
	
	public void must(ElasticSearchQuery mustQuery) {
		this.query.must(mustQuery.getQuery());
	}
	
	public void should(ElasticSearchQuery shouldQuery) {
		this.query.should(shouldQuery.getQuery());
	}
}
