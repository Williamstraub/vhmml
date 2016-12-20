package org.vhmml.dto.elasticsearch;

import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;


public class MultiMatchQuery extends ElasticSearchQuery {

	protected String[] fields;
	protected Type multiMatchType;
	protected Operator operator;
	
	public MultiMatchQuery(String searchText, String[] fields, Type multiMatchType) {
		this.searchText = searchText;
		this.fields = fields;
		this.multiMatchType = multiMatchType;
	}
	
	public MultiMatchQuery(String searchText, String[] fields, Type multiMatchType, int slop) {
		this(searchText, fields, multiMatchType);
		this.slop = slop;		
	}
	
	public MultiMatchQuery(String searchText, String[] fields, Type multiMatchType, Operator operator, BoolOperation parentBoolOperation) {
		this.searchText = searchText;
		this.fields = fields;
		this.multiMatchType = multiMatchType;
		this.operator = operator;
		this.parentBoolOperation = parentBoolOperation;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public Type getMultiMatchType() {
		return multiMatchType;
	}

	public void setMultiMatchType(Type multiMatchType) {
		this.multiMatchType = multiMatchType;
	}
	
	public QueryBuilder getQuery() {
		QueryBuilder query = QueryBuilders.multiMatchQuery(searchText, fields).type(multiMatchType).slop(getSlop());
		
		if(operator != null) {
			((MultiMatchQueryBuilder)query).operator(operator);
		} else if(Type.CROSS_FIELDS == multiMatchType || Type.PHRASE == multiMatchType) {
			((MultiMatchQueryBuilder)query).operator(Operator.AND);
		}
		
		return query;
	}
}
