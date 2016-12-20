package org.vhmml.repository.readingroom;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Subject;

@Repository
public interface ReadingRoomSubjectRepository extends NamedIdentifiableRepository<Subject> {		
	
}
