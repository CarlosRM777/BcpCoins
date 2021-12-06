package com.bcp.coins.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.bcp.coins.model.ErrorMessage;
import com.fasterxml.jackson.databind.json.JsonMapper;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String DESCRIPTION = "Acceso No Autorizado al Recurso";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorMessage errormessage = new ErrorMessage(authException, DESCRIPTION);
        OutputStream out = response.getOutputStream();
        JsonMapper mapper = new JsonMapper();
        mapper.writeValue(out, errormessage);
        out.flush();
		
	}
}
