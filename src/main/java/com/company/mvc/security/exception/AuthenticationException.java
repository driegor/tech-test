package com.company.mvc.security.exception;

public class AuthenticationException extends Exception {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String string, Exception e) {
		super(string, e);
	}
}
