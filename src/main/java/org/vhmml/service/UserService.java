package org.vhmml.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vhmml.dto.UserSearchResult;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.exception.AuthenticationFailedException;
import org.vhmml.exception.UserDisabledException;
import org.vhmml.exception.UserExistsException;

public interface UserService {
	
	public enum SearchType {
		LAST_NAME,
		FIRST_NAME,
		EMAIL,
		INSTITUTION
	}
	
	public User findById(Long id);
	public User findByUsername(String username);
	public List<User> findByRole(Role.Name roleName);
	public Page<User> findAll(Pageable pageable);
	public User getUser(Long id);	
	public User createUser(User user, List<Role.Name> roleNames) throws UserExistsException;
	public User saveUser(User user) throws UserExistsException;
	public UserSearchResult search(SearchType searchType, String searchText, Pageable pageable);
	public void changePassword(String username, String currentPassword, String newPassword) throws AuthenticationFailedException;
	public void enableUser(Long userId);
	public void disableUser(Long userId);
	public void removeUser(Long userId);
	public void grantRole(Long userId, Role.Name roleName);	
	public void revokeRole(Long userId, Role.Name roleName);	
	public void sendTemporaryPassword(String username) throws UserDisabledException;
	public void acceptReadingRoomAgreement(Long userId);
	public void sendMessageToActiveUsers(String subject, String message);
}
