package org.vhmml.dto.zotero;

import java.util.ArrayList;
import java.util.List;

public class ZoteroCollectionImportResult extends ImportResult {
	
	private Integer totalCollectionItemCount;
	private List<ZoteroReferenceItem> collectionItems = new ArrayList<ZoteroReferenceItem>();	
	
	public ZoteroCollectionImportResult() {
		super();
	}

	public Integer getTotalCollectionItemCount() {
		return totalCollectionItemCount;
	}

	public void setTotalCollectionItemCount(Integer totalCollectionItemCount) {
		this.totalCollectionItemCount = totalCollectionItemCount;
	}
	
	public List<ZoteroReferenceItem> getCollectionItems() {
		return collectionItems;
	}

	public void setCollectionItems(List<ZoteroReferenceItem> collectionItems) {
		this.collectionItems = collectionItems;
	}
}
