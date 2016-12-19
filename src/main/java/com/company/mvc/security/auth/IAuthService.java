package com.company.mvc.security.auth;

import com.company.mvc.security.exception.AuthenticationException;
import com.company.mvc.security.session.data.UserSession;

public interface IAuthService {

	String login(String userName, String password) throws AuthenticationException;

	void logout(String sessionId);

	UserSession getUserSession(String sessionKey);
}
