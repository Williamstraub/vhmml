package org.vhmml.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {	
	public Page<User> findAll(Pageable pageable);
	public Page<User> findByUsernameContains(String username, Pageable pageable);
	public Page<User> findByFirstNameStartsWith(String firstName, Pageable pageable);
	public Page<User> findByLastNameStartsWith(String lastName, Pageable pageable);
	public Page<User> findByInstitutionStartsWith(String institution, Pageable pageable);
	public User findByUsername(String username);	
	public List<User> findByEnabledTrueAndAccountLockedFalseAndCredentialsExpiredFalse();
	
	static final String FIND_BY_ROLE_QUERY = "select u from User u join u.roles r where r.name = ?1 order by u.username";
	
	@Query(FIND_BY_ROLE_QUERY)
	public List<User> findByRole(String role);
}
