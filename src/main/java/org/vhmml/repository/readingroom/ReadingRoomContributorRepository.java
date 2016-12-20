package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Contributor;

@Repository
public interface ReadingRoomContributorRepository extends NamedIdentifiableRepository<Contributor> {
	public List<org.vhmml.entity.readingroom.Contributor> findAllByOrderByNameAsc();
	public Contributor findByNameAndDisplayName(String name, String displayName);
}
