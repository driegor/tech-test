package com.company.mvc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Method;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.company.data.DummyPostData;
import com.company.mockito.MockitoTest;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.mapping.MappingData.MappingDataBuilder;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.mvc.security.auth.IAuthService;

public class MappingProcessorTest extends MockitoTest {

	@Mock
	IAuthService authService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testMappingWithoutMethods() throws HandlerException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).build();
		thrown.expect(BadRequestException.class);
		mappingProcessor.process(mappingData, null);
	}

	@Test
	public void testArgumentMisMatchMethod() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";

		GenericHandler handlerWithArgumentMisMatchMethod = new GenericHandler(authService) {
			@SuppressWarnings("unused")
			public void argumentMisMatchMethod(Integer integerArg) {
			}
		};

		Method method = handlerWithArgumentMisMatchMethod.getClass().getMethod("argumentMisMatchMethod", Integer.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		thrown.expect(BadRequestException.class);
		mappingProcessor.process(mappingData, handlerWithArgumentMisMatchMethod);
	}

	@Test
	public void testMethodInvokedWithParams() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";

		GenericHandler matchMethodHandler = new GenericHandler(authService) {
			@SuppressWarnings("unused")
			public Response matchMethod(String value) {
				return Responses.success(String.format("Dummy content with value =%s", value));
			}
		};

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", String.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		Response response = mappingProcessor.process(mappingData, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =dummyValue", response.getContent());
	}

	@Test
	public void testMethodWithStringPostData() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String postData = "dummy post data";
		GenericHandler matchMethodHandler = new GenericHandler(authService) {
			@SuppressWarnings("unused")
			public Response matchMethod(String postData) {
				return Responses.success(String.format("Dummy content with value =%s", postData));
			}
		};

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", String.class);
		MappingData mappingData = MappingDataBuilder.builder().requestBody(postData).requestBodyClass(String.class)
				.method(method).build();

		Response response = mappingProcessor.process(mappingData, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =dummy post data", response.getContent());
	}

	@Test
	public void testMethodWithComplexPostData() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String postData = "{'mail':driegor,'name':'dani'}";

		GenericHandler matchMethodHandler = new GenericHandler(authService) {
			@SuppressWarnings("unused")
			public Response matchMethod(DummyPostData postData) {
				return Responses.success(String.format("Dummy content with value =%s", postData.toString()));
			}
		};

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", DummyPostData.class);
		MappingData mappingData = MappingDataBuilder.builder().requestBody(postData)
				.requestBodyClass(DummyPostData.class).method(method).build();

		Response response = mappingProcessor.process(mappingData, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =DummyPostData [mail=driegor, name=dani]", response.getContent());
	}

	@Test
	public void testMethodInvokedWithoutParams() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();

		GenericHandler matchMethodHandler = new GenericHandler(authService) {
			@SuppressWarnings("unused")
			public Response matchMethod() {
				return Responses.success("Dummy content without value");
			}
		};

		Method method = matchMethodHandler.getClass().getMethod("matchMethod");
		MappingData mappingData = MappingDataBuilder.builder().method(method).build();

		Response response = mappingProcessor.process(mappingData, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content without value", response.getContent());
	}
}
