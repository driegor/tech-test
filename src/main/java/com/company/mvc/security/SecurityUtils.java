package com.company.mvc.security;

import java.util.UUID;

import com.company.solution.common.dto.GlobalConst;

public class SecurityUtils {

	// generate session key
	public static String createSessionKey() {
		return UUID.randomUUID().toString();
	}

	public static boolean skipAuthorize(String path) {

		if (path == null) {
			return Boolean.FALSE;
		}
		return GlobalConst.AUTHORIZE_URL.equals(path) || GlobalConst.LOGIN_URL.equals(path);
	}
}
