package com.company.mvc.response;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class Redirect {

	public static void redirect(HttpExchange exchange, String loginURL) throws IOException {
		exchange.getResponseHeaders().set("Location", loginURL);
		exchange.sendResponseHeaders(301, -1);
	}

}
