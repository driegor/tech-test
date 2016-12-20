package com.company.mvc.security.session.data;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.company.solution.common.dto.GlobalConst;
import com.company.solution.server.Server;

public class SessionData {

	private static final Logger LOGGER = Logger.getLogger(Server.class);

	private static final long FIXED_DELAY = 100;
	// data is a concurrent hash map, we don't need to synchronize anything
	private final Map<String, UserSession> data = new ConcurrentHashMap<>();

	public SessionData() {
		Timer timer;
		timer = new Timer();

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				LOGGER.info("Removing expired sessions...");
				clearExpiredSessions();
			}

			private void clearExpiredSessions() {

				data.entrySet().removeIf(entry -> isExpired(entry.getValue().getLastAccessTime()));
			}

			private boolean isExpired(long lastAccessTime) {
				long currentTime = System.currentTimeMillis();
				return currentTime > lastAccessTime + GlobalConst.SESSION_TIMEOUT;
			}

		};
		timer.schedule(task, FIXED_DELAY, GlobalConst.SESSION_TIMEOUT - FIXED_DELAY);
	}

	public void addSession(String key, UserSession session) {
		data.put(key, session);
	}

	public UserSession getSession(String sessionKey) {

		UserSession session = data.get(sessionKey);

		if (session == null) {
			return null;
		}
		session.setLastAccessTime(System.currentTimeMillis());
		data.put(sessionKey, session);
		return session;
	}

	public void removeSession(String sessionId) {
		data.remove(sessionId);
	}

}
