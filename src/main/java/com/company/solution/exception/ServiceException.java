package com.company.solution.exception;

import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class ServiceException extends HandlerException {

	private static final long serialVersionUID = 1L;

	HttpStatus status;

	public ServiceException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public ServiceException(Exception e) {
		this(e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	public ServiceException(String message) {
		this(message, HttpStatus.BAD_REQUEST);
	}

	@Override
	public Response getErrorResponse() {
		return Responses.custom(getMessage(), status, ContentType.APPLICATION_JSON);
	}
}
