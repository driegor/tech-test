
package com.company.mvc.response;

import com.company.common.utils.JsonUtils;
import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;

public final class Responses {

	private Responses() {
		// private constructor to hide implicit public one
	}

	// 200 OK
	public static <T> Response success(T content) {
		return new Response(contentToString(content, ContentType.TEXT_HTML), HttpStatus.OK, ContentType.TEXT_HTML);
	}

	// 200 OK
	public static <T> Response success(T content, ContentType contentType) {
		return new Response(contentToString(content, contentType), HttpStatus.OK, contentType);
	}

	// 201 CREATED
	public static <T> Response created(T content, ContentType contentType) {
		return new Response(contentToString(content, contentType), HttpStatus.CREATED, contentType);
	}

	// 401 UNAUTHORIZED
	public static Response unAuthorized(String content) {
		return new Response(content, HttpStatus.UNAUTHORIZED, ContentType.TEXT_HTML);
	}

	// 404 NOT_FOUND
	public static Response notFound(String content) {
		return new Response(content, HttpStatus.NOT_FOUND, ContentType.TEXT_HTML);
	}

	// 400 BAD_REQUEST
	public static Response badRequest(String content) {
		return new Response(content, HttpStatus.BAD_REQUEST, ContentType.TEXT_HTML);
	}

	// 500 INTERNAL_SERVER_ERROR
	public static Response internalError(String content) {
		return new Response(content, HttpStatus.INTERNAL_SERVER_ERROR, ContentType.TEXT_HTML);
	}

	public static <T> String contentToString(T content, ContentType contentType) {

		if (ContentType.APPLICATION_JSON.equals(contentType)) {
			return JsonUtils.writeToJson(content);
		}
		return String.valueOf(content);
	}
}