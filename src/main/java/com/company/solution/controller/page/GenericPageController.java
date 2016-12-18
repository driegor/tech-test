package com.company.solution.controller.page;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.mvc.security.auth.IAuthService;

public class GenericPageController extends GenericHandler {

	public GenericPageController(IAuthService authService) {
		super(authService);
	}

	protected static final String SESSION_KEY = "SESSION_KEY";
	protected static final String ERROR = "ERROR";

	protected Map<String, String> model = new HashMap<>();

	protected Response getPage(String templateName) {

		JtwigTemplate template = JtwigTemplate.classpathTemplate(templateName);
		JtwigModel jtModel = JtwigModel.newModel();
		model.forEach(jtModel::with);
		OutputStream out = new ByteArrayOutputStream();
		template.render(jtModel, out);
		return Responses.success(out.toString());
	}

	@Override
	public boolean useSession() {
		return Boolean.TRUE;
	}

}
