package com.bcp.coins.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//import com.pruebas.exceptions.JWTExceptions;

public class MyFilterJWT extends BasicAuthenticationFilter {

	public MyFilterJWT(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}
	public MyFilterJWT(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationManager, authenticationEntryPoint);
		// TODO Auto-generated constructor stub
	}

	public static final String AUTHORIZATION = "Authorization";
	
	@Autowired
	private JWTServiceAuth0 jwtService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(AUTHORIZATION);
		jwtService = new JWTServiceAuth0();
		if (authHeader!= null && !authHeader.startsWith("Basic "))
		{
			if (jwtService.isBearer(authHeader)) {
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				try {
					for (String role : jwtService.roles(authHeader)) 
						authorities.add(new SimpleGrantedAuthority(role));
					UsernamePasswordAuthenticationToken authentication;
					authentication = new UsernamePasswordAuthenticationToken(jwtService.user(authHeader), null, authorities);
					
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} catch (Exception e) {
					throw new JWTExceptions(e.getMessage());
				}
			}
			else
				throw new JWTExceptions("Authentication is not Basic or Bearer. ");
		}
		filterChain.doFilter(request, response);
	}

}
