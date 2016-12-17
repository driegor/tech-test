package com.company.mvc.exception;

import com.company.mvc.response.Response;

public abstract class HandlerException extends Exception {

	private static final long serialVersionUID = 1L;
	protected final String message;

	public HandlerException(String message) {
		super(message);
		this.message = message;
	}

	public HandlerException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public abstract Response getErrorResponse();
}
