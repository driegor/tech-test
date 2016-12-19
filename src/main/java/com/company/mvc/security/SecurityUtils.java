package com.company.mvc.security;

import java.util.UUID;

import com.company.mvc.security.session.data.UserSession;
import com.company.solution.common.dto.GlobalConst;

public class SecurityUtils {

	// generate session key
	public static String createSessionKey() {
		return UUID.randomUUID().toString();
	}

	// check if user session has expired
	public static boolean isExpired(UserSession session, long timeOut) {
		long currentTime = System.currentTimeMillis();
		return currentTime > session.getCreationTime() + timeOut;
	}

	public static boolean skipAuthorize(String path) {

		if (path == null) {
			return Boolean.FALSE;
		}

		return GlobalConst.AUTHORIZE_URL.equals(path) || GlobalConst.LOGIN_URL.equals(path);

	}
}
