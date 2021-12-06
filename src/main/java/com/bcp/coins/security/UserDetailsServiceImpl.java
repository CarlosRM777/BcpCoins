package com.bcp.coins.security;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	String username;
	String password;

	private User userBuilder(String username, String password, String... roles) {
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_"+ role));
		}
		return new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username.equals("user")) 
			return userBuilder(username, new BCryptPasswordEncoder().encode("123456"), "USER");
		if (username.equals("manager")) 
			return userBuilder(username, new BCryptPasswordEncoder().encode("123456"), "MANAGER");
		if (username.equals("admin")) 
			return userBuilder(username, new BCryptPasswordEncoder().encode("123456"), "USER", "MANAGER", "ADMIN");
		throw new UsernameNotFoundException("Usuario No Encontrado");
	}

}
