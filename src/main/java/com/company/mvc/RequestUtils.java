package com.company.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.HttpExchange;

public class RequestUtils {

	public static final String AMP = "&";
	public static final String EQ = "=";

	public static Map<String, String> getParameters(String query) throws UnsupportedEncodingException {

		Map<String, String> result = new HashMap<>();

		if (query == null || "".equals(query)) {
			return result;
		}

		for (String param : query.split(AMP)) {
			String[] pair = param.split(EQ);
			if (pair.length > 1) {
				result.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	public static String getParameter(HttpExchange exchange, String paramName) {

		if (exchange == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		Map<String, String> params = (Map<String, String>) exchange.getAttribute(GlobalConst.PARAMS);

		if (params == null) {
			return null;
		}

		return params.get(paramName);

	}
}
