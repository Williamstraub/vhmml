package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.vhmml.entity.NamedIdentifiable;

@NoRepositoryBean
public interface NamedIdentifiableRepository <E extends NamedIdentifiable> extends CrudRepository<E, Long> {
	public Page<E> findAll(Pageable pageable);
	public Page<E> findByNameContains(String name, Pageable pageable);
	public E findByName(String name);
	public List<E> findAllByOrderByNameAsc();
	public List<E> findAllByNameInOrderByNameAsc(List<String> names);
}
