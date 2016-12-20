package org.vhmml.repository;

import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
	public void deleteByUserIdAndRoleId(Long userId, Long roleId);
}
