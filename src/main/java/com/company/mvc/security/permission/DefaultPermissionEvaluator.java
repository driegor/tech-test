package com.company.mvc.security.permission;

import com.company.mvc.security.annotations.PreAuthorize;
import com.company.mvc.security.auth.Principal;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.HttpExchange;

public class DefaultPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(PreAuthorize preAuthorize, HttpExchange exchange, String bindingValue) {
		// check if authorization is necessary
		if (preAuthorize == null) {
			return Boolean.TRUE;
		}

		// get principal data
		Principal principal = (Principal) exchange.getAttribute(GlobalConst.PRINCIPAL);

		// it doesn't exist principal information
		if (principal == null) {
			return Boolean.FALSE;
		}

		String role = preAuthorize.useBindingValue() ? String.format(preAuthorize.role(), bindingValue)
				: preAuthorize.role();
		
		return principal.getRoles().contains(role);
	}

}
