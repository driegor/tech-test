package com.company.solution.service.impl;

import com.company.solution.common.dto.UserDTO;
import com.company.solution.domain.User;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.IUserRepository;
import com.company.solution.service.IUserService;
import com.company.solution.service.impl.generic.CrudService;

public class UserService extends CrudService<String, User, UserDTO, IUserRepository> implements IUserService {

	private static volatile UserService service;

	// private constructor
	private UserService() {
	}

	// static method to get instance
	public static UserService getInstance() {
		if (service == null) {
			synchronized (UserService.class) {
				if (service == null) {
					service = new UserService();
				}
			}
		}
		return service;
	}

	// I would use a DI framework to inject these beans
	public void init(IUserRepository userRepository, Mapper mapper) {
		this.crudRepository = userRepository;
		this.mapper = mapper;
		this.dtoClazz = UserDTO.class;
		this.entityClazz = User.class;
	}

}
