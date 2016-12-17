package com.company.solution.repository;

import java.sql.SQLException;
import java.util.List;

public interface ICrudRepository<I, E> {

	List<E> findAll() throws SQLException;

	E find(I id) throws SQLException;

	E save(E e) throws SQLException;

	void delete(I id) throws SQLException;

	E update(I id, E e) throws SQLException;
}
