package org.vhmml.service;

import java.util.List;

import org.vhmml.entity.readingroom.Import;

public interface OliverService {
	public List<Import> getImports();
	public Import getImportResult(Long id);
	public Import createImportJob(String databaseLocation);
	public Import runImportJob(Import importJob);
	public void saveImportJob(Import importJob);
	public void deleteImportedData(Long importId);
}
