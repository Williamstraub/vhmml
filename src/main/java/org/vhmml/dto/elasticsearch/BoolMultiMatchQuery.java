package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * The class creates a boolean query that contains a nested multi-match query for each
 * value in it's <code>values</code> array. This is useful for searching a list of fields
 * for a list values and ensuring search hits contain a certain number of the values.
 * For example, if a user searchs for documents containing one of several languages we
 * would want documents containing any of the selected languages to be considered a match. 
 * 
 * The following example shows a query that searches the languages and languages.folded 
 * fields for the values "Church Slavonic" and "Anglo-Saxon", returned any documents that
 * contain at least one of those values.
 * 
 * <code>
 * 	"bool": {
	  "minimum_number_should_match": 1, 
	  "should": [
	    {
	      "multi_match" : {
	        "query" : "Church Slavonic",
	        "fields" : [ "languages", "languages.folded"],
	        "type" : "phrase",
	        "operator": "and"
	      }
	    },
	     {
	      "multi_match" : {
	        "query" : "Anglo-Saxon",
	        "fields" : [ "languages", "languages.folded"],
	        "type" : "phrase",
	        "operator": "and"
	      }
	    }
	  ]
	}
 * </code>
 *
 */
public class BoolMultiMatchQuery extends MultiMatchQuery {

	private String[] values;
	private Operator operator;
	private BoolOperation boolOperation;
	
	public BoolMultiMatchQuery(String[] fields, String[] values, MultiMatchQueryBuilder.Type multiMatchType, Operator operator) {		
		super(null, fields, multiMatchType);
		this.values = values;
		this.operator = operator;
	}
	
	public BoolMultiMatchQuery(String[] fields, String[] values, MultiMatchQueryBuilder.Type multiMatchType, Operator operator, BoolOperation boolOperation) {		
		super(null, fields, multiMatchType);
		this.values = values;
		this.operator = operator;
		this.boolOperation = boolOperation;
	}	

	@Override
	public QueryBuilder getQuery() {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.minimumNumberShouldMatch(1);
		
		for(String value : values) {
			MultiMatchQueryBuilder query = QueryBuilders.multiMatchQuery(value, fields)
				.type(multiMatchType)
				.operator(operator);
			BoolOperation boolOperation = this.boolOperation != null ? this.boolOperation : BoolOperation.SHOULD;
			
			switch(boolOperation) {
				case MUST:
					boolQuery.must(query);
					break;
				case MUST_NOT:
					boolQuery.mustNot(query);
					break;
				case SHOULD:
					boolQuery.should(query);
					break;
			}									
		}	
		
		return boolQuery;
	}

}
