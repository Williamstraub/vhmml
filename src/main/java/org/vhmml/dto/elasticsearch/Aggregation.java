package org.vhmml.dto.elasticsearch;

public class Aggregation {

	private String aggregationField;
	private Integer aggregationMax;

	public Aggregation(String aggregationField, Integer aggregationMax) {
		this.aggregationField = aggregationField;
		this.aggregationMax = aggregationMax;
	}

	public String getAggregationField() {
		return aggregationField;
	}

	public void setAggregationField(String aggregationField) {
		this.aggregationField = aggregationField;
	}

	public Integer getAggregationMax() {
		return aggregationMax;
	}

	public void setAggregationMax(Integer aggregationMax) {
		this.aggregationMax = aggregationMax;
	}
}
