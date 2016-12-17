package com.company.mvc.enums;

public enum HttpStatus {
	OK(200), CREATED(201), UNAUTHORIZED(401), NOT_FOUND(404), BAD_REQUEST(400), INTERNAL_SERVER_ERROR(500);

	private final int value;

	private HttpStatus(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
