package com.company.mvc.security.session.data;

import com.company.mvc.security.auth.Principal;

public class UserSession {

	long lastAccessTime;
	Principal principal;
	String key;

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
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

		public UserSessionBuilder principal(Principal principal) {
			instance.setPrincipal(principal);
			return this;
		}

		public UserSessionBuilder lastAccessTime(long lastAccessTime) {
			instance.setLastAccessTime(lastAccessTime);
			return this;
		}

		public UserSession build() {
			return instance;
		}

	}

}
