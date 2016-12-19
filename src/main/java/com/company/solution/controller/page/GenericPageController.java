package com.company.solution.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;

public class GenericPageController extends GenericHandler {

	protected static final String SESSION_KEY = "SESSION_KEY";
	protected static final String ERROR = "ERROR";

	protected Map<String, String> model = new HashMap<>();

	protected Response getPage(String templateName) {

		JtwigTemplate template = JtwigTemplate.classpathTemplate(templateName);
		JtwigModel jtModel = JtwigModel.newModel();
		model.forEach(jtModel::with);
		return Responses.success(template.render(jtModel));
	}

	@Override
	public boolean useSession() {
		return Boolean.TRUE;
	}

}
