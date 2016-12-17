package com.company.solution.service.impl.generic;

import java.util.List;

import com.company.solution.mapper.Mapper;
import com.company.solution.repository.ICrudRepository;
import com.company.solution.service.generic.ICrudService;

public class CrudService<I, E, DTO, C extends ICrudRepository<I, E>> implements ICrudService<I, DTO> {

	protected Mapper mapper;
	protected C crudRepository;
	protected Class<DTO> dtoClazz;
	protected Class<E> entityClazz;

	@Override
	public DTO get(I id) {
		return mapper.map(crudRepository.find(id), dtoClazz);
	}

	@Override
	public List<DTO> getAll() {
		return mapper.mapList(crudRepository.findAll(), dtoClazz);

	}

	@Override
	public DTO save(DTO dto) {
		E entity = mapper.map(dto, entityClazz);
		return mapper.map(crudRepository.save(entity), dtoClazz);
	}

	@Override
	public DTO save(I id, DTO dto) {
		E entity = mapper.map(dto, entityClazz);
		return mapper.map(crudRepository.update(id, entity), dtoClazz);
	}

	@Override
	public void remove(I id) {
		crudRepository.delete(id);
	}
}
