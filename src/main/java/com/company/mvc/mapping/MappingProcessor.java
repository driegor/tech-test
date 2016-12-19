package com.company.mvc.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.response.Response;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.mapper.Mapper;
import com.sun.net.httpserver.HttpExchange;

public class MappingProcessor {

	private Mapper mapper;

	public MappingProcessor() {
		mapper = getMapper();
	}

	protected Mapper getMapper() {
		return new Mapper();
	}

	// process request
	public Response process(MappingData mappingData, HttpExchange exchange, Object controller) throws HandlerException {
		return process(mappingData, exchange, Boolean.FALSE, controller);
	}

	// process request
	public Response process(MappingData mappingData, HttpExchange exchange, boolean needsSession, Object controller)
			throws HandlerException {

		Response response = null;

		// get values to invoke method from mappingData
		String binding = mappingData.getBindValue();
		String requestBody = mappingData.getRequestBody();
		Method method = mappingData.getMethod();

		List<Object> argList = new ArrayList<>();
		try {
			// prepare the parameters
			// the order is binding,post,params
			if (binding != null) {
				argList.add(binding);
			}
			// transform requestbody to the expected class
			if (requestBody != null) {
				argList.add(mapper.string2class(mappingData.getRequestBody(), mappingData.getRequestBodyClass(),
						mappingData.getContentType()));
			}
			// Do we need to pass the user session to the controller ?
			if (needsSession) {
				argList.add(exchange.getAttribute(GlobalConst.SESSION));
			}

			// add parameters
			argList.add(exchange.getAttribute(GlobalConst.PARAMS));

			Object[] argArr = argList.stream().toArray(size -> new Object[size]);
			response = (Response) method.invoke(controller, argArr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new BadRequestException(e.getMessage(), e);
		}

		return response;

	}

}
