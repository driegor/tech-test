package com.company.mvc.security.auth;

import java.util.List;

public class Principal {

	public Principal(String userName, List<String> roles) {
		this.userName = userName;
		this.roles = roles;
	}

	List<String> roles;
	String userName;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
