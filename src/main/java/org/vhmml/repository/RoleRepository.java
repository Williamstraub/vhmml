package org.vhmml.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByName(String name);
}
