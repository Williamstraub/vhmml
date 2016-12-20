package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Language;

@Repository
public interface ReadingRoomLanguageRepository extends NamedIdentifiableRepository<Language> {
	public Language findByName(String name);
	public List<Language> findAllByOrderByNameAsc();
}
