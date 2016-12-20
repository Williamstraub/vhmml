package org.vhmml.util;

import java.io.IOException;

import org.vhmml.dto.zotero.ZoteroReferenceCollection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Custom Jackson deserializer for reference collection objects coming from the Zotero web service API.
 * The following is an example of what the data looks like coming from Zotero:<br/>
 * 
 * <code>
 *  {
        "key": "AEU7WRSU",
        "version": 26,
        "reference": {
            "type": "group",
            "id": 275822,
            "name": "ReadingRoomObject Studies (final)",
            "links": {
                "alternate": {
                    "href": "https://www.zotero.org/groups/manuscript_studies_final",
                    "type": "text/html"
                }
            }
        },
        "links": {
            "self": {
                "href": "https://api.zotero.org/groups/275822/collections/AEU7WRSU",
                "type": "application/json"
            },
            "alternate": {
                "href": "https://www.zotero.org/groups/manuscript_studies_final/collections/AEU7WRSU",
                "type": "text/html"
            }
        },
        "meta": {
            "numCollections": 0,
            "numItems": 45
        },
        "data": {
            "key": "AEU7WRSU",
            "version": 26,
            "name": "HMML Publications",
            "parentCollection": false,
            "relations": {}
        }
    }
 * </code>
 * 
 * @author Chad LaVigne
 *
 */
public class ZoteroReferenceCollectionDeserializer extends JsonDeserializer<ZoteroReferenceCollection> {

	@Override
	public ZoteroReferenceCollection deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ZoteroReferenceCollection collection = new ZoteroReferenceCollection();
		JsonNode node = jp.getCodec().readTree(jp);
		JsonNode dataNode = node.get("data");
		Integer version = JacksonUtil.getIntegerProperty(dataNode, "version");
		collection.setVersion(version);
		collection.setName(JacksonUtil.getStringProperty(dataNode, "name"));
		collection.setKey(JacksonUtil.getStringProperty(dataNode, "key"));
		collection.setParentCollection(JacksonUtil.getBooleanProperty(dataNode, "parentCollection"));		
		
		return collection;
	}
}
