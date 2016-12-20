package org.vhmml.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vhmml.repository.UserRepository;

@Service
public class VhmmlUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	public VhmmlUserDetailsService() {
		super();
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = userRepository.findByUsername(username);
		
		if(userDetails == null) {
			throw new UsernameNotFoundException("Authentication failed, incorrect username or password");
		}
		
		return userDetails;
	}
}
