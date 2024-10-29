package com.plantler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.plantler.auth.JwtToken;
import com.plantler.dto.RequestTokenDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class TokenComponent {

	private final JwtToken jwtToken;
	
	@Value("${access.auth.header.key}")
	private String key;
	
	public String getUserID(HttpServletRequest req) {
		String token =  req.getHeader(key);
		if(jwtToken.isValidToken(token)) {
			RequestTokenDTO requestTokenDTO = jwtToken.getUser(token);
			return requestTokenDTO.getId();
		}
		return null;
	}
	
}
