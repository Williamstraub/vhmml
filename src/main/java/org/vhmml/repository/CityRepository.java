package org.vhmml.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.City;
import org.vhmml.repository.readingroom.NamedIdentifiableRepository;

@Repository
public interface CityRepository extends NamedIdentifiableRepository<City> {
	
	public City findByNameAndCountryId(String name, Long countryId);
	public List<City> findAllByOrderByNameAsc();
	public List<City> findByCountryIdOrderByNameAsc(Long countryId);
}
