package org.vhmml.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Country;
import org.vhmml.repository.readingroom.NamedIdentifiableRepository;

@Repository
public interface CountryRepository extends NamedIdentifiableRepository<Country> {
	
	// Please note that this method name follows a very specific Spring Data JPA convention that must 
	// be adhered to in order for the finder impl to be generated with the correct order by clause.
	// Therefore, even though findAllOrderByName would be a better name, the extra "By" and "Asc" must
	// be part of the method name in order for the sort to work
	public List<Country> findAllByOrderByNameAsc();
	public Country findByName(String name);
}
