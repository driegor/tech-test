package com.company.solution.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.company.common.utils.JsonUtils;
import com.company.mockito.MockitoTest;
import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;
import com.company.mvc.response.Response;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.common.dto.UserDTO.UserDTOBuilder;
import com.company.solution.controller.api.UserRestController;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;

public class UserRestControllerTest extends MockitoTest {

	@Mock
	IUserService userService;

	@InjectMocks
	UserRestController userRestController;

	@Test
	public void testGet() throws ServiceException {
		String name = "dummyName";
		String password = "password";

		UserDTO userDTO = UserDTOBuilder.builder().userName(name).password(password).build();
		when(userService.get(name)).thenReturn(userDTO);

		Response response = userRestController.getUser(name);
		assertNotNull(response);
		assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(JsonUtils.writeToJson(userDTO), response.getContent());

	}

	@Test
	public void testGetAll() throws ServiceException {
		String name = "dummyName";
		String password = "password";

		//// @formatter:off
		List<UserDTO> users = IntStream.range(1, 5)
				.mapToObj(v -> UserDTOBuilder.builder().userName(name + v).password(password + v).build())
				.collect(Collectors.toList());
		// @formatter:on

		when(userService.getAll()).thenReturn(users);

		Response response = userRestController.getAll();
		assertNotNull(response);
		assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(JsonUtils.writeToJson(users), response.getContent());

	}

	@Test
	public void testPost() throws ServiceException {
		String name = "dummyName";
		String password = "password";

		UserDTO userDTO = UserDTOBuilder.builder().userName(name).password(password).build();
		when(userService.save(userDTO)).thenReturn(userDTO);

		Response response = userRestController.createUser(userDTO);
		assertNotNull(response);
		assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
		assertEquals(HttpStatus.CREATED, response.getStatus());
		assertEquals(JsonUtils.writeToJson(userDTO), response.getContent());

	}

	@Test
	public void testPut() throws ServiceException {
		String name = "dummyName";
		String password = "password";

		UserDTO userDTO = UserDTOBuilder.builder().userName(name).password(password).build();
		when(userService.save(name, userDTO)).thenReturn(userDTO);

		Response response = userRestController.updateUser(name, userDTO);
		assertNotNull(response);
		assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals(JsonUtils.writeToJson(userDTO), response.getContent());

	}

	@Test
	public void testDelete() throws ServiceException {
		String name = "dummyName";
		Response response = userRestController.delete(name);
		verify(userService, times(1)).remove(name);

		assertNotNull(response);
		assertEquals(ContentType.APPLICATION_JSON, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertEquals("\"User dummyName deleted\"", response.getContent());
	}
}
