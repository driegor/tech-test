package com.company.mvc.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.company.mvc.mapping.MappingData;
import com.company.mvc.mapping.MappingProcessor;
import com.company.mvc.mapping.MappingResolver;
import com.company.mvc.response.Response;
import com.company.mvc.response.ResponseWriter;
import com.company.mvc.security.SecurityUtils;
import com.company.mvc.security.handler.AuthorizationResponseHandler;
import com.company.mvc.security.handler.DefaultAuthorizationResponseHandler;
import com.company.mvc.security.permission.DefaultPermissionEvaluator;
import com.company.mvc.security.permission.PermissionEvaluator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GenericHandler implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(GenericHandler.class);

	protected String rootMapping;

	private MappingResolver mappingResolver;

	private MappingProcessor mappingProcessor;

	private ResponseWriter responseWriter;

	private PermissionEvaluator permissionEvaluator;

	private AuthorizationResponseHandler authorizationResponseHandler;

	public GenericHandler() {
		this.mappingResolver = getMappingResolver();
		this.mappingProcessor = getMappingProcessor();
		this.responseWriter = getResponseWriter();
		this.permissionEvaluator = getPermissionEvaluator();
		this.authorizationResponseHandler = getAuthorizationResponseHandler();
	}

	protected PermissionEvaluator getPermissionEvaluator() {
		return new DefaultPermissionEvaluator();
	}

	protected MappingResolver getMappingResolver() {
		return new MappingResolver();
	}

	protected MappingProcessor getMappingProcessor() {
		return new MappingProcessor();
	}

	protected ResponseWriter getResponseWriter() {
		return new ResponseWriter();
	}

	protected AuthorizationResponseHandler getAuthorizationResponseHandler() {
		return new DefaultAuthorizationResponseHandler();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {

			// get information from method to invoke
			MappingData mappingData = mappingResolver.resolveMapping(rootMapping, exchange, this);

			// check permission
			if (!permissionEvaluator.hasPermission(mappingData.getPreAuthorize(), exchange,
					mappingData.getBindValue())) {
				authorizationResponseHandler.onFail(exchange);
				return;
			}
			// we dont need propagate session in api controllers and in
			// login/authorize methods
			boolean propagateSession = useSession() && !SecurityUtils.skipAuthorize(mappingData.getPath());

			// invoke controller method with args
			Response response = mappingProcessor.process(mappingData, exchange, propagateSession, this);
			// write response
			responseWriter.write(exchange, response);

		} catch (Exception e) {
			// handle exception
			LOGGER.error(e.getMessage());
		}
	}

	public boolean useSession() {
		return Boolean.FALSE;
	}

}
