package org.vhmml.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vhmml.dto.LexiconSearchResult;
import org.vhmml.dto.LexiconTermView;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.Contributor;
import org.vhmml.entity.LexiconTerm;
import org.vhmml.repository.ContributorRepository;
import org.vhmml.repository.LexiconContributorRepository;
import org.vhmml.repository.LexiconRelatedTermRepository;
import org.vhmml.repository.LexiconTermRepository;
import org.vhmml.util.ElasticSearchUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Service
public class LexiconServiceImpl implements LexiconService {

	private static final Logger LOG = Logger.getLogger(LexiconServiceImpl.class);
	
	@Autowired
	private LexiconTermRepository lexiconTermRepository;	
	
	@Autowired
	private LexiconRelatedTermRepository lexiconRelatedTermRepository;
	
	@Autowired
	private ContributorRepository contributorRepository;
	
	@Autowired
	private LexiconContributorRepository lexiconContributorRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;	
	
	private static ObjectMapper objectMapper = new ObjectMapper();	
	
	public LexiconTerm findById(Long id) {
		return lexiconTermRepository.findOne(id);
	}
	
	public List<LexiconTermView> findAll() {
		List<LexiconTermView> terms = new ArrayList<LexiconTermView>();		
		Iterable<LexiconTerm> termEntities = lexiconTermRepository.findAll();
		
		for(LexiconTerm term : termEntities) {
			terms.add(new LexiconTermView(term));
		}
		
		return terms;
	}
		
