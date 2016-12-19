package com.company.mvc.response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.company.mockito.MockitoTest;
import com.company.mvc.exception.ResponseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ResponseWriterTest extends MockitoTest {

	@Mock
	HttpExchange exchange;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFailWritingToResponse() throws IOException, ResponseException {

		Throwable e = new NullPointerException("Something is wrong !!");
		Mockito.when(exchange.getResponseBody()).thenThrow(e);

		ResponseWriter responseWriter = new ResponseWriter();
		thrown.expect(ResponseException.class);
		responseWriter.write(exchange, null);
	}

	@Test
	public void testWriteResponse() throws IOException, ResponseException {

		String successMessage = "Success!!!";
		Response response = Responses.success(successMessage);

		OutputStream os = getOutPutStream(response);
		Mockito.when(exchange.getResponseBody()).thenReturn(os);
		Mockito.when(exchange.getResponseHeaders()).thenReturn(new Headers());

		ResponseWriter responseWriter = new ResponseWriter();
		responseWriter.write(exchange, response);

		// check if write was called with the ExpectedResponse values
		verify(exchange, times(1)).sendResponseHeaders(response.getStatus().value(), response.getContentLength());
		os.close();

	}

	@Test
	public void testRedirectResponse() throws IOException, ResponseException {

		String redirection = "/hola/adios";
		Response response = Responses.redirect(redirection);
		Headers headers = new Headers();

		Mockito.when(exchange.getResponseHeaders()).thenReturn(headers);

		ResponseWriter responseWriter = new ResponseWriter();
		responseWriter.write(exchange, response);

		verify(exchange, times(1)).sendResponseHeaders(301, -1);

		assertEquals(redirection, headers.get("Location").stream().findAny().get());

	}

	private OutputStream getOutPutStream(Response expectedResponse) {
		OutputStream os = new ByteArrayOutputStream() {

			@Override
			public void write(byte b[]) throws IOException {
				String content = new String(b);
				Assert.assertNotNull(b);
				Assert.assertEquals(expectedResponse.getContent(), content);
			}
		};
		return os;
	}
}
