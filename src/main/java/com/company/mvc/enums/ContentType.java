package com.company.mvc.enums;

public enum ContentType {

	APPLICATION_JSON("application/json"), TEXT_HTML("text/html");

	private final String value;

	private ContentType(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}