package com.company.mvc.security.filter;

import java.io.IOException;

import com.company.mvc.RequestUtils;
import com.company.mvc.response.Redirect;
import com.company.mvc.security.SecurityUtils;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.session.data.UserSession;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class SessionAuthenticationFilter extends Filter {

	private static final String FILTER_DESC = "Creates a UserSession object and propagates its value";

	IAuthService authService;

	public SessionAuthenticationFilter(IAuthService authService) {
		this.authService = authService;
	}

	@Override
	public String description() {
		return FILTER_DESC;
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

		UserSession userSession = null;
		String path = exchange.getRequestURI().getPath();
		String loginURL = GlobalConst.LOGIN_URL;

		// dont apply authentication to "/auth/login,/auth/authorize"
		if (SecurityUtils.skipAuthorize(path)) {
			chain.doFilter(exchange);
			return;
		}

		// get session id from exchange
		String sessionId = RequestUtils.getParameter(exchange, GlobalConst.JSESSION_ID);

		// get userSession from authService
		if (sessionId != null) {
			userSession = authService.getUserSession(sessionId);
		}

		boolean sessionCreated = userSession != null;

		// if exists session save user data in exchange and go to the page
		if (sessionCreated) {
			exchange.setAttribute(GlobalConst.SESSION, userSession);
			exchange.setAttribute(GlobalConst.PRINCIPAL, userSession.getPrincipal());
			chain.doFilter(exchange);

			// if doesnt redirect to loginUrl with current path as parameter
		} else {
			Redirect.redirect(exchange, String.format("%s?%s=%s", loginURL, GlobalConst.POST_REDIRECT_URL, path));
		}
	}
}
