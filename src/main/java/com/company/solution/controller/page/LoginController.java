package com.company.solution.controller.page;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.exception.AuthenticationException;
import com.company.mvc.security.session.data.UserSession;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.form.LoginForm;

public class LoginController extends GenericPageController {

	public static final String ROOT_MAPPING = "/auth";
	private static final Logger LOGGER = Logger.getLogger(LoginController.class);
	private IAuthService authService;

	public LoginController(IAuthService authService) {
		this.authService = authService;
		this.rootMapping = ROOT_MAPPING;
	}

	@RequestMapping(pattern = "/login")
	public Response login(Map<String, String> params) {
		LOGGER.info("Show login form [%s]");
		model.put("POST_LOGIN", params.get(GlobalConst.POST_REDIRECT_URL));
		return getPage("templates/form.twig");
	}

	@RequestMapping(pattern = "/logout")
	public Response logout(UserSession userSession, Map<String, String> params) {
		authService.logout(params.get(GlobalConst.JSESSION_ID));
		return Responses.redirect(GlobalConst.LOGIN_URL);
	}

	@RequestMapping(pattern = "/authenticate", method = RequestMethod.POST, payLoad = LoginForm.class)
	public Response authenticate(LoginForm form, Map<String, String> params) throws UnsupportedEncodingException {
		try {
			String sessionKey = authService.login(form.getUserName(), form.getPassword());
			if (form.getPostLogin() != null && !"".equals(form.getPostLogin())) {
				String redirection = String.format("%s?%s=%s", form.getPostLogin(), GlobalConst.JSESSION_ID,
						sessionKey);
				return Responses.redirect(redirection);
			}
			model.put("USER_NAME", form.getUserName());
			model.put(SESSION_KEY, sessionKey);
			return getPage("templates/home.twig");

		} catch (AuthenticationException e) {
			LOGGER.debug(e);
			model.put(ERROR, "Authentication error");
			return getPage("templates/form.twig");
		}
	}
}
