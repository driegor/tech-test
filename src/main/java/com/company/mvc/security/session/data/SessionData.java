package com.company.mvc.security.session.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.company.mvc.security.SecurityUtils;

public class SessionData {

	// data is a concurrent hash map, we don't need to synchronize anything
	private final Map<String, UserSession> data = new ConcurrentHashMap<>();

	public void addSession(String key, UserSession session) {
		data.put(key, session);
	}

	public UserSession getSession(String sessionKey, long timeOut) {

		UserSession session = data.get(sessionKey);

		if (session == null || SecurityUtils.isExpired(session, timeOut)) {
			// if session is null or has expired we return null
			return null;
		}
		return session;
	}

	public void removeSession(String sessionId) {
		data.remove(sessionId);
	}

}
