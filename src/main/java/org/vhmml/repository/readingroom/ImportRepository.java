package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Import;

@Repository
public interface ImportRepository extends CrudRepository<Import, Long> {
	public List<Import> findAllByOrderByImportDateDesc();
}
