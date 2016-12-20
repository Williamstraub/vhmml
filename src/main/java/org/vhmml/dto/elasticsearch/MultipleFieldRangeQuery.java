package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * 
 * This class is used to create a query that searches across multiple fields to see if any of the fields
 * contains a numeric value that falls within a specified range. For example, the following is the query 
 * that would be assembled by this class when searching for records with a beginDate and endDate that fall
 * between 800 and 900.
 * 
 * <code>
 *  "bool" : {
 *    "should" : [{
 *      "range" : {
 *        "beginDate" : {
 *          "from" : "800",
 *          "to" : "900",
 *          "include_lower" : true,
 *          "include_upper" : true
 *        }
 *      }
 *    },
 *    {
 *      "range" : {
 *        "endDate" : {
 *          "from" : "800",
 *          "to" : "900",
 *          "include_lower" : true,
 *          "include_upper" : true
 *        }
 *      }
 *    }]
 *  }
 * </code>
 *
 */
public class MultipleFieldRangeQuery extends ElasticSearchQuery {

	private String upperBound;
	private String lowerBound;
	String[] fields;
	
	public MultipleFieldRangeQuery(String upperBound, String lowerBound, String[] fields) {
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.fields = fields;
	}
	
	@Override
	public QueryBuilder getQuery() {
		
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		
		for(String field : fields) {
			RangeQuery rangeQuery = new RangeQuery(field, upperBound, lowerBound);
			boolQuery.should(rangeQuery.getQuery());
		}
		
		return boolQuery;
	}
}
