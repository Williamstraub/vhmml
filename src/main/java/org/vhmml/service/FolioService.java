package org.vhmml.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.vhmml.dto.FolioSearchResult;
import org.vhmml.entity.FolioObject;

public interface FolioService {
	
	public FolioObject find(Long objectId);
	public List<FolioObject> findAll();
	public List<String> findLanguages();	
	public List<String> findWritingSystems();	
	public List<String> findScripts();
	public FolioObject save(FolioObject folioObject);
	public void delete(Long objectId);
	public FolioSearchResult search(Map<String, String> searchTerms, Pageable pageable);
}
