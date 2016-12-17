package com.company.solution.controller;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class PageController extends GenericHandler {

	public static final String ROOT_MAPPING = "/pages";

	private static final Logger LOGGER = Logger.getLogger(PageController.class);

	public PageController() {
		this.rootMapping = ROOT_MAPPING;
	}

	@RequestMapping(pattern = "/Page (.*)")
	public Response getPage(String pageNumber) {
		LOGGER.info(String.format("Get page [%s]", pageNumber));
		JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/page.twig");
		JtwigModel model = JtwigModel.newModel().with("USER_NAME", "hardcore user name").with("PAGE_NAME",
				String.format("Page %s", pageNumber));

		OutputStream out = new ByteArrayOutputStream();
		template.render(model, out);
		return Responses.success(out.toString());
	}

}
