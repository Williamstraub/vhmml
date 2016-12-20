package org.vhmml.dto.zotero;

import java.util.ArrayList;
import java.util.List;

import org.vhmml.dto.SearchResult;
import org.vhmml.entity.ReferenceEntry;

public class ZoteroSearchResult extends SearchResult {

	private List<ZoteroReferenceItem> zoteroItems = new ArrayList<ZoteroReferenceItem>();
	private List<ReferenceEntry> referenceEntries = new ArrayList<ReferenceEntry>();	
	private Integer skippedItems;
	
	public ZoteroSearchResult() {
		super();
	}

	public List<ZoteroReferenceItem> getZoteroItems() {
		return zoteroItems;
	}

	public void setZoteroItems(List<ZoteroReferenceItem> zoteroItems) {
		this.zoteroItems = zoteroItems;
	}

	public List<ReferenceEntry> getReferenceEntries() {
		return referenceEntries;
	}

	public void setReferenceEntries(List<ReferenceEntry> referenceEntries) {
		this.referenceEntries = referenceEntries;
	}

	public Integer getSkippedItems() {
		return skippedItems;
	}

	public void setSkippedItems(Integer skippedItems) {
		this.skippedItems = skippedItems;
	}
}
