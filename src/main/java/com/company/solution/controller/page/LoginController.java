package com.company.solution.controller.page;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.response.Response;
import com.company.mvc.security.SecurityUtils;
import com.company.mvc.security.UserSession;
import com.company.mvc.security.auth.AuthForm;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.exception.AuthenticationException;

public class LoginController extends GenericPageController {

	public static final String ROOT_MAPPING = "/auth";
	private static final Logger LOGGER = Logger.getLogger(LoginController.class);

	public LoginController(IAuthService authService) {
		super(authService);
		this.rootMapping = ROOT_MAPPING;
	}

	@RequestMapping(pattern = "/form")
	public Response form() {
		LOGGER.info("Show login form [%s]");
		return getPage("templates/form.twig");
	}

	@RequestMapping(pattern = "/login", method = RequestMethod.POST)
	public Response login(String form, UserSession userSession) throws UnsupportedEncodingException {
		AuthForm authForm = SecurityUtils.getAuthForm(form);
		try {
			String sessionKey = authService.login(authForm);
			model.put(SESSION_KEY, sessionKey);
			return getPage("templates/home.twig");

		} catch (AuthenticationException e) {
			LOGGER.debug(e);
			model.put(ERROR, "Authentication error");
			return getPage("templates/form.twig");
		}
	}
}
