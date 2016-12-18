package com.company.solution.repository.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.common.utils.CoreUtils;
import com.company.db.DataBase;
import com.company.solution.domain.User;
import com.company.solution.domain.User.UserBuilder;
import com.company.solution.repository.IUserRepository;

public class UserRepository implements IUserRepository {

	private static final String ROLE = "role";
	private static final String USER_NAME = "userName";
	private static final String PASSWORD = "password";
	private static final String FIND_ALL_QUERY = "SELECT u.username \"userName\",u.password \"password\",ur.role \"role\" FROM USERS U LEFT JOIN USER_ROLES UR ON u.username=ur.username";
	private static final String FIND_BY_NAME_QUERY = "SELECT u.username \"userName\",u.password \"password\",ur.role \"role\" FROM USERS U LEFT JOIN USER_ROLES UR ON u.username=ur.username WHERE u.username =?";
	private static final String FIND_BY_USERNAME_AND_PASSWORD_QUERY = "SELECT u.username \"userName\",u.password \"password\",ur.role \"role\" FROM USERS U LEFT JOIN USER_ROLES UR ON u.username=ur.username WHERE u.username =? and u.password=?";
	private static final String CREATE_USER_QUERY = "INSERT INTO USERS (username,password) values ('%s','%s')";
	private static final String ADD_ROLE_QUERY = "INSERT INTO USER_ROLES (username,role) values ('%s','%s')";
	private static final String DELETE_ROLE_QUERY = "DELETE FROM USER_ROLES WHERE username ='%s' AND role = '%s'";
	private static final String DELETE_USER_ROLES_QUERY = "DELETE FROM USER_ROLES WHERE username ='%s'";
	private static final String UPDATE_USER_QUERY = "UPDATE USERS SET password='%s' WHERE username='%s'";
	private static final String DELETE_USER_QUERY = "DELETE FROM USERS WHERE username ='%s'";

	private DataBase dataBase;

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
	public void init(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public List<User> findAll() throws SQLException {
		List<Map<String, String>> maps = dataBase.executeQuery(FIND_ALL_QUERY);
		return groupByUserName(maps);
	}

	@Override
	public User find(String name) throws SQLException {
		List<Map<String, String>> maps = dataBase.executeQuery(FIND_BY_NAME_QUERY, name);

		if (maps.isEmpty()) {
			return null;
		}
		return groupByUserName(maps).stream().findFirst().get();
	}

	@Override
	public User findByUserNameAndPassword(String userName, String password) throws SQLException {
		List<Map<String, String>> maps = dataBase.executeQuery(FIND_BY_USERNAME_AND_PASSWORD_QUERY, userName, password);

		if (maps.isEmpty()) {
			return null;
		}
		return groupByUserName(maps).stream().findFirst().get();
	}

	@Override
	public User save(User user) throws SQLException {
		try {
			dataBase.executeUpdate(getCreateUserQueries(user));
			return find(user.getUserName());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			throw new SecurityException();
		}
	}

	@Override
	public User update(String userName, User user) throws SQLException {
		try {
			dataBase.executeUpdate(getUpdateUserQueries(userName, user, find(userName).getRoles()));
			return find(user.getUserName());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			throw new SecurityException();
		}
	}

	@Override
	public void delete(String name) throws SQLException {
		String deleteUser = String.format(DELETE_USER_QUERY, name);
		String deleteRoles = String.format(DELETE_USER_ROLES_QUERY, name);
		dataBase.executeUpdate(deleteUser, deleteRoles);
	}

	private String[] getUpdateUserQueries(String userName, User user, List<String> oldRoles)
			throws NoSuchFieldException, IllegalAccessException {

		// create user query
		List<String> updates = new ArrayList<>();
		updates.add(String.format(UPDATE_USER_QUERY, CoreUtils.getFieldValue(user, PASSWORD), userName));

		List<String> newRoles = new ArrayList<>(user.getRoles());

		// we keep the new roles
		newRoles.removeAll(oldRoles);

		// we remove the old ones
		oldRoles.removeAll(user.getRoles());

		newRoles.stream().forEach(role -> updates.add(String.format(ADD_ROLE_QUERY, user.getUserName(), role)));
		oldRoles.stream().forEach(role -> updates.add(String.format(DELETE_ROLE_QUERY, user.getUserName(), role)));

		return updates.toArray(new String[updates.size()]);
	}

	private String[] getCreateUserQueries(User user) throws NoSuchFieldException, IllegalAccessException {

		// create user query
		List<String> inserts = new ArrayList<>();
		inserts.add(String.format(CREATE_USER_QUERY, user.getUserName(), CoreUtils.getFieldValue(user, PASSWORD)));

		// roles queries
		user.getRoles().stream().forEach(role -> inserts.add(String.format(ADD_ROLE_QUERY, user.getUserName(), role)));

		return inserts.toArray(new String[inserts.size()]);
	}

	private List<User> groupByUserName(List<Map<String, String>> maps) {
		List<User> users = new ArrayList<>();

		// group by userName
		Map<String, List<Map<String, String>>> groups = maps.stream()
				.collect(Collectors.groupingBy(p -> p.get(USER_NAME)));

		// get roles
		groups.forEach((userName, v) -> {
			List<String> roles = v.stream().map(p -> p.get(ROLE)).filter(p -> p != null).collect(Collectors.toList());
			// we don't get password value
			users.add(UserBuilder.builder().userName(userName).roles(roles).build());
		});
		return users;
	}

}
