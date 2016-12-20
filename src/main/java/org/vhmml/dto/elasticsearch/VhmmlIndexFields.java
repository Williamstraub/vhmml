package org.vhmml.dto.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

/**
 * 
 * The class represents custom field mappings for indexed object types. This is how we specify which
 * analyzers to use on particular fields, what variations of the field should be indexed, etc. 
 * If a field isn't mapped here, that doesn't mean it won't be indexed. If an unmapped field is 
 * populated on an object that is put into the index, it will just be indexed using Elastic Search's 
 * default indexing behavior. As an example, we don't need variations of fields like beginDate
 * on ReadingRoom objects and the default anlayzer is just fine because it's just a number, 
 * so that field is not mapped here.
 *
 */
public class VhmmlIndexFields {
	
	private static VhmmlIndexFields instance = new VhmmlIndexFields();
	
	private final List<IndexedField> lexiconFields = new ArrayList<>();
	private final List<IndexedField> referenceFields = new ArrayList<>();	
	private final List<IndexedField> readingRoomFields = new ArrayList<>();
	private final List<IndexedField> readingRoomContributorFields = new ArrayList<>();
	private final List<IndexedField> folioFields = new ArrayList<>();
	private final List<IndexedField> imageViewFields = new ArrayList<>();
	private final List<IndexedField> objectSaveFields = new ArrayList<>();
	
