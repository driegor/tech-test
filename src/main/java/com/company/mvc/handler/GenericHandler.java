package com.company.mvc.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.company.mvc.mapping.MappingData;
import com.company.mvc.mapping.MappingHandler;
import com.company.mvc.mapping.MappingProcessor;
import com.company.mvc.response.Response;
import com.company.mvc.response.ResponseWriter;
import com.company.mvc.security.UserSession;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.exception.AuthenticationException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GenericHandler implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(GenericHandler.class);

	public static final Object JSESSION_ID = "JSESSION_ID";

	protected String rootMapping;

	protected IAuthService authService;

	public GenericHandler(IAuthService authService) {
		this.authService = authService;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			// get controller method to invoke
			MappingData mappingData = new MappingHandler().getMappingData(rootMapping, exchange, this);
			// invoke controller method with args
			Response response = new MappingProcessor().process(mappingData, this);
			// write response
			new ResponseWriter().write(exchange, response);

		} catch (Exception e) {
			// handle exception
			LOGGER.error(e.getMessage());
		}
	}

	public boolean useSession() {
		return Boolean.FALSE;
	}

	public UserSession getUserSession(String sessionId) throws AuthenticationException {
		return authService.getUserSession(sessionId);
	}

}
