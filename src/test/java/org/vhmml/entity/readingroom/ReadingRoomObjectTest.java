package org.vhmml.entity.readingroom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReadingRoomObjectTest {

	@Test
	public void testGetCollection() throws Exception {
		ReadingRoomObject object = new ReadingRoomObject();
		
		object.setHmmlProjectNumber("44556");		
		assertEquals("44556", object.getCollection());
		
		object.setHmmlProjectNumber("Malta Series I");		
		assertEquals("Malta Series I", object.getCollection());
		
		object.setHmmlProjectNumber("England 12");
		assertEquals("England", object.getCollection());
		
		object.setHmmlProjectNumber("SAV BMH 00001");
		assertEquals("SAV BMH", object.getCollection());
		
		object.setHmmlProjectNumber("AODA 00235");
		assertEquals("AODA", object.getCollection());
		
		object.setHmmlProjectNumber("SEP 00234");
		assertEquals("SEP", object.getCollection());
		
		object.setHmmlProjectNumber("APTSCH THRI 00032");
		assertEquals("APTSCH THRI", object.getCollection());		
	}
}
