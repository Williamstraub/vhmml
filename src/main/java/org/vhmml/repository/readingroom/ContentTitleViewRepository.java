package org.vhmml.repository.readingroom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ContentTitleView;

@Repository
public interface ContentTitleViewRepository extends CrudRepository<ContentTitleView, Long> {
	public Page<ContentTitleView> findAll(Pageable pageable);
	public Page<ContentTitleView> findByTitleContains(String title, Pageable pageable);
}
