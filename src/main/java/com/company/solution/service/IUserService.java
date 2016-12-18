package com.company.solution.service;

import com.company.solution.common.dto.UserDTO;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.generic.ICrudService;

public interface IUserService extends ICrudService<String, UserDTO> {

	UserDTO findByUserAndPassword(String userName, String password) throws ServiceException;

}
