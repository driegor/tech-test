package com.company.solution.repository.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.company.db.DataBase;
import com.company.solution.domain.User;
import com.company.solution.domain.User.UserBuilder;
import com.company.solution.mapper.Mapper;

public class UserRepositoryTest {

	private UserRepository repository;

	DataBase db;

	@Before
	public void setup() {
		db = new DataBase();
		db.init();
		repository = UserRepository.getInstance();
		repository.init(db, new Mapper());

	}

	@After
	public void cleanUp() {
		db.shutDown();
	}

	@Test
	public void testGetAll() {

		List<String> userNames = Arrays.asList("user1", "user2", "user3", "admin");

		List<User> users = repository.findAll();
		assertNotNull(users);
		assertFalse(users.isEmpty());
		assertTrue(users.size() == 4);
		assertTrue(users.stream().map(m -> m.getUserName()).collect(Collectors.toList()).containsAll(userNames));

	}

	@Test
	public void testGetByName() {

		String userName = "user2";
		User user = repository.find(userName);
		assertNotNull(user);
		assertEquals(userName, user.getUserName());
	}

	@Test
	public void testCreateUser() {

		String userName = "newUser";
		User user = repository.find(userName);
		assertNull(user);

		repository.save(UserBuilder.builder().userName(userName).password("123456").build());

		user = repository.find(userName);
		assertNotNull(user);
		assertEquals(userName, user.getUserName());
	}

	@Test
	public void updateUser() {

		String oldUser = "oldUser";
		String newUser = "newUser";

		assertNull(repository.find(oldUser));
		repository.save(UserBuilder.builder().userName(oldUser).password("123456").build());
		assertNotNull(repository.find(oldUser));
		assertNull(repository.find(newUser));
		repository.update(oldUser, UserBuilder.builder().userName(newUser).build());
		assertNotNull(repository.find(newUser));

	}

	@Test
	public void deleteUser() {

		String userName = "makeUpUser";

		assertNull(repository.find(userName));
		repository.save(UserBuilder.builder().userName(userName).password("123456").build());
		assertNotNull(repository.find(userName));
		repository.delete(userName);
		assertNull(repository.find(userName));

	}
}
