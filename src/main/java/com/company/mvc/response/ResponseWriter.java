package com.company.mvc.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import com.company.mvc.exception.ResponseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ResponseWriter {
	static final String RESPONSE_ERROR = "Error ['%s'] writting response";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final Logger LOGGER = Logger.getLogger(ResponseWriter.class.getName());

	// Write response object to exchange outputStream
	public void write(HttpExchange exchange, Response response) throws ResponseException {
		OutputStream os = null;
		try {
			// if the response is a redirection, just redirect to that page
			if (response.isRedirect()) {
				Redirect.redirect(exchange, response.getContent());
				return;
			}

			// get output stream from httpExchange
			os = exchange.getResponseBody();
			Headers headers = exchange.getResponseHeaders();

			headers.add(CONTENT_TYPE, response.getContentType().value());
			exchange.sendResponseHeaders(response.getStatus().value(), response.getContentLength());
			os.write(response.getContentBytes());
		} catch (Exception e) {
			throw new ResponseException(String.format(RESPONSE_ERROR, e.getMessage()), e);
		} finally {
			try {
				// always try to close stream
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				LOGGER.severe("Error closing stream.[" + e + "]");
			}
		}
	}
}
