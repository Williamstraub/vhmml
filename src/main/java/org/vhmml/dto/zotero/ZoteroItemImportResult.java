package org.vhmml.dto.zotero;

public class ZoteroItemImportResult extends ImportResult {
	
	private ZoteroReferenceItem item;
	
	public ZoteroItemImportResult() {
		super();
	}

	public ZoteroReferenceItem getItem() {
		return item;
	}

	public void setItem(ZoteroReferenceItem item) {
		this.item = item;
	}
}
