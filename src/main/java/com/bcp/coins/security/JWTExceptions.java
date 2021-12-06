package com.bcp.coins.security;

import org.springframework.security.core.AuthenticationException;

public class JWTExceptions extends AuthenticationException {
	private static final String DESCRIPTION = "JWT Exception TOKEN (410)";
	public JWTExceptions(String detail) {
		// TODO Auto-generated constructor stub
		super(DESCRIPTION + ". " + detail);
	}

}
