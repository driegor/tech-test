package com.company.mvc.exception;

import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class MappingException extends HandlerException {

	private static final long serialVersionUID = 1L;

	public MappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MappingException(String message) {
		super(message);
	}

	@Override
	public Response getErrorResponse() {
		return Responses.internalError(message);
	}

}
