package org.vhmml.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vhmml.entity.ReferenceCreator;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.util.TestObjectBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/test-data-config.xml"})
@Transactional
public class ReferenceEntryRepositoryTest {

	@Autowired
	private ReferenceEntryRepository referenceEntryRepository;
	
	@Autowired
	private ReferenceCreatorRepository referenceCreatorRepository;
	
	@Autowired
	private CreatorRepository creatorRepository;	
	
	public ReferenceEntryRepositoryTest() {
		super();
	}

	@Test
	public void testDeleteEntry() throws Exception {
		ReferenceEntry entry = TestObjectBuilder.getReferenceEntryWithMultipleCreators();
		long entryCountBefore = referenceEntryRepository.count();
		long refCreatorCountBefore = referenceCreatorRepository.count();
		long creatorCountBefore = creatorRepository.count();
		
		for(ReferenceCreator refCreator : entry.getReferenceCreators()) {
			creatorRepository.save(refCreator.getCreator());
		}
		
		entry = referenceEntryRepository.save(entry);
		assertNotNull("No id on reference entry after saving", entry.getId());
		
		Long entryId = entry.getId();
		entry = referenceEntryRepository.findOne(entryId);
		
		assertNotNull("Unable to read reference entry after saving", entry);		
		assertEquals("Wrong reference entry count after save", entryCountBefore + 1, referenceEntryRepository.count());
		assertEquals("Wrong reference creator count after save", refCreatorCountBefore + 2, referenceCreatorRepository.count());
		assertEquals("Wrong reference entry count after save", creatorCountBefore + 2, creatorRepository.count());
		
		referenceEntryRepository.delete(entryId);
		
		entry = referenceEntryRepository.findOne(entryId);
		assertNull("Entry was not null when reading after delete", entry);
		
		// entry & entry-creator relationship should be gone, but the creator records should stay there
		assertEquals("Wrong reference entry count after delete", entryCountBefore, referenceEntryRepository.count());
		assertEquals("Wrong reference creator count after delete", refCreatorCountBefore, referenceCreatorRepository.count());
		assertEquals("Wrong reference entry count after delete", creatorCountBefore + 2, creatorRepository.count());
	}
}
