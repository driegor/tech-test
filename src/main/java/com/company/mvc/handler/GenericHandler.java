package com.company.mvc.handler;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.company.mvc.mapping.MappingData;
import com.company.mvc.mapping.MappingProcessor;
import com.company.mvc.mapping.MappingResolver;
import com.company.mvc.response.Response;
import com.company.mvc.response.ResponseWriter;
import com.company.mvc.security.SecurityUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GenericHandler implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(GenericHandler.class);

	protected String rootMapping;

	private MappingResolver mappingResolver;

	private MappingProcessor mappingProcessor;

	private ResponseWriter responseWriter;

	public GenericHandler() {
		this.mappingResolver = getMappingResolver();
		this.mappingProcessor = getMappingProcessor();
		this.responseWriter = getResponseWriter();
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

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {

			// get controller method to invoke
			MappingData mappingData = mappingResolver.resolveMapping(rootMapping, exchange, this);
			// invoke controller method with args
			boolean useSession = useSession() && !SecurityUtils.skipAuthorize(mappingData.getPath());
			Response response = mappingProcessor.process(mappingData, exchange, useSession, this);
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
