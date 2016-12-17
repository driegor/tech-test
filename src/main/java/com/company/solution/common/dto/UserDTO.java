package com.company.solution.common.dto;

import java.util.List;

public class UserDTO {

	private String userName;
	private List<String> roles;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static class UserDTOBuilder {

		private UserDTO instance;

		private UserDTOBuilder() {
			instance = new UserDTO();
		}

		public static UserDTOBuilder builder() {
			return new UserDTOBuilder();
		}

		public UserDTOBuilder userName(String userName) {
			instance.setUserName(userName);
			return this;
		}

		public UserDTOBuilder roles(List<String> roles) {
			instance.setRoles(roles);
			return this;
		}

		public UserDTOBuilder password(String password) {
			instance.setPassword(password);
			return this;
		}

		public UserDTO build() {
			return instance;
		}
	}
}
