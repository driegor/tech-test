package com.company.mvc.security.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.company.mockito.MockitoTest;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.session.data.UserSession;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.Filter.Chain;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class SessionAuthenticationFilterTest extends MockitoTest {

	@Mock
	IAuthService authService;

	@Mock
	Chain chain;

	@Mock
	HttpExchange exchange;

	@Test
	public void testAuthenticated() throws IOException, URISyntaxException {
		SessionAuthenticationFilter filter = new SessionAuthenticationFilter(authService);
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(GlobalConst.LOGIN_URL));
		filter.doFilter(exchange, chain);
		verify(chain, times(1)).doFilter(exchange);
	}

	@Test
	public void testNotLogged() throws IOException, URISyntaxException {
		SessionAuthenticationFilter filter = new SessionAuthenticationFilter(authService);
		String path = "/any/url";

		Headers headers = new Headers();
		Mockito.when(exchange.getResponseHeaders()).thenReturn(headers);
		Mockito.when(exchange.getRequestURI()).thenReturn(new URI(path));
		filter.doFilter(exchange, chain);

		Mockito.when(exchange.getResponseHeaders()).thenReturn(headers);

		verify(exchange, times(1)).sendResponseHeaders(301, -1);
		String redirection = String.format("%s?%s=%s", GlobalConst.LOGIN_URL, GlobalConst.POST_REDIRECT_URL, path);
		assertEquals(redirection, headers.get("Location").stream().findAny().get());

	}

	@Test
	public void testLogged() throws IOException, URISyntaxException {
		SessionAuthenticationFilter filter = new SessionAuthenticationFilter(authService);
		String path = "/any/url";
		String sessionId = "xxxxx";
		UserSession userSession = new UserSession();
		Map<String, String> params = new HashMap<>();
		params.put(GlobalConst.JSESSION_ID, sessionId);

		when(exchange.getRequestURI()).thenReturn(new URI(path));
		when(exchange.getAttribute(GlobalConst.PARAMS)).thenReturn(params);
		when(authService.getUserSession(sessionId)).thenReturn(userSession);

		filter.doFilter(exchange, chain);
		verify(exchange, times(1)).setAttribute(GlobalConst.SESSION, userSession);
		verify(chain, times(1)).doFilter(exchange);

	}
}
