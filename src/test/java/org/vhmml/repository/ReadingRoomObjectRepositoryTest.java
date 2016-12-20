package org.vhmml.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.repository.readingroom.ReadingRoomObjectRepository;
import org.vhmml.util.TestObjectBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/test-data-config.xml"})
@Transactional
public class ReadingRoomObjectRepositoryTest {

	@Autowired
	private ReadingRoomObjectRepository readingRoomObjectRepository;
	
	public ReadingRoomObjectRepositoryTest() {
		super();
	}
	
	@Test 
	public void testDeleteArchivalByImportSource() throws Exception {
		// fail();
	}

	@Test
	public void testSaveArchivalMaterial() throws Exception {
		ArchivalObject archivalObject = TestObjectBuilder.getArchivalMaterial();				
		readingRoomObjectRepository.save(archivalObject);
		
		Long id = archivalObject.getId();
		assertNotNull("No id on archival object after saving", id);
		
		ReadingRoomObject dbObject = readingRoomObjectRepository.findOne(id);
		assertNotNull("Unable to read object from database after save", dbObject);				
		assertTrue("Object not read back from the database as an instance of ArchivalObject", ArchivalObject.class.isInstance(dbObject));
		ArchivalObject dbArchivalObject = (ArchivalObject)dbObject;
		
		ArchivalData archivalData = dbArchivalObject.getArchivalData();
		assertNotNull("No archival data on object after save", archivalData);
		assertNotNull("No archival content on object after save", archivalData.getContent());		
	}
}
