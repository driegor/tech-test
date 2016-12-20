package com.company.mvc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.company.data.DummyPostData;
import com.company.mockito.MockitoTest;
import com.company.mvc.enums.ContentType;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.mapping.MappingData.MappingDataBuilder;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.session.data.UserSession;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.form.LoginForm;
import com.sun.net.httpserver.HttpExchange;

public class MappingProcessorTest extends MockitoTest {

	@Mock
	IAuthService authService;

	@Mock
	HttpExchange exchange;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testMappingWithoutMethods() throws HandlerException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).build();
		thrown.expect(BadRequestException.class);
		mappingProcessor.process(mappingData, exchange, null);
	}

	@Test
	public void testArgumentMisMatchMethod() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";

		GenericHandler handlerWithArgumentMisMatchMethod = new GenericHandler() {
			@SuppressWarnings("unused")
			public void argumentMisMatchMethod(Integer integerArg) {
			}
		};

		Method method = handlerWithArgumentMisMatchMethod.getClass().getMethod("argumentMisMatchMethod", Integer.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		thrown.expect(BadRequestException.class);
		mappingProcessor.process(mappingData, exchange, handlerWithArgumentMisMatchMethod);
	}

	@Test
	public void testMethodInvokedWithBindings() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(String value, Map<String, String> map) {
				return Responses.success(String.format("Dummy content with value =%s", value));
			}
		};

		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", String.class, Map.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =dummyValue", response.getContent());
	}

	@Test
	public void testMethodInvokedWithBindingsWithUserSession()
			throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(String value, UserSession userSession, Map<String, String> map) {
				return Responses.success(String.format("Dummy content with value =%s", value));
			}
		};

		when(exchange.getAttribute(GlobalConst.JSESSION_ID)).thenReturn(new UserSession());
		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", String.class, UserSession.class,
				Map.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, Boolean.TRUE, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =dummyValue", response.getContent());

	}

	@Test
	public void testMethodWithFormPostData() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String postData = "userName=user&password=xxxx";
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(LoginForm loginForm, Map<String, String> map) {
				return Responses.success(String.format("Dummy content with value =%s,%s", loginForm.getUserName(),
						loginForm.getPassword()));
			}
		};

		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", LoginForm.class, Map.class);
		MappingData mappingData = MappingDataBuilder.builder().requestBody(postData).contentType(ContentType.TEXT_HTML)
				.requestBodyClass(LoginForm.class).method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =user,xxxx", response.getContent());
	}

	@Test
	public void testMethodWithComplexPostData() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String postData = "{'mail':'driegor','name':'dani'}";
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(DummyPostData postData, Map<String, String> map) {
				return Responses.success(String.format("Dummy content with value =%s", postData.toString()));
			}
		};
		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", DummyPostData.class, Map.class);
		MappingData mappingData = MappingDataBuilder.builder().requestBody(postData)
				.requestBodyClass(DummyPostData.class).contentType(ContentType.APPLICATION_JSON).method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =DummyPostData [mail=driegor, name=dani]", response.getContent());
	}

	@Test
	public void testMethodInvokedWithoutBindings() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(Map<String, String> map) {
				return Responses.success("Dummy content without value");
			}
		};

		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", Map.class);
		MappingData mappingData = MappingDataBuilder.builder().method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content without value", response.getContent());
	}

	@Test
	public void testPreAuthorizeWithBindings() throws HandlerException, NoSuchMethodException, SecurityException {
		MappingProcessor mappingProcessor = new MappingProcessor();
		String dummyBindingValue = "dummyValue";
		Map<String, String> map = new HashMap<>();

		GenericHandler matchMethodHandler = new GenericHandler() {
			@SuppressWarnings("unused")
			public Response matchMethod(String value, Map<String, String> map) {
				return Responses.success(String.format("Dummy content with value =%s", value));
			}
		};

		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(map);

		Method method = matchMethodHandler.getClass().getMethod("matchMethod", String.class, Map.class);
		MappingData mappingData = MappingDataBuilder.builder().bindingValue(dummyBindingValue).method(method).build();

		Response response = mappingProcessor.process(mappingData, exchange, matchMethodHandler);
		assertNotNull(response);
		assertEquals("Dummy content with value =dummyValue", response.getContent());
	}
}
