package org.vhmml.util;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class NumberUtil {

	public static String getNumberWithSuffix(Integer number) {
		String numberWithSuffix = null;
		
		if(number != null) {
			numberWithSuffix = number.toString();
			numberWithSuffix += 
				numberWithSuffix.endsWith("1") && !numberWithSuffix.endsWith("11") ? "st" : 
				numberWithSuffix.endsWith("2") && !numberWithSuffix.endsWith("12") ? "nd" : 
				numberWithSuffix.endsWith("3") && !numberWithSuffix.endsWith("13") ? "rd" : "th";
		}
		
		return numberWithSuffix;
	}
	
	public static String getCenturyDisplayDate(List<Integer> centuries, boolean provisional) {
		StringBuilder centuryDisplay = new StringBuilder();
		
		if(CollectionUtils.isNotEmpty(centuries)) {
			Collections.sort(centuries);
			Integer firstCentury = centuries.get(0);
			Integer lastCentury = centuries.get(centuries.size() - 1);			
			centuryDisplay.append(getNumberWithSuffix(Math.abs(firstCentury)));
			
			if(centuries.size() > 1 && !firstCentury.equals(lastCentury)) {
				if(firstCentury < 0 && lastCentury > 0) {
					centuryDisplay.append(" century BCE");
				}
				
				centuryDisplay.append("-").append(getNumberWithSuffix(Math.abs(lastCentury)));				
			}
			
			centuryDisplay.append(lastCentury < 0 ? " century BCE" : " century");
			
			if(firstCentury < 0 && lastCentury >= 0) {
				centuryDisplay.append(" CE");
			}
			centuryDisplay.append(provisional ? " (?)" : "");
		}
		
		return centuryDisplay.toString();
	}
	
	public static String getCenturyList(Integer firstCentury, Integer lastCentury) {
		StringBuilder centuryList = new StringBuilder();
		
		if(firstCentury != null) {
			lastCentury = lastCentury == null ? firstCentury : lastCentury;
			
			if(firstCentury > lastCentury) {
				Integer temp = firstCentury;
				firstCentury = lastCentury;
				lastCentury = temp;
			}
			
			for(int i = firstCentury; i <= lastCentury; i++) {
				i = i == 0 ? i + 1 : i;
				centuryList.append(i > firstCentury ? " " : ""); 
				String suffix = i < 0 ? " century BCE" : " century CE";
				centuryList.append(getNumberWithSuffix(Math.abs(i))).append(suffix);
			}			
		}
		
		return centuryList.toString();
	}
}
