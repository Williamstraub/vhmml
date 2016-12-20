package org.vhmml.dto.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.vhmml.service.ElasticSearchServiceImpl.Index;

public enum IndexedType {
	LEXICON_TERM(Index.SEARCH, "lexicon_term", VhmmlIndexFields.getInstance().getLexiconFields()),
	REFERENCE_ITEM(Index.SEARCH, "reference_item", VhmmlIndexFields.getInstance().getReferenceFields()),
	READING_ROOM_OBJECT(Index.SEARCH, "reading_room_object", VhmmlIndexFields.getInstance().getReadingRoomFields()),
	READING_ROOM_CONTRIBUTOR(Index.SEARCH, "reading_room_contributor", VhmmlIndexFields.getInstance().getReadingRoomContributorFields()),
	FOLIO_OBJECT(Index.SEARCH, "folio_object", VhmmlIndexFields.getInstance().getFolioFields()),
	IMAGE_VIEW(Index.REPORT, "image_view", VhmmlIndexFields.getInstance().getImageViewFields()),
	READING_ROOM_OBJECT_SAVE(Index.REPORT, "object_save", VhmmlIndexFields.getInstance().getObjectSaveFields());
	
	private Index index;
	private String name;
	private List<IndexedField> fields;
	
	IndexedType(Index index, String name, List<IndexedField> fields) {
		this.index = index;
		this.name = name;
		this.fields = fields;
	}
	
	public String getName() {
		return name;
	}
	
	public Index getIndex() {
		return index;
	}
	
	public List<IndexedField> getFields() {
		return fields;
	}
	
	public static List<IndexedType> getTypesForIndex(Index index) {
		List<IndexedType> types = new ArrayList<>();
		
		for(IndexedType type : values()) {
			if(type.getIndex() == index) {
				types.add(type);
			}
		}
		
		return types;
	}
	
	public static String[] getFieldNamesForType(IndexedType indexedType) {
		List<String> fieldNames = new ArrayList<>();
		List<IndexedField> fields = indexedType.getFields();
		
		if(CollectionUtils.isNotEmpty(fields)) {
			for(IndexedField field : fields) {
				String fieldName = field.getName();
					
				fieldNames.add(fieldName);
				
				Map<String, String> fieldVariants = field.getFieldVariants();
				
				if(MapUtils.isNotEmpty(fieldVariants)) {
					Set<String> variantNames = fieldVariants.keySet();
					
					for(String variantName : variantNames) {
						fieldNames.add(fieldName + "." + variantName);
					}
				}
			}
		}
						
		return fieldNames.toArray(new String[fieldNames.size()]);
	}
}
