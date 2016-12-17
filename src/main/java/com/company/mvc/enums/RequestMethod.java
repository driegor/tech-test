package com.company.mvc.enums;

public enum RequestMethod {
	GET, POST(Boolean.TRUE), PUT(Boolean.TRUE), DELETE;
	boolean reauestBody;

	private RequestMethod() {
		this.reauestBody = Boolean.FALSE;
	}

	private RequestMethod(boolean reauestBody) {
		this.reauestBody = reauestBody;
	}

	public boolean needsRequestBody() {
		return reauestBody;
	}
}
