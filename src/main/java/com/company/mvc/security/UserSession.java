package com.company.mvc.security;

import com.company.mvc.security.auth.Principal;

public class UserSession {

	long creationTime;
	Principal principal;
	String key;

	public long getCreationTime() {
		return creationTime;
	}

	public Principal getPrincipal() {
		return principal;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}

	public static class UserSessionBuilder {

		private UserSession instance;

		private UserSessionBuilder() {
			instance = new UserSession();
		}

		public static UserSessionBuilder builder() {
			return new UserSessionBuilder();
		}

		public UserSessionBuilder key(String key) {
			instance.setKey(key);
			return this;
		}

		public UserSessionBuilder creationTime(long creationTime) {
			instance.setCreationTime(creationTime);
			return this;
		}

		public UserSessionBuilder principal(Principal principal) {
			instance.setPrincipal(principal);
			return this;
		}

		public UserSession build() {
			return instance;
		}
	}

}
