package com.company.solution.repository.impl;

import java.util.List;

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

		List<User> users = repository.findAll();
		System.out.println(users);
	}

}
