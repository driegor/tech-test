package com.company.mvc.security.auth;

import com.company.mvc.security.UserSession;
import com.company.mvc.security.exception.AuthenticationException;

public interface IAuthService {

	String login(AuthForm form) throws AuthenticationException;

	UserSession getUserSession(String sessionKey) throws AuthenticationException;
}
