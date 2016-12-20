package com.company.mvc.response;

import java.io.IOException;

import com.company.mvc.enums.HttpStatus;
import com.sun.net.httpserver.HttpExchange;

public class Redirect {

	public static void redirect(HttpExchange exchange, String targetUrl) throws IOException {
		innerRedirect(exchange, targetUrl, HttpStatus.PERM_REDIRECT);
	}

	private static void innerRedirect(HttpExchange exchange, String targetUrl, HttpStatus status) throws IOException {
		exchange.getResponseHeaders().set("Location", targetUrl);
		exchange.sendResponseHeaders(status.value(), -1);
	}

}
