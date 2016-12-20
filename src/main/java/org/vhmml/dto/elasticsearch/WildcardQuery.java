package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class WildcardQuery extends MultiMatchQuery {
	
	public WildcardQuery(String searchText, String[] fields) {
		super(searchText, fields, null);		
	}

	@Override
	public QueryBuilder getQuery() {
		QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(searchText);
		
		for(String field : fields) {
			queryBuilder.field(field);
		}
		
		return queryBuilder;
	}
}
