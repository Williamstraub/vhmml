package org.vhmml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.Lists;

public class NumberUtilTest {

	@Test
	public void testGetCenturyDisplayDate() throws Exception {
		// test with one century
		String centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(15), false);
		assertNotNull("Century date was null calling with single century", centuryDate);
		assertEquals("Wrong century date format calling with single century", "15th century", centuryDate);
		
		// test with list of two
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(1, 3), false);
		assertNotNull("Century date was null calling with two centuries", centuryDate);
		assertEquals("Wrong century date format calling with two centuries", "1st-3rd century", centuryDate);
		
		// test with list of more than two
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(15, 18, 19), false);
		assertNotNull("Century date was null calling with more than two centuries", centuryDate);
		assertEquals("Wrong century date format calling with more than two centuries", "15th-19th century", centuryDate);
		
		// test with list of more than two out of order
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(18, 14, 16), false);
		assertNotNull("Century date was null calling with more than two centuries out of order", centuryDate);
		assertEquals("Wrong century date format calling with more than two centuries outer of order", "14th-18th century", centuryDate);
		
		// test with BCE dates		
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(-20, -15), false);
		assertNotNull("Century date was null calling with BCE dates", centuryDate);
		assertEquals("Wrong century date format calling with BCE dates", "20th-15th century BCE", centuryDate);
		
		// test with provisional flag set		
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(-4, -6), true);
		assertNotNull("Century date was null calling with provisional flag set", centuryDate);
		assertEquals("Wrong century date format calling with provisional flag set", "6th-4th century BCE (?)", centuryDate);
		
		// test with BCE and CE dates
		centuryDate = NumberUtil.getCenturyDisplayDate(Lists.newArrayList(-4, 2), true);
		assertNotNull("Century date was null calling with BCE and CE dates", centuryDate);
		assertEquals("Wrong century date format calling with BCE and CE dates", "4th century BCE-2nd century CE (?)", centuryDate);
	}
	
	@Test
	public void testGetCenturyList() throws Exception {
		// test with one century
		String centuryList = NumberUtil.getCenturyList(5, null);
		assertNotNull("Century list was null calling with single century", centuryList);
		assertEquals("Wrong century date format calling with single century", "5th century CE", centuryList);
		
		// test with centuries backwards
		centuryList = NumberUtil.getCenturyList(1, -2);
		assertNotNull("Century list was null calling with centuries backwards", centuryList);
		assertEquals("Wrong century date format calling with centuries backwards", "2nd century BCE 1st century BCE 1st century CE", centuryList);
		
		// test with valid args
		centuryList = NumberUtil.getCenturyList(2, 5);
		assertNotNull("Century list was null calling with valid args", centuryList);
		assertEquals("Wrong century date format calling with valid args", "2nd century CE 3rd century CE 4th century CE 5th century CE", centuryList);
	}
}
