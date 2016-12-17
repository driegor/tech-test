package com.company.solution.service.generic;

import java.util.List;

public interface ICrudService<I, DTO> {

	DTO get(I id);

	List<DTO> getAll();

	DTO save(DTO dto);

	void remove(I id);

	DTO save(I id, DTO dto);
}
