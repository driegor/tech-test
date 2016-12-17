package com.company.mvc.exception;

import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class BadRequestException extends HandlerException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public Response getErrorResponse() {
		return Responses.badRequest(message);
	}

}
