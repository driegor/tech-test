package com.company.solution.repository.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.company.db.DataBase;
import com.company.solution.domain.User;
import com.company.solution.mapper.Mapper;

public class UserRepositoryTest {

	private UserRepository repository;

	@Before
	public void setup() {
		DataBase dataBase = new DataBase();
		dataBase.init();
		repository = UserRepository.getInstance();
		repository.init(dataBase, new Mapper());

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

}
