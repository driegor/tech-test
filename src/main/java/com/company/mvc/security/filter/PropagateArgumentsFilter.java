package com.company.mvc.security.filter;

import java.io.IOException;
import java.util.Map;

import com.company.mvc.RequestUtils;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class PropagateArgumentsFilter extends Filter {

	private static final String FILTER_DESC = "Get parameters from request and saves their value";

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

		Map<String, String> params = RequestUtils.getParameters(exchange.getRequestURI().getRawQuery());
		exchange.setAttribute(GlobalConst.PARAMS, params);
		chain.doFilter(exchange);

	}

	@Override
	public String description() {
		return FILTER_DESC;
	}
}
