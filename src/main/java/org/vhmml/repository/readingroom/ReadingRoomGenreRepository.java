package org.vhmml.repository.readingroom;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Genre;

@Repository
public interface ReadingRoomGenreRepository extends NamedIdentifiableRepository<Genre> {	
	
}
