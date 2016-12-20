package com.company.mvc.security.filter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.company.mockito.MockitoTest;
import com.company.mvc.enums.ContentType;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.common.dto.UserDTO.UserDTOBuilder;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;
import com.sun.net.httpserver.Filter.Chain;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class BasicAuthenticationFilterTest extends MockitoTest {

	@Mock
	IUserService userService;

	@Mock
	HttpExchange exchange;

	@Mock
	Chain chain;

	@Test
	public void testFilterNoHeader() throws IOException {

		String content = "{message:\'Authorization header doesn't exist\'}";
		Response response = Responses.unAuthorized(content, ContentType.APPLICATION_JSON);

		OutputStream os = getOutPutStream(response);
		Mockito.when(exchange.getRequestHeaders()).thenReturn(new Headers());
		Mockito.when(exchange.getResponseBody()).thenReturn(os);
		Mockito.when(exchange.getResponseHeaders()).thenReturn(new Headers());

		BasicAuthenticationFilter filter = new BasicAuthenticationFilter(userService);
		filter.doFilter(exchange, chain);

		verify(exchange, times(1)).sendResponseHeaders(response.getStatus().value(), response.getContentLength());
		os.close();

	}

	@Test
	public void testMalFormedHeader() throws IOException {

		String content = "{message:\'User no valid\'}";
		Response response = Responses.unAuthorized(content, ContentType.APPLICATION_JSON);

		OutputStream os = getOutPutStream(response);
		Headers headers = new Headers();
		headers.add(GlobalConst.AUTHORIZATION, "Basic malformedEncoding");
		Mockito.when(exchange.getRequestHeaders()).thenReturn(headers);
		Mockito.when(exchange.getResponseBody()).thenReturn(os);
		Mockito.when(exchange.getResponseHeaders()).thenReturn(new Headers());

		BasicAuthenticationFilter filter = new BasicAuthenticationFilter(userService);
		filter.doFilter(exchange, chain);

		verify(exchange, times(1)).sendResponseHeaders(response.getStatus().value(), response.getContentLength());
		os.close();

	}

	@Test
	public void testUserNotValid() throws IOException {

		String content = "{message:\'User no valid\'}";
		Response response = Responses.unAuthorized(content, ContentType.APPLICATION_JSON);

		OutputStream os = getOutPutStream(response);
		Headers headers = new Headers();
		headers.add(GlobalConst.AUTHORIZATION, "Basic ZGFuaTpkYW5p");
		Mockito.when(exchange.getRequestHeaders()).thenReturn(headers);
		Mockito.when(exchange.getResponseBody()).thenReturn(os);
		Mockito.when(exchange.getResponseHeaders()).thenReturn(new Headers());

		BasicAuthenticationFilter filter = new BasicAuthenticationFilter(userService);
		filter.doFilter(exchange, chain);

		verify(exchange, times(1)).sendResponseHeaders(response.getStatus().value(), response.getContentLength());
		os.close();

	}

	@Test
	public void testValid() throws IOException, ServiceException {

		Headers headers = new Headers();
		headers.add(GlobalConst.AUTHORIZATION, "Basic ZGFuaTpkYW5p");
		Mockito.when(exchange.getRequestHeaders()).thenReturn(headers);
		when(userService.findByUserAndPassword("dani", "dani"))
				.thenReturn(UserDTOBuilder.builder().userName("dani").build());

		BasicAuthenticationFilter filter = new BasicAuthenticationFilter(userService);
		filter.doFilter(exchange, chain);

		verify(chain, times(1)).doFilter(exchange);

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
