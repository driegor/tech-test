package com.company.solution.repository;

import java.sql.SQLException;

import com.company.solution.domain.User;

public interface IUserRepository extends ICrudRepository<String, User> {

	User findByUserNameAndPassword(String userName, String password) throws SQLException;

}
