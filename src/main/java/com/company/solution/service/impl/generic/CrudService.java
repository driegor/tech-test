package com.company.solution.service.impl.generic;

import java.sql.SQLException;
import java.util.List;

import com.company.solution.exception.ServiceException;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.ICrudRepository;
import com.company.solution.service.generic.ICrudService;

public class CrudService<I, E, DTO, C extends ICrudRepository<I, E>> implements ICrudService<I, DTO> {

	protected Mapper mapper;
	protected C repository;
	protected Class<DTO> dtoClazz;
	protected Class<E> entityClazz;

	@Override
	public DTO get(I id) throws ServiceException {
		try {
			return mapper.map(repository.find(id), dtoClazz);
		} catch (SQLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<DTO> getAll() throws ServiceException {
		try {
			return mapper.mapList(repository.findAll(), dtoClazz);
		} catch (SQLException e) {
			throw new ServiceException(e);

		}

	}

	@Override
	public DTO save(DTO dto) throws ServiceException {
		E entity = mapper.map(dto, entityClazz);
		try {
			return mapper.map(repository.save(entity), dtoClazz);
		} catch (SQLException e) {
			throw new ServiceException(e);

		}
	}

	@Override
	public DTO save(I id, DTO dto) throws ServiceException {
		E entity = mapper.map(dto, entityClazz);
		try {
			return mapper.map(repository.update(id, entity), dtoClazz);
		} catch (SQLException e) {
			throw new ServiceException(e);

		}
	}

	@Override
	public void remove(I id) throws ServiceException {
		try {
			repository.delete(id);
		} catch (SQLException e) {
			throw new ServiceException(e);

		}
	}
}
