package com.company.mvc.exception;

import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class MappingNotFoundException extends HandlerException {

	private static final long serialVersionUID = 1L;

	public MappingNotFoundException(String message) {
		super(message);
	}

	@Override
	public Response getErrorResponse() {
		return Responses.notFound(message);
	}
}
