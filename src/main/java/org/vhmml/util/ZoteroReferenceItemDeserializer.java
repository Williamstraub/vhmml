package org.vhmml.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vhmml.dto.zotero.ZoteroReferenceCreator;
import org.vhmml.dto.zotero.ZoteroReferenceItem;
import org.vhmml.dto.zotero.ZoteroReferenceTag;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;

/**
 * Custom Jackson deserializer for Library item objects coming from the Zotero web service API. 
 * Using the custom deserializer allows us to keep a clean object model that's void of unnecessary wrapper objects, etc.
 * Please note that not all fields are being used at this time.
 * The following is an example of what the data looks like coming from Zotero:<br/>
 * <code>
 * {
    "key": "PII9EN9K",
    "version": 2,
    "itemType": "book",
    "title": "A Biographical Register of the University of Cambridge to 1500",
    "creators": [
        {
            "creatorType": "author",
            "firstName": "A. B.",
            "lastName": "Emden"
        }
    ],
    "abstractNote": "on Robert Bothe",
    "series": "",
    "seriesNumber": "",
    "volume": "",
    "numberOfVolumes": "",
    "edition": "",
    "place": "Cambridge",
    "publisher": "University Press",
    "date": "1963",
    "numPages": "",
    "language": "",
    "ISBN": "",
    "shortTitle": "Biographical Register (Cambridge)",
    "url": "",
    "accessDate": "",
    "archive": "",
    "archiveLocation": "",
    "libraryCatalog": "",
    "callNumber": "",
    "rights": "",
    "extra": "",
    "dateAdded": "2014-07-25T19:39:51Z",
    "dateModified": "2014-07-25T19:39:51Z",
    "tags": [
        {
            "tag": "BIOGRAPHIES"
        },
        {
            "tag": "University of Cambridge"
        }
    ]
 * </code>
 * 
 * @author Chad LaVigne
 *
 */
public class ZoteroReferenceItemDeserializer extends JsonDeserializer<ZoteroReferenceItem> {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public ZoteroReferenceItemDeserializer() {
		super();
	}
	
	@Override
	public ZoteroReferenceItem deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ZoteroReferenceItem zoteroReferenceItem = new ZoteroReferenceItem();
		JsonNode node = jp.getCodec().readTree(jp);
		Integer version = (Integer)((IntNode)node.get("version")).numberValue();
		zoteroReferenceItem.setVersion(version);
		zoteroReferenceItem.setBlogTitle(JacksonUtil.getStringProperty(node, "blogTitle"));
		zoteroReferenceItem.setBookTitle(JacksonUtil.getStringProperty(node, "bookTitle"));
		zoteroReferenceItem.setConferenceName(JacksonUtil.getStringProperty(node, "conferenceName"));
		zoteroReferenceItem.setDate(JacksonUtil.getStringProperty(node, "date"));
		zoteroReferenceItem.setDictionaryTitle(JacksonUtil.getStringProperty(node, "dictionaryTitle"));
		zoteroReferenceItem.setDirector(JacksonUtil.getStringProperty(node, "director"));
		zoteroReferenceItem.setEdition(JacksonUtil.getStringProperty(node, "edition"));
		zoteroReferenceItem.setEncyclopediaTitle(JacksonUtil.getStringProperty(node, "encyclopediaTitle"));
		zoteroReferenceItem.setIssue(JacksonUtil.getStringProperty(node, "issue"));
		zoteroReferenceItem.setItemType(JacksonUtil.getStringProperty(node, "itemType"));
		zoteroReferenceItem.setKey(JacksonUtil.getStringProperty(node, "key"));
		zoteroReferenceItem.setPages(JacksonUtil.getStringProperty(node, "pages"));
		zoteroReferenceItem.setPlace(JacksonUtil.getStringProperty(node, "place"));
		zoteroReferenceItem.setProceedingsTitle(JacksonUtil.getStringProperty(node, "proceedingsTitle"));
		zoteroReferenceItem.setPublicationTitle(JacksonUtil.getStringProperty(node, "publicationTitle"));
		zoteroReferenceItem.setPublisher(JacksonUtil.getStringProperty(node, "publisher"));
		zoteroReferenceItem.setSeries(JacksonUtil.getStringProperty(node, "series"));
		zoteroReferenceItem.setSeriesNumber(JacksonUtil.getStringProperty(node, "seriesNumber"));
		zoteroReferenceItem.setSeriesTitle(JacksonUtil.getStringProperty(node, "seriesTitle"));
		zoteroReferenceItem.setShortTitle(JacksonUtil.getStringProperty(node, "shortTitle"));
		zoteroReferenceItem.setStudio(JacksonUtil.getStringProperty(node, "studio"));
		zoteroReferenceItem.setTitle(JacksonUtil.getStringProperty(node, "title"));
		zoteroReferenceItem.setType(JacksonUtil.getStringProperty(node, "itemType"));
		zoteroReferenceItem.setUniversity(JacksonUtil.getStringProperty(node, "university"));
		zoteroReferenceItem.setUrl(JacksonUtil.getStringProperty(node, "url"));
		zoteroReferenceItem.setVolume(JacksonUtil.getStringProperty(node, "volume"));
		
		JsonNode creatorsNode = node.get("creators");
		
		if(creatorsNode != null && creatorsNode.isArray() && creatorsNode.size() > 0) {
			List<ZoteroReferenceCreator> creators = new ArrayList<ZoteroReferenceCreator>();
			
			for(JsonNode creatorNode : creatorsNode) {
				creators.add(objectMapper.readValue(creatorNode.toString(), ZoteroReferenceCreator.class));
			}
			
			zoteroReferenceItem.setCreators(creators);
		}	
		
		JsonNode collectionsNode = node.get("collections");
		
		if(collectionsNode != null && collectionsNode.isArray() && collectionsNode.size() > 0) {
			List<String> collectionKeys = new ArrayList<String>();
			
			for(JsonNode collectionKey : collectionsNode) {
				collectionKeys.add(collectionKey.asText());
			}
			
			zoteroReferenceItem.setCollections(collectionKeys);
		}
		
		JsonNode tagsNode = node.get("tags");
		
		if(tagsNode != null && tagsNode.isArray() && tagsNode.size() > 0) {
			List<ZoteroReferenceTag> tags = new ArrayList<ZoteroReferenceTag>();
			
			for(JsonNode tagNode : tagsNode) {
				tags.add(objectMapper.readValue(tagNode.toString(), ZoteroReferenceTag.class));
			}
			
			zoteroReferenceItem.setTags(tags);
		}
				
		return zoteroReferenceItem;
	}
}
