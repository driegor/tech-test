package com.company.solution.service.impl;

import com.company.mvc.security.SecurityUtils;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.auth.Principal;
import com.company.mvc.security.exception.AuthenticationException;
import com.company.mvc.security.session.data.SessionData;
import com.company.mvc.security.session.data.UserSession;
import com.company.mvc.security.session.data.UserSession.UserSessionBuilder;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;

public class AuthService implements IAuthService {

	private static final long SESSION_TIMEOUT = 300000;
	private static volatile AuthService service;
	private IUserService userService;
	private SessionData sessionData;

	// private constructor
	private AuthService() {
	}

	// static method to get instance
	public static AuthService getInstance() {
		if (service == null) {
			synchronized (AuthService.class) {
				if (service == null) {
					service = new AuthService();
				}
			}
		}
		return service;
	}

	// I would use a DI framework to inject these beans
	public void init(IUserService userService, SessionData sessionData) {

		this.userService = userService;
		this.sessionData = sessionData;
	}

	@Override
	public String login(String userName, String password) throws AuthenticationException {

		UserDTO user;
		try {
			user = userService.findByUserAndPassword(userName, password);
		} catch (ServiceException e) {
			throw new AuthenticationException("Error retrieving user", e);
		}

		if (user == null) {
			throw new AuthenticationException("User doesn't exist");
		}

		String key = SecurityUtils.createSessionKey();
		UserSession userSession = UserSessionBuilder.builder()
				.principal(new Principal(user.getUserName(), user.getRoles())).creationTime(System.currentTimeMillis())
				.key(key).build();
		addSession(key, userSession);
		return key;

	}

	@Override
	public void logout(String sessionId) {
		sessionData.removeSession(sessionId);
	}

	@Override
	public UserSession getUserSession(String sessionKey) {
		return sessionData.getSession(sessionKey, SESSION_TIMEOUT);
	}

	private void addSession(String key, UserSession userSession) {
		sessionData.addSession(key, userSession);
	}

}
