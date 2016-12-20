package org.vhmml.dto;

import org.vhmml.entity.FolioObject;

public class FolioSearchHit extends SearchHit {

	private FolioObject folioObject;

	public FolioSearchHit(FolioObject folioObject) {
		this.folioObject = folioObject;
	}
	
	public FolioObject getFolioObject() {
		return folioObject;
	}

	public void setFolioObject(FolioObject folioObject) {
		this.folioObject = folioObject;
	}
}
