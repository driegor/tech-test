package com.company.solution.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.db.DataBase;
import com.company.solution.domain.User;
import com.company.solution.domain.User.UserBuilder;

public class UserRepositoryTest {

	private UserRepository repository;

	DataBase db;

	@Before
	public void setup() {
		db = new DataBase();
		db.init();
		repository = UserRepository.getInstance();
		repository.init(db);

	}

	@After
	public void cleanUp() throws SQLException {
		db.shutDown();
	}

	@Test
	public void testGetAll() throws SQLException {

		List<String> userNames = Arrays.asList("user1", "user2", "user3", "admin");

		List<User> users = repository.findAll();
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertEquals(4, users.size());
		assertTrue(users.stream().map(m -> m.getUserName()).collect(Collectors.toList()).containsAll(userNames));

	}

	@Test
	public void testGetByName() throws SQLException {

		String userName = "user2";
		User user = repository.find(userName);
		assertNotNull(user);
		assertEquals(userName, user.getUserName());
	}

	@Test
	public void testCreateUser() throws SQLException {

		List<String> roles = Arrays.asList("PAGE_1", "PAGE_2", "PAGE_3");

		String userName = "newUser";
		User user = repository.find(userName);
		assertNull(user);

		repository.save(UserBuilder.builder().userName(userName).password("123456").roles(roles).build());

		user = repository.find(userName);
		assertNotNull(user);
		assertEquals(userName, user.getUserName());
		assertTrue(user.getRoles().containsAll(roles));
	}

	@Test
	public void updateUser() throws SQLException {

		String userName = "user";

		List<String> oldRoles = Arrays.asList("PAGE_1", "PAGE_2", "PAGE_3");
		List<String> newRoles = Arrays.asList("PAGE_2", "ADMIN");

		assertNull(repository.find(userName));
		repository.save(UserBuilder.builder().userName(userName).roles(oldRoles).password("123456").build());
		User user = repository.find(userName);

		assertNotNull(user);
		assertTrue(oldRoles.equals(user.getRoles()));

		assertNotNull(repository.find(userName));
		repository.update(userName,
				UserBuilder.builder().userName(userName).roles(newRoles).password("123456").build());
		user = repository.find(userName);

		assertNotNull(user);
		assertTrue(newRoles.equals(user.getRoles()));

	}

	@Test
	public void deleteUser() throws SQLException {

		String userName = "makeUpUser";

		assertNull(repository.find(userName));
		repository.save(UserBuilder.builder().userName(userName).password("123456").build());
		assertNotNull(repository.find(userName));
		repository.delete(userName);
		assertNull(repository.find(userName));

	}
}
