package com.company.solution.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.db.DataBase;
import com.company.solution.domain.User;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.IUserRepository;

public class UserRepository implements IUserRepository {

	private static final String FIND_ALL_QUERY = "SELECT username \"userName\",password \"password\" FROM USERS";
	private static final String FIND_BY_NAME_QUERY = "SELECT username \"userName\",password \"password\" FROM USERS WHERE username =?";

	private DataBase dataBase;
	private Mapper mapper;

	private static volatile UserRepository intance;

	// private constructor
	private UserRepository() {
	}

	// static method to get instance
	public static UserRepository getInstance() {
		if (intance == null) {
			synchronized (UserRepository.class) {
				if (intance == null) {
					intance = new UserRepository();
				}
			}
		}
		return intance;
	}

	// I would use a DI framework to inject these beans
	public void init(DataBase dataBase, Mapper mapper) {
		this.dataBase = dataBase;
		this.mapper = mapper;
	}

	@Override
	public List<User> findAll() {
		List<Map<String, String>> maps = dataBase.executeQuery(FIND_ALL_QUERY);
		return maps.stream().map(v -> mapper.mapHash(v, User.class)).collect(Collectors.toList());
	}

	@Override
	public User find(String name) {
		List<Map<String, String>> maps = dataBase.executeQuery(FIND_BY_NAME_QUERY, name);
		return maps.stream().map(v -> mapper.mapHash(v, User.class)).collect(Collectors.toList()).stream().findFirst()
				.get();
	}

	@Override
	public User save(User e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public User update(String id, User e) {
		// TODO Auto-generated method stub
		return null;
	}

}