	public LexiconSearchResult search(String searchText, Pageable pageable) {		
		LexiconSearchResult searchResult = new LexiconSearchResult();
		String[] fields = IndexedType.getFieldNamesForType(IndexedType.LEXICON_TERM);	
		SearchResponse searchResponse = elasticSearchService.phrasePrefixSearch(IndexedType.LEXICON_TERM, searchText, pageable, fields);		
		SearchHit[] hits = searchResponse.getHits().getHits();
        List<LexiconTermView> terms = new ArrayList<LexiconTermView>();
        
        for(SearchHit hit : hits) {
        	Map<String, Object> source = hit.getSource();
        	Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        	LexiconTermView term = new LexiconTermView();
        	term.setId(new Long((Integer)source.get("id")));
        	term.setTerm(ElasticSearchUtil.getFieldValue(source, highlightFields, "term"));
        	term.setShortDefinition(ElasticSearchUtil.getFieldValue(source, highlightFields, "shortDefinition"));
        	term.setFullDefinition(ElasticSearchUtil.getFieldValue(source, highlightFields, "fullDefinition"));
        	term.setDisplayDefinition(ElasticSearchUtil.findFirstHighlightedField(source, highlightFields, "shortDefinition", "shortDefinition.folded", "fullDefinition", "fullDefinition.folded"));        	
        	term.setFrenchTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "frenchTerms"));        	
        	term.setLatinTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "latinTerms"));        	
        	term.setGermanTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "germanTerms"));        	
        	term.setItalianTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "italianTerms"));        	
        	term.setSpanishTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "spanishTerms"));        	
        	term.setArabicTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "arabicTerms"));        	
        	term.setArmenianTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "armenianTerms"));        	
        	term.setSyriacTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "syriacTerms"));        	
        	term.setAmharicTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "amharicTerms"));        	
        	term.setPortugueseTerms(ElasticSearchUtil.getFieldValue(source, highlightFields, "portugueseTerms")); 
        	term.setHighlightFieldsForDisplay(hit);
        	
        	terms.add(term);
        }		
		
        searchResult.setTerms(terms);
        
		if(pageable.getOffset() > 0) {
			searchResult.setPageNumber(pageable.getOffset()/pageable.getPageSize());
		}
		
		searchResult.setPageSize(pageable.getPageSize());
		searchResult.setTotalElements(searchResponse.getHits().getTotalHits());
        
		return searchResult;
	}
	
	public LexiconSearchResult startsWithSearch(String term, Pageable pageable) {
		List<LexiconTerm> terms = null;
		Sort sort = pageable.getSort();
		Order sortOrder = sort != null ? sort.getOrderFor("term") : null;
		
		if(sortOrder != null && !sortOrder.isAscending()) {
			terms = lexiconTermRepository.findByTermStartingWithOrderByTermDesc(term);			
		} else {
			terms = lexiconTermRepository.findByTermStartingWithOrderByTermAsc(term);
		}
				
		return new LexiconSearchResult(terms);		
	}
	
	public LexiconSearchResult termContainsSearch(String term) {
		return new LexiconSearchResult(lexiconTermRepository.findByTermContains(term));
	}
	
	public List<Contributor> findContributorsByName(String name) {
		return contributorRepository.findByNameContains(name);
	}
	
	@Transactional
	public void delete(Long id) {				
		lexiconRelatedTermRepository.deleteByTermId(id);
		lexiconRelatedTermRepository.deleteByRelatedTermId(id);
		lexiconContributorRepository.deleteByLexiconId(id);
		lexiconTermRepository.delete(id);
		elasticSearchService.removeIndexedObject(IndexedType.LEXICON_TERM, id.toString());
	}
	
	@Transactional
	public LexiconTerm remove(Long id) {
		LexiconTerm term = lexiconTermRepository.findOne(id);
		term.setRemoved(true);
		elasticSearchService.removeIndexedObject(IndexedType.LEXICON_TERM, id.toString());
		return term;
	}
	
	@Transactional
	public LexiconTerm save(LexiconTerm term) {		
		LexiconTerm dbTerm = null;
		
		if(term.getId() == null) {
			dbTerm = create(term);
		} else {
			dbTerm = update(term);
		}
				
		try {
			// index the term without the related terms to avoid Jackson choking on circular reference
			LexiconTerm indexedTerm = new LexiconTerm();
			BeanUtils.copyProperties(term, indexedTerm);
			indexedTerm.setRelatedTerms(null);
			elasticSearchService.updateIndexedObject(IndexedType.LEXICON_TERM, term.getId().toString(), objectMapper.writeValueAsString(indexedTerm));
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException while trying to update index after updating a lexicon term, index will be out of sync with the database", e);
		}		
		
		return dbTerm; 
	}
	
	@Transactional
	private LexiconTerm create(LexiconTerm term) {
		LexiconTerm newTerm = null;
		// get the contributors & related terms from the db, new ones that don't exist yet will be created
		List<Contributor> contributors = getContributors(term);
		List<LexiconTerm> relatedTerms = getRelatedTerms(term);

		term.setContributors(contributors);
		term.setRelatedTerms(relatedTerms);
		
		// now that all the contributors & related terms exist in the database, we can save the term
		newTerm = lexiconTermRepository.save(term);
		
		// now that the term exists, we can point the related terms back at it
		for(LexiconTerm relatedTerm : newTerm.getRelatedTerms()) {
			List<LexiconTerm> relatedTermRelations = relatedTerm.getRelatedTerms();
			if(!relatedTermRelations.contains(newTerm)) {
				relatedTermRelations.add(newTerm);
				lexiconTermRepository.save(relatedTerm);	
			}			
		}		
		
		return newTerm;
	}
	
	@Transactional
	private LexiconTerm update(LexiconTerm term) {
		LexiconTerm dbTerm = lexiconTermRepository.findOne(term.getId());
		List<LexiconTerm> originalRelatedTerms = dbTerm.getRelatedTerms();
		List<LexiconTerm> newRelatedTerms = getRelatedTerms(term);
		List<LexiconTerm> updatedRelatedTerms = new ArrayList<LexiconTerm>();
		List<Contributor> updatedContributors = getContributors(term);
		Map<String, LexiconTerm> newRelatedTermsByTerm = new HashMap<String, LexiconTerm>();
		
		for(LexiconTerm relatedTerm : newRelatedTerms) {
			newRelatedTermsByTerm.put(relatedTerm.getTerm(), relatedTerm);
		}		
		
		// all related term associations are mutual so we must remove the relationship that points the related term back to the original
		for(LexiconTerm original : originalRelatedTerms) {
			if(newRelatedTermsByTerm.get(original.getTerm()) == null) {
				lexiconRelatedTermRepository.deleteByTermIdAndRelatedTermId(original.getId(), dbTerm.getId());
			}
		}
		
		// make sure each related term also has a relationship pointing back to the term
		if(!CollectionUtils.isEmpty(newRelatedTerms)) {			
			
			for(LexiconTerm relatedTerm : newRelatedTerms) {
				List<LexiconTerm> reverseRelatedTerms = relatedTerm.getRelatedTerms();					
				Map<Long, LexiconTerm> reverseRelatedTermsById = getTermsById(reverseRelatedTerms);
		        
				if(reverseRelatedTermsById.get(dbTerm.getId()) == null) {
					reverseRelatedTerms.add(dbTerm);
					relatedTerm.setRelatedTerms(reverseRelatedTerms);
					lexiconTermRepository.save(relatedTerm);
				}
				
				updatedRelatedTerms.add(relatedTerm);
			}			
		}
		
		BeanUtils.copyProperties(term, dbTerm);
		dbTerm.setRelatedTerms(updatedRelatedTerms);
		dbTerm.setContributors(updatedContributors);
		
		return lexiconTermRepository.save(dbTerm); 
	}
	
	private List<LexiconTerm> getRelatedTerms(LexiconTerm term) {
		List<LexiconTerm> dbRelatedTerms = new ArrayList<LexiconTerm>();
		
		if(!CollectionUtils.isEmpty(term.getRelatedTerms())) {			
			 
			for(LexiconTerm relatedTerm : term.getRelatedTerms()) {
				LexiconTerm dbRelatedTerm = lexiconTermRepository.findByTerm(relatedTerm.getTerm());
				
				// if the related term doesn't exist create it
				if(dbRelatedTerm == null) {					
					dbRelatedTerm = lexiconTermRepository.save(relatedTerm);
				}
				
				dbRelatedTerms.add(dbRelatedTerm);
			}			
		}
				
		return dbRelatedTerms;
	}
	
	private List<Contributor> getContributors(LexiconTerm term) {
		List<Contributor> updatedContributors = new ArrayList<Contributor>();
		
		if(!CollectionUtils.isEmpty(term.getContributors())) {
			for(Contributor contributor : term.getContributors()) {
				Contributor dbContributor = contributorRepository.findByName(contributor.getName());
				
				if(dbContributor == null) {
					dbContributor = contributorRepository.save(contributor);
				}
				
				updatedContributors.add(dbContributor);
			}	
		}
		
		return updatedContributors;
	}
	
	private Map<Long, LexiconTerm> getTermsById(List<LexiconTerm> terms) {
		Map<Long, LexiconTerm> termsById = Maps.uniqueIndex(terms,
            new Function<LexiconTerm, Long>() {
                public Long apply(LexiconTerm theTerm) {
                    return theTerm.getId();
                }
            }
		);
		
		return termsById;
	}
}
