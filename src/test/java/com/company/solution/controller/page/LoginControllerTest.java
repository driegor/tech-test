package com.company.solution.controller.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;

import com.company.mockito.MockitoTest;
import com.company.mvc.response.Response;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.exception.AuthenticationException;
import com.company.solution.common.dto.GlobalConst;
import com.company.solution.form.LoginForm;
import com.company.solution.form.LoginForm.LoginFormBuilder;

public class LoginControllerTest extends MockitoTest {

	@Mock
	IAuthService authService;

	@Test
	public void testLogin() {

		LoginController loginController = new LoginController(authService);
		Map<String, String> params = new HashMap<>();

		Response response = loginController.login(params);
		assertNotNull(response.getContent());
	}

	@Test
	public void testLogout() {

		LoginController loginController = new LoginController(authService);
		Map<String, String> params = new HashMap<>();
		String sessionId = "xxxxxx";
		params.put(GlobalConst.JSESSION_ID, sessionId);

		Response response = loginController.logout(null, params);
		assertNotNull(response.getContent());
		assertEquals(response.isRedirect(), Boolean.TRUE);
		verify(authService, times(1)).logout(sessionId);
	}

	@Test
	public void testAuthenticateFail() throws UnsupportedEncodingException, AuthenticationException {

		LoginController loginController = new LoginController(authService);
		Map<String, String> params = new HashMap<>();
		String userName = "userName";
		String password = "123456";
		when(authService.login(userName, password)).thenThrow(new AuthenticationException("error"));
		LoginForm form = LoginFormBuilder.builder().userName(userName).password(password).build();
		Response response = loginController.authenticate(form, params);
		assertNotNull(response.getContent());

	}

	@Test
	public void testAuthenticate() throws UnsupportedEncodingException, AuthenticationException {

		LoginController loginController = new LoginController(authService);
		Map<String, String> params = new HashMap<>();
		String userName = "userName";
		String password = "123456";
		String sessionId = "xxxxxxx";
		when(authService.login(userName, password)).thenReturn(sessionId);
		LoginForm form = LoginFormBuilder.builder().userName(userName).password(password).build();
		Response response = loginController.authenticate(form, params);
		assertNotNull(response.getContent());

	}

	@Test
	public void testAuthenticateWithPostUrl() throws UnsupportedEncodingException, AuthenticationException {

		LoginController loginController = new LoginController(authService);
		String postLogin = "/postUrl";
		Map<String, String> params = new HashMap<>();
		String userName = "userName";
		String password = "123456";
		String sessionId = "xxxxxxx";
		when(authService.login(userName, password)).thenReturn(sessionId);
		LoginForm form = LoginFormBuilder.builder().userName(userName).password(password).postLogin(postLogin).build();
		Response response = loginController.authenticate(form, params);
		assertNotNull(response.getContent());
		assertEquals(response.isRedirect(), Boolean.TRUE);
		assertEquals(String.format("%s?%s=%s", form.getPostLogin(), GlobalConst.JSESSION_ID, sessionId),
				response.getContent());

	}

}
