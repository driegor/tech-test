package com.company.mvc.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

import com.company.mvc.security.auth.AuthForm;
import com.company.mvc.security.auth.AuthForm.AuthFormBuilder;

public class SecurityUtils {

	public static AuthForm getAuthForm(String data) throws UnsupportedEncodingException {

		String[] pairs = data.split("\\&");
		String[] fields = pairs[0].split("=");
		String name = URLDecoder.decode(fields[1], "UTF-8");
		fields = pairs[1].split("=");
		String password = URLDecoder.decode(fields[1], "UTF-8");
		return AuthFormBuilder.builder().userName(name).password(password).build();
	}

	// generate session key
	public static String createSessionKey() {
		return UUID.randomUUID().toString();
	}

	// check if user session has expired
	public static boolean isExpired(UserSession session, long timeOut) {
		long currentTime = System.currentTimeMillis();
		return currentTime > session.getCreationTime() + timeOut;
	}
}
