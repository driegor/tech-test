package com.company.mvc.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.company.mvc.security.exception.AuthenticationException;

public class SessionData {
	private static final String NOT_VALID_SESSION_KEY = "Not valid user key [%s]";

	// data is a concurrent hash map, we don't need to synchronize anything
	private final Map<String, UserSession> data = new ConcurrentHashMap<>();

	public void addSession(String key, UserSession session) {
		data.put(key, session);
	}

	public UserSession getSession(String sessionKey, long timeOut) throws AuthenticationException {

		UserSession session = data.get(sessionKey);

		// session key doesn't exist in data structure
		if (session == null) {
			throw new AuthenticationException(String.format(NOT_VALID_SESSION_KEY, sessionKey));
		}
		// session key has expired
		if (SecurityUtils.isExpired(session, timeOut)) {
			throw new AuthenticationException(String.format(NOT_VALID_SESSION_KEY, sessionKey));
		}
		return session;
	}

}
