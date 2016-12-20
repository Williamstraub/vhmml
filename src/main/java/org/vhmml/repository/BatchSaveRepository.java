package org.vhmml.repository;

import java.util.List;

/**
 * Interface defining a custom batchSave to do batch inserts that perform better than save(Collection<Entity>) method
 * we get from CrudRepository.
 * 
 * @author Chad LaVigne
 *
 * @param <T>
 */
public interface BatchSaveRepository<T> {
	int batchSave(List<T> records);
}
