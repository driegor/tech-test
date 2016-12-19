package com.company.solution.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;

import com.company.mockito.MockitoTest;
import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;
import com.company.mvc.response.Response;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.auth.Principal;
import com.company.mvc.security.session.data.UserSession;
import com.company.solution.controller.page.PageController;

public class PageControllerTest extends MockitoTest {

	@Mock
	IAuthService authService;

	@Mock
	UserSession userSession;

	@Test
	public void testRenderPage() {
		String userName = "dummyUserName";
		List<String> roles = Arrays.asList("role1", "role2");
		Map<String, String> params = new HashMap<>();
		Principal principal = new Principal(userName, roles);
		when(userSession.getPrincipal()).thenReturn(principal);

		PageController pageController = new PageController();
		Response response = pageController.goToPage(String.valueOf(1), userSession, params);

		assertEquals(ContentType.TEXT_HTML, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertNotNull(response.getContent());

	}

}
