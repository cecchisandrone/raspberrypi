package com.github.cecchisandrone.smarthome.service.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.cecchisandrone.smarthome.service.configuration.ConfigurationService;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	private static final String ADMIN_USERNAME = "admin";

	@Autowired
	private ConfigurationService configurationService;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		if (!username.equals(ADMIN_USERNAME)) {
			throw new UsernameNotFoundException(username + " not found");
		}

		final String password = configurationService.getConfiguration().getProfile().getPassword();
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new User(ADMIN_USERNAME, password, authorities);
	}
}