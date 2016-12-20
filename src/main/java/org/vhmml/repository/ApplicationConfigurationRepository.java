package org.vhmml.repository;

import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.ConfigurationValue;

public interface ApplicationConfigurationRepository extends CrudRepository<ConfigurationValue, String> {

}
