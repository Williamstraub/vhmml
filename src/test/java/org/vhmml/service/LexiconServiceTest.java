package org.vhmml.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.vhmml.entity.LexiconTerm;
import org.vhmml.repository.LexiconTermRepository;
import org.vhmml.util.TestObjectBuilder;

@WebAppConfiguration // need web app config because VhmmlMessageUtil puts messages on servlet context
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/app-config.xml", "classpath:spring/service-config.xml", "classpath:spring/test-data-config.xml"})
@Transactional
public class LexiconServiceTest {

	private static final String TEST_TERM = "Abbreviation";
	
	@Autowired
	private LexiconService lexiconService;
	
	@Autowired
	private LexiconTermRepository lexiconTermRepository;	
	
	@Test
	public void testSaveNewTermWithCorruptRelatedTerms() throws Exception {
		LexiconTerm term = TestObjectBuilder.getLexiconTermWithCorruptRelatedTerms();
		LexiconTerm newRelatedTerm = new LexiconTerm("New term that's not in the database yet");
		
		// remove two relations and add a new one for a term that doesn't exist
		term.getRelatedTerms().remove(0);
		term.getRelatedTerms().remove(0);
		term.getRelatedTerms().add(newRelatedTerm);
		
		LexiconTerm result = lexiconService.save(term);
		
		assertNotNull("Null result returned after saving lexicon term", result);
		assertTrue("No related terms found on result after saving lexicon term", result.getRelatedTerms().size() > 0);
		assertEquals("Wrong number of related terms after saving lexicon term", 5, result.getRelatedTerms().size());
		
		// make sure all the related terms are correctly associated to the term now
		
		for(LexiconTerm relatedTerm : result.getRelatedTerms()) {
			assertNotNull("No corresponding related term pointing back to original term", relatedTerm.getRelatedTerms());
			assertEquals("Wrong number of related terms found on related term, should only find one pointing back to original term", 1, relatedTerm.getRelatedTerms().size());
			assertEquals("No corresponding related term pointing back to original term", term.getTerm(), relatedTerm.getRelatedTerms().get(0).getTerm());
		}
	}
	
	@Test
	public void testSaveTermWithCorruptRelatedTerms() throws Exception {
		LexiconTerm term = lexiconTermRepository.findByTerm(TEST_TERM);
		LexiconTerm newRelatedTerm = new LexiconTerm("New term that's not in the database yet");
		
		// add a new relation that doesn't point back to the term, should come back related to each other		
		term.getRelatedTerms().add(newRelatedTerm);
		
		LexiconTerm result = lexiconService.save(term);
		
		assertNotNull("Null result returned after saving lexicon term", result);
		assertTrue("No related terms found on result after saving lexicon term", result.getRelatedTerms().size() > 0);		
		
		// make sure all the related terms are correctly associated to the term now		
		for(LexiconTerm relatedTerm : result.getRelatedTerms()) {
			assertNotNull("No corresponding related term pointing back to original term", relatedTerm.getRelatedTerms());
			assertTrue("No corresponding related term pointing back to original term", relatedTerm.getRelatedTerms().contains(result));
		}
	}
}
