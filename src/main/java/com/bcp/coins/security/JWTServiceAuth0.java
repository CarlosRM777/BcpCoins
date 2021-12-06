package com.bcp.coins.security;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
//import com.pruebas.exceptions.JWTExceptions;

@Service
public class JWTServiceAuth0 {
	private static final String USER = "user";
	private static final String ROLES = "roles";
	private static final String ISSUER = "miw-spring5";
	private static final int EXPIRE_IN_MILISECONDS = 3600000;
	private static final String SECRET = "clave-secreta-test";
	private static final String BEARER = "Bearer ";

	public String createToken(String user, List<String> roles) throws Exception {
		try {
			return JWT.create() 
					.withIssuer(ISSUER)
					.withIssuedAt(new Date())
					.withNotBefore(new Date())
					.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRE_IN_MILISECONDS))
					.withClaim(USER, user)
					.withArrayClaim(ROLES, roles.toArray(new String[0]))
					.sign(Algorithm.HMAC256(SECRET));
		} catch (Exception exception) {
			throw new JWTExceptions("JWT could not Create Token. " + exception.getMessage());
		}
	}
	
	public Boolean isBearer(String authorization) {
		return authorization != null && authorization.startsWith(BEARER) && authorization.split("\\.").length == 3;
	}
	
	public String user(String authorization) throws Exception {
		String user = this.verify(authorization).getClaim(USER).toString();
		System.out.println("USER: " + user);
		return user;
	}
	
	private DecodedJWT verify(String authorization) throws Exception {
		if (!this.isBearer(authorization)) {
			throw new Exception("It is not Bearer");
		}
		try {
			return JWT.require(Algorithm.HMAC256(SECRET))
					.withIssuer(ISSUER).build()
					.verify(authorization.substring(BEARER.length()));
		} catch (Exception exception) {
			throw new JWTExceptions("JWT is wrong. " + exception.getMessage());
		}
	}
	public List<String> roles(String authorization) throws Exception {
		List<String> roles= Arrays.asList(this.verify(authorization).getClaim(ROLES).asArray(String.class));
		System.out.println("ROLES: " + roles.toString());		
		return roles;
	}
	
	public JWTServiceAuth0() {
		// TODO Auto-generated constructor stub
	}

}
