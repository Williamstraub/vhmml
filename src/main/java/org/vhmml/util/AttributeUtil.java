package org.vhmml.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.lang3.StringUtils;

import com.google.common.collect.Lists;

public class AttributeUtil {
	
	public static List<String> getStringAttributeAsList(String attributeValue) {		
		return getStringAttributeAsList(attributeValue, ", ");
	}
	
	public static List<String> getStringAttributeAsList(String attributeValue, String delimiter) {
		List<String> list = new ArrayList<String>();
		
		if(StringUtils.isNotEmpty(attributeValue)) {
			list = Arrays.asList(attributeValue.split(delimiter));
		}
		
		return list;
	}
	
	public static String getListAttributeAsString(List<String> list) {
		
		String stringAtt = null;
		
		if(CollectionUtils.isNotEmpty(list)) {
			stringAtt = StringUtils.join(list, ", ");
		}
		
		return stringAtt;
	}
	
	public static String getDimensionAttribute(Integer height, Integer width, Integer depth) {
		Float heightFloat = height != null ? new Float(height) : null;
		Float widthFloat = width != null ? new Float(width) : null;
		Float depthFloat = depth != null ? new Float(depth) : null;
		return getDimensionAttribute(heightFloat, widthFloat, depthFloat);
	}
	
	public static String getDimensionAttribute(Float height, Float width, Float depth) {
		StringBuilder dimensions = new StringBuilder();
		 NumberFormat numberFormat = new DecimalFormat("##.##");	        
	        
		if(height != null && height > 0) {
			dimensions.append(numberFormat.format(height));
		}
		
		if(width != null && width > 0) {
			if(dimensions.length() > 0) {
				dimensions.append(" x ");
			}
			
			dimensions.append(numberFormat.format(width));
		}
		
		if(depth != null && depth > 0) {
			if(dimensions.length() > 0) {
				dimensions.append(" x ");
			}
			
			dimensions.append(numberFormat.format(depth));
		}
		
		if(dimensions.length() > 0) {
			dimensions.append(" cm");
		}
		
		return dimensions.toString();
	}
	
	public static String getYearRange(Integer beginDate, Integer endDate) {
		StringBuilder yearRange = new StringBuilder();
		
		if(beginDate != null) {
			yearRange.append(beginDate);
			if(beginDate < 0) {
				yearRange.append(" BCE");
			}
		}
		
		if(endDate != null && !endDate.equals(beginDate)) {
			if(yearRange.length() > 0) {
				yearRange.append("-");
			}
			
			yearRange.append(endDate);
			
			if(endDate < 0) {
				yearRange.append(" BCE");
			}
		}
		
		return yearRange.toString();
	}
	
	public static Date getDatePrecise(Integer year, Integer month, Integer day) {
		Date datePrecise = null;
		Calendar cal = new GregorianCalendar();
		cal.clear();
		boolean dateSet = false;
		
		if(year != null) {
			cal.set(Calendar.YEAR, year);
			dateSet = true;
		}
		
		if(month != null) {
			cal.set(Calendar.MONTH, month);
			dateSet = true;
		}
		
		if(day != null) {
			cal.set(Calendar.DAY_OF_MONTH, day);
			dateSet = true;
		}
		
		if(dateSet) {
			datePrecise = cal.getTime();
		}
		
		return datePrecise;
	}
	
	public static String getDatePreciseDisplay(Integer year, Integer month, Integer day) {
		String format = "";
		String displayDate = null;
		Date datePrecise = getDatePrecise(year, month, day);
		
		if(datePrecise != null) {	
			if(year != null) {
				format += "yyy";
			}
			
			if(month != null) {
				format += format.length() > 0 ? " MMMM" : "MMMM";
			}
			
			if(day != null) {
				format += format.length() > 0 ? " dd" : "dd";
			}
					
			SimpleDateFormat displayFormat = new SimpleDateFormat(format);
			displayDate = displayFormat.format(datePrecise);			
		}
		
		return displayDate;
	}
	
	public static String getCenturyDisplay(List<Integer> centuries, boolean centuryUncertain, String defaultValue) {
		
		String centuryDisplay = null;

		if(CollectionUtils.isNotEmpty(centuries)) {
			Integer firstCentury = (Integer)Collections.min(centuries);
			Integer lastCentury = (Integer)Collections.max(centuries);			
			centuryDisplay = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(firstCentury, lastCentury), centuryUncertain);
		} else if(StringUtils.isNotEmpty(defaultValue)) {
			centuryDisplay = defaultValue;
			
			if(centuryUncertain && !centuryDisplay.contains("?")) {
				centuryDisplay += " (?)";
			}
		} else if(centuryUncertain) {
			centuryDisplay = "(?)";
		}
		
		return centuryDisplay;		
	}
}
