package com.company.mvc.mapping;

import java.lang.reflect.Method;

import com.company.mvc.enums.RequestMethod;

public class MappingData {

	private Method method;
	private RequestMethod requestMethod;

	private String bindingValue;
	private String requestBody;
	private String sessionId;
	private Class<?> requestBodyClass;

	public String getBindValue() {
		return bindingValue;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void setBindingValue(String bindingValue) {
		this.bindingValue = bindingValue;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public Class<?> getRequestBodyClass() {
		return requestBodyClass;
	}

	public void setRequestBodyClass(Class<?> requestBodyClass) {
		this.requestBodyClass = requestBodyClass;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public static class MappingDataBuilder {

		private MappingData instance;

		private MappingDataBuilder() {
			instance = new MappingData();
		}

		public static MappingDataBuilder builder() {
			return new MappingDataBuilder();
		}

		public MappingDataBuilder method(Method method) {
			instance.setMethod(method);
			return this;
		}

		public MappingDataBuilder bindingValue(String bindingValue) {
			instance.setBindingValue(bindingValue);
			return this;
		}

		public MappingDataBuilder requestMethod(RequestMethod requestMethod) {
			instance.setRequestMethod(requestMethod);
			return this;
		}

		public MappingDataBuilder requestBody(String requestBody) {
			instance.setRequestBody(requestBody);
			return this;
		}

		public MappingDataBuilder requestBodyClass(Class<?> requestBodyClass) {
			instance.setRequestBodyClass(requestBodyClass);
			return this;
		}

		public MappingDataBuilder sessionId(String sessionId) {
			instance.setSessionId(sessionId);
			return this;
		}

		public MappingData build() {
			return instance;
		}

	}

}
