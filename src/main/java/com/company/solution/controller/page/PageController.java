package com.company.solution.controller.page;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.response.Response;
import com.company.mvc.security.UserSession;
import com.company.mvc.security.auth.IAuthService;

public class PageController extends GenericPageController {

	public static final String ROOT_MAPPING = "/pages";

	private static final Logger LOGGER = Logger.getLogger(PageController.class);

	public PageController(IAuthService authService) {
		super(authService);
		this.rootMapping = ROOT_MAPPING;
	}

	@RequestMapping(pattern = "/Page (.*)")
	public Response goToPage(String pageNumber, UserSession userSession) {
		LOGGER.info(String.format("Get page [%s]", pageNumber));
		model.put("USER_NAME", userSession.getPrincipal().getUserName());
		model.put("PAGE_NAME", String.format("Page %s", pageNumber));
		return getPage("templates/page.twig");
	}

}
