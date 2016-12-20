package com.company.solution.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import com.company.mockito.MockitoTest;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.common.dto.UserDTO.UserDTOBuilder;
import com.company.solution.domain.User;
import com.company.solution.domain.User.UserBuilder;
import com.company.solution.exception.ServiceException;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.IUserRepository;

public class UserServiceTest extends MockitoTest {

	@Mock
	private IUserRepository userRepository;

	private Mapper mapper = new Mapper();

	private UserService userService;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.userService = UserService.getInstance();
		userService.init(userRepository, mapper);
	}

	@Test
	public void testGet() throws SQLException, ServiceException {
		String name = "dummyName";
		String password = "dummyPassword";

		User entity = UserBuilder.builder().userName(name).password(password).build();
		UserDTO expectedDTO = UserDTOBuilder.builder().userName(name).password(password).build();

		when(userRepository.find(name)).thenReturn(entity);
		UserDTO dto = userService.get(name);

		assertNotNull(dto);
		assertThat(expectedDTO, new ReflectionEquals(dto, "roles"));

	}

	@Test
	public void testGetAll() throws SQLException, ServiceException {
		String name = "dummyName";
		String password = "password";

		//// @formatter:off
		List<User> users = IntStream.rangeClosed(1, 5)
				.mapToObj(v -> UserBuilder.builder().userName(name + v).password(password + v).build())
				.collect(Collectors.toList());
		// @formatter:on

		when(userRepository.findAll()).thenReturn(users);
		List<UserDTO> dtos = userService.getAll();

		assertNotNull(dtos);
		assertTrue(dtos.size() == 5);

		//// @formatter:off
		assertTrue(IntStream.range(0, users.size())
				.allMatch(i -> new ReflectionEquals(mapper.map(users.get(i), UserDTO.class)).matches(dtos.get(i))));
		// @formatter:on

	}

	@Test
	public void testSave() throws SQLException, ServiceException {
		String name = "dummyName";
		String password = "password";

		UserDTO dto = UserDTOBuilder.builder().userName(name).password(password).build();
		User user = UserBuilder.builder().userName(name).password(password).build();

		when(userRepository.save(refEq(user))).thenReturn(user);
		UserDTO savedDTO = userService.save(dto);

		assertNotNull(dto);
		assertThat(savedDTO, new ReflectionEquals(dto, "roles"));

	}

	@Test
	public void testUpdate() throws SQLException, ServiceException {
		String name = "dummyName";
		String password = "password";

		UserDTO dto = UserDTOBuilder.builder().userName(name).password(password).build();
		User user = UserBuilder.builder().userName(name).password(password).build();

		when(userRepository.update(eq(name), refEq(user))).thenReturn(user);
		UserDTO savedDTO = userService.save(name, dto);

		assertNotNull(dto);
		assertThat(savedDTO, new ReflectionEquals(dto, "roles"));

	}

	@Test
	public void testDelete() throws SQLException, ServiceException {
		String name = "dummyName";
		when(userRepository.delete(name)).thenReturn(Boolean.TRUE);
		userService.remove(name);
		verify(userRepository, times(1)).delete(name);

	}
}
