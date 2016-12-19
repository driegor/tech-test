package com.company.mvc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.company.mockito.MockitoTest;
import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.exception.BadRequestException;
import com.company.mvc.exception.HandlerException;
import com.company.mvc.exception.MappingNotFoundException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.security.auth.IAuthService;
import com.sun.net.httpserver.HttpExchange;

public class MappingHandlerTest extends MockitoTest {

	@Mock
	HttpExchange exchange;

	@Mock
	IAuthService authService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testBadRequestMethod() throws HandlerException {
		MappingHandler mappingHandler = new MappingHandler();

		when(exchange.getRequestMethod()).thenReturn("NOT-EXISTING-METHOD");
		thrown.expect(BadRequestException.class);
		mappingHandler.getMappingData(null, exchange, null);

	}

	@Test
	public void testNotAnnotatedMethod() throws HandlerException, URISyntaxException {
		MappingHandler mappingHandler = new MappingHandler();
		GenericHandler dummyHandler = new GenericHandler(authService);
		String path = "/dummy/path";
		RequestMethod method = RequestMethod.GET;
		when(exchange.getRequestMethod()).thenReturn(method.name());
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));

		thrown.expect(MappingNotFoundException.class);
		thrown.expectMessage(String.format("Not mapping found for path ['%s'] and method ['%s']", path, method));
		mappingHandler.getMappingData(null, exchange, dummyHandler);

	}

	@Test
	public void testPathDoesntMatch() throws HandlerException, URISyntaxException {
		MappingHandler mappingHandler = new MappingHandler();
		GenericHandler dummyHandler = new GenericHandler(authService) {
			@RequestMapping(pattern = "/different-dummy/path")
			public void neverInvokedMethod() {
			}
		};

		String path = "/dummy/path";
		RequestMethod method = RequestMethod.GET;
		when(exchange.getRequestMethod()).thenReturn(method.name());
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));

		thrown.expect(MappingNotFoundException.class);
		thrown.expectMessage(String.format("Not mapping found for path ['%s'] and method ['%s']", path, method));
		mappingHandler.getMappingData(null, exchange, dummyHandler);

	}

	@Test
	public void testAnnotatedMethodDoesntMatch() throws HandlerException, URISyntaxException {
		MappingHandler mappingHandler = new MappingHandler();
		GenericHandler dummyHandler = new GenericHandler(authService) {
			@RequestMapping(pattern = "/dummy/path")
			public void neverInvokedMethod() {
			}
		};

		String path = "/dummy/path";
		RequestMethod method = RequestMethod.POST;
		when(exchange.getRequestMethod()).thenReturn(method.name());
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));

		thrown.expect(MappingNotFoundException.class);
		thrown.expectMessage(String.format("Not mapping found for path ['%s'] and method ['%s']", path, method));
		mappingHandler.getMappingData(null, exchange, dummyHandler);

	}

	@Test
	public void testBindingMethod()
			throws HandlerException, URISyntaxException, NoSuchMethodException, SecurityException {
		MappingHandler mappingHandler = new MappingHandler();

		GenericHandler dummyHandler = new GenericHandler(authService) {
			@RequestMapping(pattern = "/dummy/path/([a-zA-Z0-9]+)")
			public void neverInvokedMethod(String name) {
			}
		};

		String path = "/dummy/path/2";
		RequestMethod method = RequestMethod.GET;
		when(exchange.getRequestMethod()).thenReturn(method.name());
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));

		MappingData mappingData = mappingHandler.getMappingData("", exchange, dummyHandler);

		assertNotNull(mappingData);
		assertEquals(method, mappingData.getRequestMethod());
		assertEquals(String.valueOf(2), mappingData.getBindValue());
		assertEquals(dummyHandler.getClass().getMethod("neverInvokedMethod", String.class), mappingData.getMethod());

	}

	@Test
	public void testPostMethod() throws HandlerException, URISyntaxException, NoSuchMethodException, SecurityException {
		MappingHandler mappingHandler = new MappingHandler();
		String requestBody = "dummyRequestValue";
		InputStream is = new ByteArrayInputStream(requestBody.getBytes());

		GenericHandler dummyHandler = new GenericHandler(authService) {
			@RequestMapping(pattern = "/dummy/path/", method = RequestMethod.POST)
			public void neverInvokedMethod(String postValue) {
			}
		};

		String path = "/dummy/path/";
		RequestMethod method = RequestMethod.POST;
		when(exchange.getRequestMethod()).thenReturn(method.name());
		when(exchange.getRequestBody()).thenReturn(is);

		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));

		MappingData mappingData = mappingHandler.getMappingData("", exchange, dummyHandler);

		assertNotNull(mappingData);
		assertEquals(method, mappingData.getRequestMethod());
		assertEquals(requestBody, mappingData.getRequestBody());
		assertEquals(dummyHandler.getClass().getMethod("neverInvokedMethod", String.class), mappingData.getMethod());

	}
}
