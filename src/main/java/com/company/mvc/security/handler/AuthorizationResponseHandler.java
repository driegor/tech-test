package com.company.mvc.security.handler;

import com.sun.net.httpserver.HttpExchange;

public interface AuthorizationResponseHandler {

	void onFail(HttpExchange exchange) throws Exception;

}
