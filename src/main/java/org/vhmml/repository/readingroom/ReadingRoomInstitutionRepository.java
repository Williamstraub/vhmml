package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Institution;

@Repository
public interface ReadingRoomInstitutionRepository extends NamedIdentifiableRepository<Institution> {
	
	public List<Institution> findAllByOrderByNameAsc();
	public Institution findByName(String name);
}
