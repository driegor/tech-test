package com.company.solution.domain;

import java.util.List;

public class User {

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

	public static class UserBuilder {

		private User instance;

		private UserBuilder() {
			instance = new User();
		}

		public static UserBuilder builder() {
			return new UserBuilder();
		}

		public UserBuilder userName(String userName) {
			instance.setUserName(userName);
			return this;
		}

		public UserBuilder roles(List<String> roles) {
			instance.setRoles(roles);
			return this;
		}

		public UserBuilder password(String password) {
			instance.setPassword(password);
			return this;
		}

		public User build() {
			return instance;
		}
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", roles=" + roles + "]";
	}

}