	private VhmmlIndexFields() {
		lexiconFields.add(new IndexedField("term"));
		lexiconFields.add(new IndexedField("shortDefinition"));
		lexiconFields.add(new IndexedField("fullDefinition"));
		lexiconFields.add(new IndexedField("frenchTerms"));
		lexiconFields.add(new IndexedField("latinTerms"));
		lexiconFields.add(new IndexedField("germanTerms"));
		lexiconFields.add(new IndexedField("italianTerms"));
		lexiconFields.add(new IndexedField("spanishTerms"));
		lexiconFields.add(new IndexedField("arabicTerms"));
		lexiconFields.add(new IndexedField("armenianTerms"));
		lexiconFields.add(new IndexedField("syriacTerms"));
		lexiconFields.add(new IndexedField("amharicTerms"));
		lexiconFields.add(new IndexedField("portugueseTerms"));
				
		referenceFields.add(new IndexedField("title"));
		referenceFields.add(new IndexedField("shortTitle"));
		referenceFields.add(new IndexedField("displayTitle"));
		referenceFields.add(new IndexedField("date"));
		referenceFields.add(new IndexedField("author"));
		referenceFields.add(new IndexedField("bookTitle"));
		referenceFields.add(new IndexedField("seriesTitle"));
		referenceFields.add(new IndexedField("encyclopediaTitle"));
		referenceFields.add(new IndexedField("publicationTitle"));
		referenceFields.add(new IndexedField("itemTypeDisplay"));
		referenceFields.add(new IndexedField("place"));
		referenceFields.add(new IndexedField("publisher"));
		referenceFields.add(new IndexedField("series"));
		referenceFields.add(new IndexedField("edition"));
		referenceFields.add(new IndexedField("volume"));
		referenceFields.add(new IndexedField("version"));
		referenceFields.add(new IndexedField("pages"));
		referenceFields.add(new IndexedField("tags"));
	
		
		readingRoomFields.add(new IndexedField("shelfmark"));
		readingRoomFields.add(new IndexedField("titles"));
		readingRoomFields.add(new IndexedField("titlesNs"));
		readingRoomFields.add(new IndexedField("alternateTitles"));
		readingRoomFields.add(new IndexedField("uniformTitles"));
		
		Map<String, String> fieldVariants = new HashMap<>();
		// override the default folding analyzer with the ngram folding one so the folded version of the field does partial matching
		fieldVariants.put("folded", "small_ngram_folding_analyzer");
		fieldVariants.put("zero_trim", "zero_trim_analyzer");
		fieldVariants.put("zero_trim_folded", "zero_trim_folding_analyzer");
		readingRoomFields.add(new IndexedField("hmmlProjectNumber", "string", "small_ngram_analyzer", fieldVariants));
		
		readingRoomFields.add(new IndexedField("inputter"));
		readingRoomFields.add(new IndexedField("inputDate"));
		readingRoomFields.add(new IndexedField("objectType"));
		readingRoomFields.add(new IndexedField("extentDisplay"));
		readingRoomFields.add(new IndexedField("provenance"));
		readingRoomFields.add(new IndexedField("binding"));
		readingRoomFields.add(new IndexedField("processedBy"));
		readingRoomFields.add(new IndexedField("accessRestriction"));
		readingRoomFields.add(new IndexedField("format"));
		readingRoomFields.add(new IndexedField("country"));
		readingRoomFields.add(new IndexedField("city"));
		readingRoomFields.add(new IndexedField("repository"));
		readingRoomFields.add(new IndexedField("holdingInstitution"));
		readingRoomFields.add(new IndexedField("centuries"));
		readingRoomFields.add(new IndexedField("supportDisplay"));
		readingRoomFields.add(new IndexedField("languages"));
		readingRoomFields.add(new IndexedField("authors"));
		readingRoomFields.add(new IndexedField("otherContributors"));
		readingRoomFields.add(new IndexedField("otherContributorsDisplay"));
		readingRoomFields.add(new IndexedField("authorsNs"));
		readingRoomFields.add(new IndexedField("subjects"));		
		readingRoomFields.add(new IndexedField("iconName"));
		readingRoomFields.add(new IndexedField("centuryDisplay"));
		readingRoomFields.add(new IndexedField("centuryList"));
		
		fieldVariants = new HashMap<>();
		// override the default analyzers with ones that don't do stemming because we do exact matches on boolean field and boolean values like "false" get stemmed to "fals"
		fieldVariants.put("folded", "folding_analyzer");
				
		readingRoomFields.add(new IndexedField("active", "default_analyzer", fieldVariants));
		readingRoomFields.add(new IndexedField("locked", "default_analyzer", fieldVariants));
		readingRoomFields.add(new IndexedField("incipit"));
		
		fieldVariants = new HashMap<>();		
		readingRoomFields.add(new IndexedField("features"));
		readingRoomFields.add(new IndexedField("genres"));
		readingRoomFields.add(new IndexedField("scripts"));
		readingRoomFields.add(new IndexedField("writingSystems"));
		
		readingRoomFields.add(new IndexedField("formerOwners"));
		readingRoomFields.add(new IndexedField("artists"));
		readingRoomFields.add(new IndexedField("scribes"));
		readingRoomFields.add(new IndexedField("authorsDisplay"));
		readingRoomFields.add(new IndexedField("scribesNs"));
		readingRoomFields.add(new IndexedField("commonName"));
		readingRoomFields.add(new IndexedField("notes"));
		
		fieldVariants = new HashMap<>();
		// override the default folding analyzer with the ngram folding one so the folded version of the field does partial matching
		fieldVariants.put("folded", "small_ngram_folding_analyzer");
		fieldVariants.put("zero_trim", "zero_trim_analyzer");
		fieldVariants.put("zero_trim_folded", "zero_trim_folding_analyzer");
		readingRoomFields.add(new IndexedField("alternateSurrogates", "string", "small_ngram_analyzer", fieldVariants));
		readingRoomFields.add(new IndexedField("placeOfOrigin"));
		readingRoomFields.add(new IndexedField("bibliography"));
		
		readingRoomContributorFields.add(new IndexedField("name"));
		readingRoomContributorFields.add(new IndexedField("displayName"));
		readingRoomContributorFields.add(new IndexedField("nameNs"));
		readingRoomContributorFields.add(new IndexedField("authorityUriLc"));
		readingRoomContributorFields.add(new IndexedField("authorityUriViaf"));
		
		
		folioFields.add(new IndexedField("id"));		
		folioFields.add(new IndexedField("createdBy"));			
		folioFields.add(new IndexedField("createdDate"));		
		folioFields.add(new IndexedField("lastUpdatedBy"));		
		folioFields.add(new IndexedField("lastUpdate"));
		
		fieldVariants = new HashMap<>();
		// override the default folding analyzer with the ngram folding one so the folded version of the field does partial matching
		fieldVariants.put("folded", "small_ngram_folding_analyzer");
		fieldVariants.put("zero_trim", "zero_trim_analyzer");
		fieldVariants.put("zero_trim_folded", "zero_trim_folding_analyzer");
		folioFields.add(new IndexedField("folioObjectNumber", "string", "small_ngram_analyzer", fieldVariants));
		folioFields.add(new IndexedField("iconName"));
		folioFields.add(new IndexedField("country"));
		folioFields.add(new IndexedField("city"));
		folioFields.add(new IndexedField("repository"));
		folioFields.add(new IndexedField("shelfMark"));
		folioFields.add(new IndexedField("commonName"));
		folioFields.add(new IndexedField("provenance"));
		folioFields.add(new IndexedField("bibliography"));
		folioFields.add(new IndexedField("externalUrl"));
		folioFields.add(new IndexedField("permanentLink"));
		folioFields.add(new IndexedField("acknowledgements"));
		folioFields.add(new IndexedField("placeOfOrigin"));
		folioFields.add(new IndexedField("datePrecise"));
		folioFields.add(new IndexedField("dateCentury"));
		folioFields.add(new IndexedField("language"));
		folioFields.add(new IndexedField("writingSystem"));
		folioFields.add(new IndexedField("script"));
		folioFields.add(new IndexedField("title"));
		folioFields.add(new IndexedField("text"));
		folioFields.add(new IndexedField("description"));
		folioFields.add(new IndexedField("specialFeatures"));
		folioFields.add(new IndexedField("transcription"));
		
		// override the default analyzers with ones that don't do stemming because we do exact matches on boolean field and boolean values like "false" get stemmed to "fals"
		fieldVariants = new HashMap<>();
		fieldVariants.put("folded", "folding_analyzer");
		folioFields.add(new IndexedField("active", "default_analyzer", fieldVariants));
		
		imageViewFields.add(new IndexedField("username"));		
		imageViewFields.add(new IndexedField("collection"));
		imageViewFields.add(new IndexedField("country"));
		imageViewFields.add(new IndexedField("city"));
		imageViewFields.add(new IndexedField("repository"));		
		imageViewFields.add(new IndexedField("date", "date", "yyyy-MM-dd HH:mm:ss z"));
		
		fieldVariants.put("folded", "small_ngram_folding_analyzer");
		fieldVariants.put("zero_trim", "zero_trim_analyzer");
		fieldVariants.put("zero_trim_folded", "zero_trim_folding_analyzer");
		imageViewFields.add(new IndexedField("hmmlProjectNumber", "string", "small_ngram_analyzer", fieldVariants));
						
		objectSaveFields.add(new IndexedField("username"));
		objectSaveFields.add(new IndexedField("date", "date", "yyyy-MM-dd HH:mm:ss z"));
		objectSaveFields.add(new IndexedField("hmmlProjectNumber"));		
	}
	
	public static VhmmlIndexFields getInstance() {
		return instance;
	}
	
	public List<IndexedField> getLexiconFields() {
		return lexiconFields;
	}
	
	public List<IndexedField> getReferenceFields() {
		return referenceFields;
	}
	
	public List<IndexedField> getReadingRoomFields() {
		return readingRoomFields;
	}
	
	public List<IndexedField> getReadingRoomContributorFields() {
		return readingRoomContributorFields;
	}
	
	public List<IndexedField> getFolioFields() {
		return folioFields;
	}
	
	public List<IndexedField> getImageViewFields() {
		return imageViewFields;
	}
	
	public List<IndexedField> getObjectSaveFields() {
		return objectSaveFields;
	}
	
	public static boolean containsField(List<IndexedField> fields, String fieldName) {
		boolean contains = false;
		
		if(CollectionUtils.isNotEmpty(fields)) {
			for(IndexedField field : fields) {
				if(field.getName().equals(fieldName)) {
					contains = true;
					break;
				}
			}
		}
		
		return contains;
	}
}
