package org.vhmml.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vhmml.entity.LexiconTerm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/test-data-config.xml"})
@Transactional
public class LexiconTermRepositoryTest {

	@Autowired
	private LexiconTermRepository lexiconTermRepository;
	
	@Test
	public void testFindByTerm() throws Exception {
		LexiconTerm term = lexiconTermRepository.findByTerm("Abbreviation");
		
		assertNotNull("No term found for Abbreviation", term);
	}
	
	@Test
	public void testSaveLexicon() throws Exception {
		LexiconTerm term = new LexiconTerm();
		String originalTermText = "Original Term";
		String relatedTermText1 = "First related term";
		String relatedTermText2 = "Second related term";
		term.setTerm(originalTermText);
		
		List<LexiconTerm> relatedTerms = new ArrayList<LexiconTerm>();
		List<LexiconTerm> reverseRelatedTerms = new ArrayList<LexiconTerm>();
		LexiconTerm related1 = new LexiconTerm(relatedTermText1);
		LexiconTerm related2 = new LexiconTerm(relatedTermText2);
		reverseRelatedTerms.add(term);
		related1.setRelatedTerms(reverseRelatedTerms);
		related2.setRelatedTerms(reverseRelatedTerms);
		relatedTerms.add(related1);
		relatedTerms.add(related2);
		
		term.setRelatedTerms(relatedTerms);
		
		lexiconTermRepository.save(term);
		
		LexiconTerm dbTerm = lexiconTermRepository.findOne(term.getId());
		assertNotNull("Unable to retrieve lexicon term after saving", dbTerm);
		assertTrue("No related terms found after saving lexicon term", dbTerm.getRelatedTerms().size() > 0);
		
		for(LexiconTerm relatedTerm : dbTerm.getRelatedTerms()) {
			assertNotNull("Term text was null on related term after retrieving from the database", relatedTerm.getTerm());
		}
		
		LexiconTerm relatedTerm1 = lexiconTermRepository.findByTerm(relatedTermText1);
		assertNotNull("Related term #1 was null trying to retrieve by term text", relatedTerm1);
		// the first related term should also be related back to the original
		assertTrue("No related terms found on related term #1 (is should point back to the orginial term it's related to)", relatedTerm1.getRelatedTerms().size() > 0);
		assertEquals("Wrong term text on related term #1's first related term", originalTermText, relatedTerm1.getRelatedTerms().get(0).getTerm());
		
		LexiconTerm relatedTerm2 = lexiconTermRepository.findByTerm(relatedTermText2);
		assertNotNull("Related term #2 was null trying to retrieve by term text", relatedTerm2);
		// the second related term should also be related back to the original
		assertTrue("No related terms found on related term #2 (is should point back to the orginial term it's related to)", relatedTerm2.getRelatedTerms().size() > 0);
		assertEquals("Wrong term text on related term #2's first related term", originalTermText, relatedTerm2.getRelatedTerms().get(0).getTerm());
	}
	
	@Test
	public void testSaveRelatedTerms() throws Exception {
		LexiconTerm existingTerm = lexiconTermRepository.findByTerm("Abbreviation");
		
		assertNotNull("Unable to find Abbreviation in lexicon_terms table", existingTerm);
		assertTrue("No related terms for Abbreviation", existingTerm.getRelatedTerms().size() > 0);
		int relatedTermCount = existingTerm.getRelatedTerms().size();
		
		// remove two related terms and add a new one for a term that doesn't exist
		existingTerm.getRelatedTerms().remove(0);
		existingTerm.getRelatedTerms().remove(0);
		existingTerm.getRelatedTerms().add(new LexiconTerm("New term that's not in the database yet"));
		
		LexiconTerm result = lexiconTermRepository.save(existingTerm);
		
		assertNotNull("Result was null after saving updated lexicon term", result);
		assertTrue("No related terms for Abbreviation after saving", existingTerm.getRelatedTerms().size() > 0);
		assertEquals("Wrong number of related terms after saving saving updated lexicon term", relatedTermCount - 1, result.getRelatedTerms().size());
	}
}
