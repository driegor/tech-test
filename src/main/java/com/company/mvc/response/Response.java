package com.company.mvc.response;

import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;

public class Response {

	private ContentType contentType;
	private HttpStatus status;
	private String content;
	private boolean redirect;

	public Response(String content, HttpStatus status, ContentType contentType, boolean redirect) {
		this.content = content;
		this.status = status;
		this.contentType = contentType;
		this.redirect = redirect;
	}

	public Response(String content, HttpStatus status, ContentType contentType) {
		this(content, status, contentType, Boolean.FALSE);
	}

	public String getContent() {
		return content;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public byte[] getContentBytes() {
		return content.getBytes();
	}

	public int getContentLength() {
		return content.getBytes().length;
	}

	public boolean isRedirect() {
		return redirect;
	}
}
