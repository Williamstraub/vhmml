package org.vhmml.util;

import java.io.IOException;
import java.util.List;

import org.vhmml.dto.zotero.ZoteroReferenceGroup;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Custom Jackson deserializer for Library group objects coming from the Zotero web service API. 
 * The following is an example of what the data looks like coming from Zotero:<br/>
 * 
 * <code>
 * {
        "id": 337150,
        "version": 3,
        "links": {
            "self": {
                "href": "https://api.zotero.org/groups/337150",
                "type": "application/json"
            },
            "alternate": {
                "href": "https://www.zotero.org/groups/manuscript_studies_vhmml",
                "type": "text/html"
            }
        },
        "meta": {
            "created": "2015-03-16T21:07:01Z",
            "lastModified": "2015-03-16T21:11:06Z",
            "numItems": 0
        },
        "data": {
            "id": 337150,
            "version": 3,
            "name": "ReadingRoomObject Studies vhmml",
            "owner": 1750527,
            "type": "PublicClosed",
            "description": "<p> paleography  bibliographic info</p>",
            "url": "",
            "libraryEditing": "admins",
            "libraryReading": "members",
            "fileEditing": "admins",
            "members": [
                1113284
            ]
        }
    }
 * 
 * </code>
 * 
 * @author Chad LaVigne
 *
 */
public class ZoteroReferenceGroupDeserializer extends JsonDeserializer<ZoteroReferenceGroup> {

	@Override
	public ZoteroReferenceGroup deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ZoteroReferenceGroup group = new ZoteroReferenceGroup();
		JsonNode node = jp.getCodec().readTree(jp);
		JsonNode dataNode = node.get("data");
		Integer id = JacksonUtil.getIntegerProperty(dataNode, "id");
		Integer version = JacksonUtil.getIntegerProperty(dataNode, "version");
		Integer owner = JacksonUtil.getIntegerProperty(dataNode, "owner");
		group.setId(id);
		group.setVersion(version);
		group.setOwner(owner);
		group.setName(JacksonUtil.getStringProperty(dataNode, "name"));
		group.setType(JacksonUtil.getStringProperty(dataNode, "type"));
		group.setDescription(JacksonUtil.getStringProperty(dataNode, "description"));
		group.setUrl(JacksonUtil.getStringProperty(dataNode, "url"));
		group.setLibraryEditing(JacksonUtil.getStringProperty(dataNode, "libraryEditing"));
		group.setLibraryReading(JacksonUtil.getStringProperty(dataNode, "libraryReading"));
		group.setFileEditing(JacksonUtil.getStringProperty(dataNode, "fileEditing"));
		List<JsonNode> members = dataNode.findValues("members");
		
		for(JsonNode member : members) {
			group.getMembers().add(member.asInt());
		}
		
		return group;
	}
}
