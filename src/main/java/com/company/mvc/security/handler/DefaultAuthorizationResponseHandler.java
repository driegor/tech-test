package com.company.mvc.security.handler;

import java.io.IOException;

import com.company.mvc.RequestUtils;
import com.company.mvc.response.Redirect;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.HttpExchange;

public class DefaultAuthorizationResponseHandler implements AuthorizationResponseHandler {

	@Override
	public void onFail(HttpExchange exchange) throws IOException {
		String sessionId = RequestUtils.getParameter(exchange, GlobalConst.JSESSION_ID);
		Redirect.redirect(exchange,
				String.format("%s?%s=%s", GlobalConst.FORBIDDEN, GlobalConst.JSESSION_ID, sessionId));
	}

}
