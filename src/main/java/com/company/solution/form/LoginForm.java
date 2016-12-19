package com.company.solution.form;

public class LoginForm {

	private String userName;
	private String password;
	private String postLogin;

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

	public void setPostLogin(String postLogin) {
		this.postLogin = postLogin;
	}

	public String getPostLogin() {
		return postLogin;
	}

	public static class LoginFormBuilder {

		private LoginForm instance;

		private LoginFormBuilder() {
			instance = new LoginForm();
		}

		public static LoginFormBuilder builder() {
			return new LoginFormBuilder();
		}

		public LoginFormBuilder userName(String userName) {
			instance.setUserName(userName);
			return this;
		}

		public LoginFormBuilder password(String password) {
			instance.setPassword(password);
			return this;
		}

		public LoginFormBuilder postLogin(String postLogin) {
			instance.setPostLogin(postLogin);
			return this;
		}

		public LoginForm build() {
			return instance;
		}
	}

}
