package org.vhmml.service;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vhmml.dto.UserSearchResult;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.entity.UserRole;
import org.vhmml.exception.AuthenticationFailedException;
import org.vhmml.exception.UserDisabledException;
import org.vhmml.exception.UserExistsException;
import org.vhmml.repository.RoleRepository;
import org.vhmml.repository.UserRepository;
import org.vhmml.repository.UserRoleRepository;
import org.vhmml.service.ApplicationConfigService.Property;

import com.google.common.collect.Lists;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ApplicationConfigService configService;	
	
	public User getUser(Long id) {
		return userRepository.findOne(id);
	}
	
	public UserSearchResult search(SearchType searchType, String searchText, Pageable pageable) {
		Page<User> users = null;
		
		if(StringUtils.isEmpty(searchText)) {
			users = userRepository.findAll(pageable);			
		} else {
			if(searchType == null) {
				searchType = SearchType.EMAIL;
			}
			
			switch(searchType) {
				case EMAIL:
					users = userRepository.findByUsernameContains(searchText, pageable);
					break;
				case FIRST_NAME:
					users = userRepository.findByFirstNameStartsWith(searchText, pageable);
					break;
				case LAST_NAME:
					users = userRepository.findByLastNameStartsWith(searchText, pageable);
					break;
				case INSTITUTION:
					users = userRepository.findByInstitutionStartsWith(searchText, pageable);
					break;
			}			
		}
		
		return new UserSearchResult(users);
	}
	
	public User findById(Long id) {
		return userRepository.findOne(id);
	}
	
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	public List<User> findByRole(Role.Name role) {
		return userRepository.findByRole(role.getName());
	}
	
	@Transactional
	public User createUser(User user, List<Role.Name> roleNames) throws UserExistsException {
		
		if(userRepository.findByUsername(user.getUsername()) != null) {
			throw new UserExistsException("The email address entered is already in use");
		}
		
		boolean newUser = StringUtils.isEmpty(user.getPassword());
		
		if(newUser) {
			user.setEnabled(false);
			user.setRegisterTime(new Date());			
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setEnabled(true);
		}

		user = userRepository.save(user);
		
		if(newUser) {
			sendRegistrationNotificationEmail(user);
			sendRegistrationConfirmationEmail(user);
		}
		
		if(!CollectionUtils.isEmpty(roleNames)) {	
			Map<Role.Name, Role> roleMap = getRoles();
			
			for(Role.Name roleName : roleNames) {
				Role role = roleMap.get(roleName);
				userRoleRepository.save(new UserRole(user.getId(), role.getId()));
			}
		}		
		
		return user;
	}
	
	private void sendRegistrationConfirmationEmail(User user) {		
		String message = "Dear " + user.getFirstName()  + ",<br/><br/>";
		message += "Thank you for registering at vHMML.org!<br/><br/>";
		message += "Because of HMML's agreements with the owning libraries, all applications for access to Reading Room must be reviewed by a HMML librarian. We will do this as quickly as possible, and you should receive a response within one business day (weekends and public holidays are excluded).<br/><br/>";
		message += configService.getValue(Property.VHMML_EMAIL_SIGNATURE);
		
		emailService.sendMessages(Lists.newArrayList(user.getUsername()), "vHMML Registration", message);
	}
	
	private void sendRegistrationNotificationEmail(User user) {
		List<String> systemsLibrarianEmailList = configService.getValues(Property.SYSTEMS_LIB_EMAIL_LIST);		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String message = "Dear vHMML systems librarian,<br/><br/>There is a request for a new user registration, ";
		InetAddress serverIP;
		try {
			serverIP = InetAddress.getLocalHost();
			message += "on server: " + serverIP +"<br/><br/>";
		} catch (UnknownHostException e) {
			LOG.error("UnknownHostException while trying to get local server IP address", e);
		}			
	
		message += " Please check the vHMML User Admin page for:<br/><br/>" + user.getFirstName() + " " + user.getLastName() + "<br/>" + user.getUsername() + "<br/>" + formatter.format(user.getRegisterTime()) + "<br/><br/>";
		message += configService.getValue(Property.VHMML_EMAIL_SIGNATURE);
		emailService.sendMessages(systemsLibrarianEmailList, "vHMML Registration", message);
	}
	
	public void sendMessageToActiveUsers(String subject, String message) {
		
		List<String> emailAddresses = new ArrayList<>();
		List<User> activeUsers = userRepository.findByEnabledTrueAndAccountLockedFalseAndCredentialsExpiredFalse();
		
		for(User user : activeUsers) {
			if(!"admin".equals(user.getUsername())) {
				emailAddresses.add(user.getUsername());
			}			
		}		
		
		emailService.sendMessages(emailAddresses, subject, message);
	}
	
	public void sendTemporaryPassword(String username) throws UserDisabledException {
		User user = findByUsername(username);
		
		if(user == null) {
			LOG.warn("Attempt to send temporary password to an email address for which there is no registered user");
			throw new UsernameNotFoundException("There is no user registered with the email address " + username);
		}
		
		if(!user.isEnabled()) {
			throw new UserDisabledException("Your account has not yet been approved, therefore you cannot yet change your password. Please wait for your registration acceptance email (up to 72 hours)");
		}
		
		String password = generatePassword();
		user.setPassword(passwordEncoder.encode(password));
		user.setCredentialsExpired(true);
		userRepository.save(user);
		
		String message = "Dear " + user.getFirstName() + ",<br/><br/>";
		message += "According to our records you recently requested that your vHMML password be reset using our \"forgot password\" feature. Here is your temporary password:<br/><br/>";
		message += password + "<br/><br/>";
		message += "Upon successful authentication you will be required to immediately change your password.<br/><br/>";
		message += "If you did not make this request, please contact the vHMML support using one of the following methods:<br/><br/>";
		message += "Email: help@vhmml.org<br/>";
		message += "Phone: 320-363-3514<br/><br/>";
		message += configService.getValue(Property.VHMML_EMAIL_SIGNATURE);
		emailService.sendMessage(username, "vHMML password reset request", message);
		
		List<String> systemsLibrarianEmailList = configService.getValues(Property.SYSTEMS_LIB_EMAIL_LIST);
		message = "The vHMML user registered with the email address " + username + " has recently requested a password reset using the \"forgot password\" feature.";
		emailService.sendMessages(systemsLibrarianEmailList, "vHMML password reset request", message);
	}
	
	@Transactional
	public void enableUser(Long userId) {
		
		User user = userRepository.findOne(userId);
		
		if(user == null) {
			throw new UsernameNotFoundException("User activation failed, no user found for id " + userId);
		}
		
		boolean generatePassword = user.getPassword() == null;
		
		try {
			if(generatePassword) {
				String password = generatePassword();
				user.setPassword(passwordEncoder.encode(password));
				user.setCredentialsExpired(true);
				String message = "Dear " + user.getFirstName() + ",<br/><br/>"
		                + "Your vHMML registration has been activated. To login visit "
		                + "http://vhmml.org/login and sign in using the email "
		                + "address you registered with and the following temporary password:<br/><br/>"
		                + password + "<br/><br/>"
		                + "Upon successfully signing in you will be required to change your password.<br/><br/>"
						+ configService.getValue(Property.VHMML_EMAIL_SIGNATURE);
				emailService.sendMessage(user.getUsername(), "Your vHMML registration has been approved.", message);						       
			}
			
			user.setEnabled(true);
			userRepository.save(user);
		} catch(Exception e) {
			String message = "Exception activating user for user id " + userId + ". user has not been activated";
			LOG.error(message, e);
			throw new RuntimeException(message, e);
		}		
	}
	
	@Transactional
	public void disableUser(Long userId) {
		User user = userRepository.findOne(userId);
		
		if(user == null) {
			throw new UsernameNotFoundException("User de-activation failed, no user found for id " + userId);
		}
		
		user.setEnabled(false);
		userRepository.save(user);
	}
	
	@Transactional
	public void removeUser(Long userId) {
		User user = userRepository.findOne(userId);
		
		if(user == null) {
			throw new UsernameNotFoundException("User removal failed, no user found for id " + userId);
		}
		
		user.setRemoved(true);
		userRepository.save(user);
	}
	
	@Transactional
	public void acceptReadingRoomAgreement(Long userId) {
		User user = userRepository.findOne(userId);
		
		if(user == null) {
			throw new UsernameNotFoundException("Unable to accept Reading Room agreement, no user found for id " + userId);
		}
		
		user.setUsageAgreementAccepted(true);
		user.setUsageAgreementAcceptedDate(new Date());
		userRepository.save(user);
	}
	
	@Transactional
	public void grantRole(Long userId, Role.Name roleName) {		
		Role role = roleRepository.findByName(roleName.getName());
		UserRole userRole = new UserRole(userId, role.getId());
		userRoleRepository.save(userRole);
	}
	
	@Transactional
	public void revokeRole(Long userId, Role.Name roleName) {		
		Role role = roleRepository.findByName(roleName.getName());
		userRoleRepository.deleteByUserIdAndRoleId(userId, role.getId());
	}
	
	@Transactional
	public User saveUser(User user) throws UserExistsException {
		User dbUser = userRepository.findOne(user.getId());
		
		if(!user.getUsername().equals(dbUser.getUsername())) {
			if(userRepository.findByUsername(user.getUsername()) != null) {
				throw new UserExistsException("The email address entered is already in use");
			}
		}
		
		org.springframework.beans.BeanUtils.copyProperties(user, dbUser, "password", "registerTime", "firstLogin", "lastLogin", "roles");
		return userRepository.save(dbUser);
	}
	
	public Map<Role.Name, Role> getRoles() {
		Map<Role.Name, Role> roleMap = new HashMap<Role.Name, Role>();
		Iterable<Role> roles = roleRepository.findAll();
		
		for(Role role : roles) {
			roleMap.put(Role.Name.valueOf(role.getName()), role);
		}
		
		return roleMap;
	}
	
	public void changePassword(String username, String currentPassword, String newPassword) throws AuthenticationFailedException {
		LOG.info("changing password for user " + username);
		
		User user = findByUsername(username);		
		
		if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new AuthenticationFailedException("Incorrect password");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setCredentialsExpired(false);
		userRepository.save(user);
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	private String generatePassword() {
		return RandomStringUtils.random(4, "abcdefghjkmnpqrstuvwxyz") + RandomStringUtils.random(2, "ABCDEFGHJKMNPQRSTUVWXYZ") + RandomStringUtils.random(2, "23456789");
	}	
}
