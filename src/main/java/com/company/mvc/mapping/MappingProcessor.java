package com.company.mvc.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.company.common.utils.CoreUtils;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.response.Response;

public class MappingProcessor {

	// process request
	public Response process(MappingData mappingData, Object handler) throws HandlerException {

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
				argList.add(CoreUtils.requestBodyToPostData(mappingData.getRequestBody(),
						mappingData.getRequestBodyClass()));
			}
			Object[] argArr = argList.stream().toArray(size -> new Object[size]);
			response = (Response) method.invoke(handler, argArr);
		} catch (Exception e) {
			throw new BadRequestException(e.getMessage(), e);
		}

		return response;

	}

}
