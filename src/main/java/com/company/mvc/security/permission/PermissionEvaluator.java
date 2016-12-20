package com.company.mvc.security.permission;

import com.company.mvc.security.annotations.PreAuthorize;
import com.sun.net.httpserver.HttpExchange;

public interface PermissionEvaluator {

	boolean hasPermission(PreAuthorize preAuthorize, HttpExchange exchange, String extraData);

}
