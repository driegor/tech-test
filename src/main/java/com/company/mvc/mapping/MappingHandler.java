package com.company.mvc.mapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.company.common.utils.CoreUtils;
import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.exception.MappingException;
import com.company.mvc.exception.MappingNotFoundException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.mapping.MappingData.MappingDataBuilder;
import com.sun.net.httpserver.HttpExchange;

public class MappingHandler {

	private static final String MAPPING_NOT_FOUND = "Not mapping found for path ['%s'] and method ['%s']";
	private static final String INVALID_REQUEST_BODY = "Not valid request body";

	public MappingData getMappingData(String rootMapping, HttpExchange exchange, GenericHandler handler)
			throws HandlerException {

		RequestMethod rq;
		try {
			// get request method
			rq = RequestMethod.valueOf(RequestMethod.class, exchange.getRequestMethod());
		} catch (Exception e) {
			// method is not present in enum RequestMethod (GET, POST,
			// PUT,DELETE)
			throw new BadRequestException(e.getMessage(), e);
		}

		// get path from request
		String path = exchange.getRequestURI().getPath();
		String queryString = exchange.getRequestURI().getQuery();

		// get methods annotated with "RequestMapping" in this class and
		// subclasses to find which method we must to invoke
		Collection<Method> methods = CoreUtils.getAnnotatedMethods(RequestMapping.class, handler);

		// we didn't find any method
		if (methods.isEmpty()) {
			throw new MappingNotFoundException(String.format(MAPPING_NOT_FOUND, path, rq));
		}

		MappingData mappingData = null;

		// check if method has proper annotation values
		Iterator<Method> iterator = methods.iterator();
		while (iterator.hasNext() && mappingData == null) {
			mappingData = extractMappingDataFromMethod(rootMapping, path, rq, iterator.next());
		}

		// no method found
		if (mappingData == null) {
			throw new MappingNotFoundException(String.format(MAPPING_NOT_FOUND, path, rq));
		}

		// if the method posts some data, we get the request body
		if (rq.needsRequestBody()) {
			String requestBody = null;
			try {
				requestBody = CoreUtils.fromStream(exchange.getRequestBody());
			} catch (IOException e) {
				throw new MappingException(INVALID_REQUEST_BODY, e);
			}
			mappingData.setRequestBody(requestBody);
		}

		// does the controller use session?
		if (handler.useSession() && queryString != null) {
			// get session id
			String pathPattern = String.format("^.*%s=(.*)$", GenericHandler.JSESSION_ID);
			Pattern pattern = Pattern.compile(pathPattern);
			Matcher matcher = pattern.matcher(queryString);
			if (matcher.find()) {
				mappingData.setSessionId(CoreUtils.getFirstMatch(matcher));
			}
		}

		// return found mapping
		return mappingData;
	}

	// check if found method has proper annotations values
	protected MappingData extractMappingDataFromMethod(String rootMapping, String path, RequestMethod rq, Method method)
			throws MappingException {
		RequestMapping annotation = method.getAnnotation(RequestMapping.class);
		MappingData mappingData = null;

		// Does it match ?
		if (rq.equals(annotation.method())) {

			String pathPattern = String.format("^%s%s$", rootMapping, annotation.pattern());
			Pattern pattern = Pattern.compile(pathPattern);
			Matcher matcher = pattern.matcher(path);

			// if pattern matches, get first match from regexp
			if (matcher.find()) {
				String bindingValue = CoreUtils.getFirstMatch(matcher);
				mappingData = MappingDataBuilder.builder().bindingValue(bindingValue).requestMethod(rq).method(method)
						.requestBodyClass(annotation.payLoad()).build();
			}
		}
		return mappingData;
	}
}
