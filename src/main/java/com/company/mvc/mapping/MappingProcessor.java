package com.company.mvc.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.company.common.utils.CoreUtils;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;

public class MappingProcessor {

	// process request
	public Response process(MappingData mappingData, GenericHandler controller) throws HandlerException {

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
			// Do we need to propagate the session id ?
			if (controller.useSession()) {
				argList.add(mappingData.getSessionId() != null ? controller.getUserSession(mappingData.getSessionId())
						: null);
			}

			Object[] argArr = argList.stream().toArray(size -> new Object[size]);
			response = (Response) method.invoke(controller, argArr);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new BadRequestException(e.getMessage(), e);
		}

		return response;

	}

}
