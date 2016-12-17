package com.company.solution.service.generic;

import java.util.List;

import com.company.solution.exception.ServiceException;

public interface ICrudService<I, DTO> {

	DTO get(I id) throws ServiceException;

	List<DTO> getAll() throws ServiceException;

	DTO save(DTO dto) throws ServiceException;

	void remove(I id) throws ServiceException;

	DTO save(I id, DTO dto) throws ServiceException;
}
