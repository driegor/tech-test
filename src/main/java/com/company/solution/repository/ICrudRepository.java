package com.company.solution.repository;

import java.util.List;

public interface ICrudRepository<I, E> {

	List<E> findAll();

	E find(I id);

	E save(E e);

	void delete(I id);

	E update(I id, E e);
}
