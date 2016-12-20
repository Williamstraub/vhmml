package org.vhmml.dto.zotero;

import org.vhmml.util.ZoteroReferenceCollectionDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ZoteroReferenceCollectionDeserializer.class)
public class ZoteroReferenceCollection {

	private String key;
	private Integer version;
	private String name;
	private boolean parentCollection;
	private Integer importedItemCount;
	
	public ZoteroReferenceCollection() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isParentCollection() {
		return parentCollection;
	}

	public void setParentCollection(boolean parentCollection) {
		this.parentCollection = parentCollection;
	}

	public Integer getImportedItemCount() {
		return importedItemCount;
	}

	public void setImportedItemCount(Integer importedItemCount) {
		this.importedItemCount = importedItemCount;
	}
}
