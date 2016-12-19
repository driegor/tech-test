package com.company.solution.controller.api;

import java.util.List;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;
import com.company.mvc.response.Responses;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;

public class UserRestController extends GenericHandler {

	public static final String ROOT_MAPPING = "/api/users";

	private static final Logger LOGGER = Logger.getLogger(UserRestController.class);

	private IUserService userService;

	public UserRestController(IUserService userService) {
		this.userService = userService;
		this.rootMapping = ROOT_MAPPING;
	}

	@RequestMapping
	public Response getAll() throws ServiceException {
		LOGGER.info("Get all users");
		List<UserDTO> users = userService.getAll();
		return Responses.success(users, ContentType.APPLICATION_JSON);
	}

	@RequestMapping(pattern = "/(.*)")
	public Response getUser(String name) throws ServiceException {
		LOGGER.info(String.format("Get user [%s]", name));
		UserDTO user = userService.get(name);
		return Responses.success(user, ContentType.APPLICATION_JSON);
	}

	@RequestMapping(method = RequestMethod.POST, payLoad = UserDTO.class)
	public Response createUser(UserDTO user) throws ServiceException {
		LOGGER.info(String.format("Create user [%s]", user.getUserName()));
		return Responses.created(userService.save(user), ContentType.APPLICATION_JSON);
	}

	@RequestMapping(method = RequestMethod.PUT, pattern = "/(.*)", payLoad = UserDTO.class)
	public Response updateUser(String name, UserDTO user) throws ServiceException {
		LOGGER.info(String.format("Update user [%s]", name));
		return Responses.success(userService.save(name, user), ContentType.APPLICATION_JSON);
	}

	@RequestMapping(pattern = "/(.*)", method = RequestMethod.DELETE)
	public Response delete(String name) throws ServiceException {
		LOGGER.info(String.format("Delete user [%s]", name));
		userService.remove(name);
		return Responses.success(String.format("User %s deleted", name), ContentType.APPLICATION_JSON);
	}

}
