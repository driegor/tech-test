package com.company.mvc.handler;

import java.io.IOException;
import java.util.logging.Logger;

import com.company.mvc.mapping.MappingData;
import com.company.mvc.mapping.MappingHandler;
import com.company.mvc.mapping.MappingProcessor;
import com.company.mvc.response.Response;
import com.company.mvc.response.ResponseWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GenericHandler implements HttpHandler {

	private static final Logger LOGGER = Logger.getLogger(GenericHandler.class.getName());

	protected String rootMapping;
	private MappingHandler mappingHandler = new MappingHandler();
	private MappingProcessor mappingProcessor = new MappingProcessor();
	private ResponseWriter responseWriter = new ResponseWriter();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			// get controller method to invoke
			MappingData mappingData = mappingHandler.getMappingData(rootMapping, exchange, this);
			// invoke controller method with args
			Response response = mappingProcessor.process(mappingData, this);
			// write response
			responseWriter.write(exchange, response);

		} catch (Exception e) {
			// handle exception
			LOGGER.severe(e.getMessage());
		}
	}

}
