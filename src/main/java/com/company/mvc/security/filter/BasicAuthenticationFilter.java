package com.company.mvc.security.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.log4j.Logger;

import com.company.mvc.enums.ContentType;
import com.company.mvc.exception.ResponseException;
import com.company.mvc.response.ResponseWriter;
import com.company.mvc.response.Responses;
import com.company.mvc.security.auth.Principal;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.service.IUserService;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class BasicAuthenticationFilter extends Filter {

	private static final Logger LOGGER = Logger.getLogger(BasicAuthenticationFilter.class);

	private static final String FILTER_DESC = "Creates a credential object and propagates its value";

	IUserService userService;

	public BasicAuthenticationFilter(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public String description() {
		return FILTER_DESC;
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

		try {
			authorize(exchange, chain);
		} catch (ResponseException e) {
			LOGGER.error("Error writting to response " + e.getMessage());
		}
	}

	private void authorize(HttpExchange exchange, Chain chain) throws ResponseException, IOException {
		// get Authorization header value

		List<String> auths = exchange.getRequestHeaders().get(GlobalConst.AUTHORIZATION);
		String authHeader = auths != null && !auths.isEmpty() ? auths.stream().findAny().get() : null;

		if (authHeader == null || !authHeader.startsWith("Basic")) {
			notAuthorized(exchange, "Authorization header doesn't exist");
			return;
		}

		// get authorization information

		try {
			Principal principal = getPrincipal(authHeader);
			exchange.setAttribute(GlobalConst.PRINCIPAL, principal);
			chain.doFilter(exchange);
			return;
		} catch (Exception e) {
			notAuthorized(exchange, "User no valid");

		}
	}

	private Principal getPrincipal(String authHeader) throws Exception {
		String encodedUsernamePassword = authHeader.substring("Basic ".length()).trim();
		String decodedUsernamePassword = new String(Base64.getDecoder().decode(encodedUsernamePassword));
		String[] userAndPassword = decodedUsernamePassword.split(":");
		String userName = userAndPassword[0];
		String password = userAndPassword[1];
		UserDTO user = userService.findByUserAndPassword(userName, password);
		return new Principal(userName, user.getRoles());
	}

	private void notAuthorized(HttpExchange exchange, String message) throws ResponseException {
		String content = String.format("{message:\'%s\'}", message);
		new ResponseWriter().write(exchange, Responses.unAuthorized(content, ContentType.APPLICATION_JSON));
	}
}
