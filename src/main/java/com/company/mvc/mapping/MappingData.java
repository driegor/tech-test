package com.company.mvc.mapping;

import java.lang.reflect.Method;

import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.security.annotations.PreAuthorize;

public class MappingData {

	private Method method;
	private RequestMethod requestMethod;

	private String bindingValue;
	private String requestBody;
	private Class<?> requestBodyClass;
	private String path;
	private ContentType contentType;
	private PreAuthorize preAuthorize;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

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

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public PreAuthorize getPreAuthorize() {
		return preAuthorize;
	}

	public void setPreAuthorize(PreAuthorize preAuthorize) {
		this.preAuthorize = preAuthorize;
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

		public MappingDataBuilder path(String path) {
			instance.setPath(path);
			return this;
		}

		public MappingDataBuilder preAuthorize(PreAuthorize preAuthorize) {
			instance.setPreAuthorize(preAuthorize);
			return this;
		}

		public MappingDataBuilder contentType(ContentType contentType) {
			instance.setContentType(contentType);
			return this;
		}

		public MappingData build() {
			return instance;
		}

	}

}
