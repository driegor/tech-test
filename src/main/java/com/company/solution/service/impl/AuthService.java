package com.company.solution.service.impl;

import com.company.mvc.security.SecurityUtils;
import com.company.mvc.security.SessionData;
import com.company.mvc.security.UserSession;
import com.company.mvc.security.UserSession.UserSessionBuilder;
import com.company.mvc.security.auth.AuthForm;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.auth.Principal;
import com.company.mvc.security.exception.AuthenticationException;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;

public class AuthService implements IAuthService {

	private static final long SESSION_TIMEOUT = 300000;
	private static volatile AuthService service;
	private IUserService userService;
	private SessionData sessionData = new SessionData();

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
	public void init(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public String login(AuthForm form) throws AuthenticationException {

		UserDTO user;
		try {
			user = userService.findByUserAndPassword(form.getUserName(), form.getPassword());
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
	public UserSession getUserSession(String sessionKey) throws AuthenticationException {
		return sessionData.getSession(sessionKey, SESSION_TIMEOUT);
	}

	private void addSession(String key, UserSession userSession) {
		sessionData.addSession(key, userSession);
	}

}
