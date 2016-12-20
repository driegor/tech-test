package com.company.solution.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.company.mockito.MockitoTest;
import com.company.mvc.security.exception.AuthenticationException;
import com.company.mvc.security.session.data.SessionData;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.common.dto.UserDTO.UserDTOBuilder;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;

public class AuthServiceTest extends MockitoTest {

	@Mock
	private IUserService userService;

	@Mock
	private SessionData sessionData;

	private AuthService authService;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.authService = AuthService.getInstance();
		authService.init(userService, sessionData);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testLoginError() throws AuthenticationException, ServiceException {
		String userName = "userName";
		String password = "xxxx";

		when(userService.findByUserAndPassword(userName, password))
				.thenThrow(new ServiceException(new SQLException("bad query")));
		thrown.expect(AuthenticationException.class);

		authService.login(userName, password);

	}

	@Test
	public void testInvalidLogin() throws AuthenticationException, ServiceException {
		String userName = "userName";
		String password = "xxxx";

		when(userService.findByUserAndPassword(userName, password)).thenReturn(null);
		thrown.expect(AuthenticationException.class);

		authService.login(userName, password);

	}

	@Test
	public void testValid() throws AuthenticationException, ServiceException {
		String userName = "userName";
		String password = "xxxx";
		List<String> roles = Arrays.asList("role1", "role2");

		UserDTO userDto = UserDTOBuilder.builder().userName(userName).password(password).roles(roles).build();
		when(userService.findByUserAndPassword(userName, password)).thenReturn(userDto);

		authService.login(userName, password);

		verify(sessionData, times(1)).addSession(anyString(), any());

	}

	@Test
	public void testLogout() {
		String sessionId = "xxxxx";
		authService.logout(sessionId);
		verify(sessionData, times(1)).removeSession(sessionId);
	}

	@Test
	public void testGetUserSession() {
		String sessionId = "xxxxx";
		authService.getUserSession(sessionId);
		verify(sessionData, times(1)).getSession(sessionId);
	}

}
