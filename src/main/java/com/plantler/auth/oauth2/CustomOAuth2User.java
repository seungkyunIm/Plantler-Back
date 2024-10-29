package com.plantler.auth.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomOAuth2User implements OAuth2User {

	private String issuer;
	private String id;
	private String name;
	private String email;
	private int statusCode;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    public String getIssuer() {
    	return this.issuer;
    }
    
    public String getId() {
    	return this.id;
    }
    
    public String getEmail() {
    	return this.email;
    }

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
    
}
