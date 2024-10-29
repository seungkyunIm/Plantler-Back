package com.plantler.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.plantler.dto.UserDTO;

import lombok.ToString;

@ToString
public class MyUserDTO implements UserDetails {

	private UserDTO user;
	
	public MyUserDTO(UserDTO user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return user.getUser_pwd();
	}

	@Override
	public String getUsername() {
		return user.getUser_nick();
	}
	
	public String getEmail() {
		return user.getUser_email();
	}
	
	public int getNo() {
		return user.getUser_no();
	}
	
	public String getId() {
		return user.getUser_id();
	}

}
