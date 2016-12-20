package org.vhmml.dto.elasticsearch;

import java.util.Date;

public class DateRangeFilter {

	private String dateField;
	private String dateFormat;
	private Date start;
	private Date end;
	
	public DateRangeFilter(String dateField, String dateFormat, Date start, Date end) {
		this.dateField = dateField;
		this.dateFormat = dateFormat;
		this.start = start;
		this.end = end;
	}

	public String getDateField() {
		return dateField;
	}

	public void setDateField(String dateField) {
		this.dateField = dateField;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
