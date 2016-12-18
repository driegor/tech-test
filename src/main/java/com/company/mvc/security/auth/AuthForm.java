package com.company.mvc.security.auth;

public class AuthForm {

	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public static class AuthFormBuilder {

		private AuthForm instance;

		private AuthFormBuilder() {
			instance = new AuthForm();
		}

		public static AuthFormBuilder builder() {
			return new AuthFormBuilder();
		}

		public AuthFormBuilder userName(String userName) {
			instance.setUserName(userName);
			return this;
		}

		public AuthFormBuilder password(String password) {
			instance.setPassword(password);
			return this;
		}

		public AuthForm build() {
			return instance;
		}
	}

}
