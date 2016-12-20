package org.vhmml.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vhmml.dto.Report;
import org.vhmml.dto.elasticsearch.Aggregation;
import org.vhmml.dto.elasticsearch.DateRangeFilter;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery.BoolOperation;
import org.vhmml.dto.elasticsearch.MultiMatchQuery;

import com.google.common.collect.Lists;
import com.ibm.icu.util.Calendar;

@Service
public class ReportServiceImpl implements ReportService {

	private static final Logger LOG = Logger.getLogger(ReportServiceImpl.class);
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	public Report runReport(Report.Type reportType, Map<String, String> reportParameters, Pageable pageable) {		
		Report report = null;
		SearchResponse response = null;
		Date startDate = getDate(reportParameters.get("startDate"));
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.setTime(getDate(reportParameters.get("endDate")));
		endDate.add(Calendar.DAY_OF_MONTH, 1);
		DateRangeFilter dateFilter = new DateRangeFilter("date", ImageService.IMAGE_VIEW_DATE_FORMAT, startDate, endDate.getTime());
		
		switch(reportType) {
			case POPULARITY:
				String aggregationMaxString = reportParameters.get("aggregationMax");
				Integer aggregationMax = StringUtils.isNotEmpty(aggregationMaxString) ? Integer.valueOf(aggregationMaxString) : 100;
				Aggregation aggregation = new Aggregation(reportParameters.get("aggregationField"), aggregationMax);												
				response = elasticSearchService.findAll(reportType.getIndexedType(), pageable, dateFilter, aggregation);
				report = getAggregationReport(response, aggregation, pageable);
				break;
			case OBJECT:
				List<ElasticSearchQuery> queries = new ArrayList<>();
				String projectNumber = reportParameters.get("hmmlProjectNumber");
				
				if(StringUtils.isEmpty(projectNumber)) {
					response = elasticSearchService.findAll(reportType.getIndexedType(), pageable, dateFilter, null);
				} else {
					String[] zeroTrimFields = new String[] {"hmmlProjectNumber.zero_trim", "hmmlProjectNumber.zero_trim_folded"}; 
					queries.add(new MultiMatchQuery(projectNumber, zeroTrimFields, MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.OR, BoolOperation.SHOULD));				
					queries.add(new MultiMatchQuery(projectNumber, zeroTrimFields, MultiMatchQueryBuilder.Type.PHRASE, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, BoolOperation.SHOULD));
					queries.add(new MultiMatchQuery(projectNumber, new String[] {"hmmlProjectNumber.zero_trim", "hmmlProjectNumber.zero_trim_folded"}, MultiMatchQueryBuilder.Type.PHRASE_PREFIX));
					response = elasticSearchService.multiFieldSearch(reportType.getIndexedType(), queries, false, pageable, dateFilter);
				}
				
				report = getReport(reportType, response, pageable);
				break;
			case USER:
			case CATALOGER:
				String username = reportParameters.get("username");
				
				if(StringUtils.isEmpty(username)) {
					response = elasticSearchService.findAll(reportType.getIndexedType(), pageable, dateFilter, null);
				} else {
					response = elasticSearchService.phrasePrefixSearch(reportType.getIndexedType(), username, pageable, dateFilter, new String[] {"username", "username.folded"});
				}
				
				report = getReport(reportType, response, pageable);
				break;
		}		
				
		return report;
	}
	
	public String getReportCSV(Report.Type reportType, Map<String, String> reportParameters, Pageable pageable) {
		StringBuilder csv = new StringBuilder();
		Report report = runReport(reportType, reportParameters, pageable);
		String startDate = reportParameters.get("startDate");
		String endDate = reportParameters.get("endDate");
		csv.append(reportType.getDisplayName()).append(" Report for ").append(startDate).append(" - ").append(endDate).append("\n");
		
		switch(reportType) {
			case POPULARITY:				
				String aggregationField = reportParameters.get("aggregationField");
				String fieldLabel = "hmmlProjectNumber".equals(aggregationField) ? "Object" : WordUtils.capitalize(aggregationField);				
				csv.append(fieldLabel).append(",").append("Number of Hits").append("\n");
				
				for(Map<String, Object> row : report.getData()) {
					csv.append(row.get("key")).append(",").append(row.get("count")).append("\n");
				}
				
				break;
			case OBJECT:
				csv.append("Object").append(",").append("Username").append(",").append("Date Accessed").append("\n");
				
				for(Map<String, Object> row : report.getData()) {
					csv.append(row.get("hmmlProjectNumber")).append(",").append(row.get("username")).append(",").append(row.get("date")).append("\n");
				}
				break;
			case USER:
			case CATALOGER:
				String dateLabel = reportType == Report.Type.CATALOGER ? "Date Added/Edited" : "Date Accessed";
				csv.append("Username").append(",").append("Object").append(",").append(dateLabel).append("\n");
				
				for(Map<String, Object> row : report.getData()) {
					csv.append(row.get("username")).append(",").append(row.get("hmmlProjectNumber")).append(",").append(row.get("date")).append("\n");
				}
				break;
		}
		
		return csv.toString();
	}
	
	private Report getAggregationReport(SearchResponse searchResponse, Aggregation aggregation, Pageable pageable) {
		String aggregationField = aggregation.getAggregationField();
		Report report = new Report(aggregationField);
		List<Map<String, Object>> reportData = new ArrayList<>();
		Terms  terms = searchResponse.getAggregations().get(aggregationField);
		List<Terms.Bucket> buckets = Lists.newArrayList(terms.getBuckets());               
        int start = pageable != null ? pageable.getOffset() : 0;
        int end = pageable != null ? start + pageable.getPageSize() : buckets.size();
        end = end > buckets.size() ? buckets.size() : end;
        
        for(int i = start; i < end; i++) {
        	Terms.Bucket bucket = buckets.get(i);
        	Map<String, Object> hit = new HashMap<>();
        	hit.put("key", bucket.getKey());
        	hit.put("count", bucket.getDocCount());
        	reportData.add(hit);
        }		
		
        report.setData(reportData);        
		report.setPagingInformation(searchResponse, pageable);
		report.setTotalElements(new Long(buckets.size()));
		
		return report;
	}
	
	private Report getReport(Report.Type reportType, SearchResponse searchResponse, Pageable pageable) {
		Report report = new Report(reportType.getDisplayName());
		List<Map<String, Object>> reportData = new ArrayList<>();
		SearchHit[] hits = searchResponse.getHits().getHits();       
        
        for(SearchHit hit : hits) {
        	reportData.add(hit.getSource());        	
        }		
		        
        report.setData(reportData);
		report.setPagingInformation(searchResponse, pageable);
		
		return report;
	}
	
	private Date getDate(String dateString) {
		Date date = null;
		
		if(StringUtils.isNotEmpty(dateString)) {
			try {
				date = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
			} catch (ParseException e) {				
				LOG.error("Unexpected exception trying to parse date string " + dateString, e);
			}
		}

		return date;
	}
}
