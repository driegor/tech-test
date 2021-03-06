package com.company.solution.controller.page;

import java.util.Map;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.HttpStatus;
import com.company.mvc.response.Response;
import com.company.mvc.security.annotations.PreAuthorize;
import com.company.mvc.security.session.data.UserSession;

public class PageController extends GenericPageController {

	public static final String ROOT_MAPPING = "/pages";

	private static final Logger LOGGER = Logger.getLogger(PageController.class);

	public PageController() {
		this.rootMapping = ROOT_MAPPING;
	}

	@PreAuthorize(role = "PAGE_%s", useBindingValue = true)
	@RequestMapping(pattern = "/Page (.*)")
	public Response goToPage(String pageNumber, UserSession userSession, Map<String, String> params) {
		LOGGER.info(String.format("Get page [%s]", pageNumber));
		model.put("USER_NAME", userSession.getPrincipal().getUserName());
		model.put("PAGE_NAME", String.format("Page %s", pageNumber));
		model.put(SESSION_KEY, userSession.getKey());
		return getPage("templates/page.twig");
	}

	@RequestMapping(pattern = "/forbidden")
	public Response forbidden(UserSession userSession, Map<String, String> params) {
		LOGGER.info("Get forbidden page");
		model.put("USER_NAME", userSession.getPrincipal().getUserName());
		model.put(SESSION_KEY, userSession.getKey());
		return getPage("templates/forbidden.twig", HttpStatus.FORBIDDEN);
	}

}
