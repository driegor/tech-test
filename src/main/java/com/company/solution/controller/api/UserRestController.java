package com.company.solution.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.company.mvc.annotations.RequestMapping;
import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.RequestMethod;
import com.company.mvc.exception.ResponseException;
import com.company.mvc.handler.GenericHandler;
import com.company.mvc.response.Response;
import com.company.mvc.response.ResponseWriter;
import com.company.mvc.response.Responses;
import com.company.mvc.security.annotations.PreAuthorize;
import com.company.mvc.security.handler.AuthorizationResponseHandler;
import com.company.solution.common.dto.UserDTO;
import com.company.solution.exception.ServiceException;
import com.company.solution.service.IUserService;
import com.sun.net.httpserver.HttpExchange;

public class UserRestController extends GenericHandler {

	public static final String ROOT_MAPPING = "/api/users";

	private static final Logger LOGGER = Logger.getLogger(UserRestController.class);

	private IUserService userService;

	public UserRestController(IUserService userService) {
		this.userService = userService;
		this.rootMapping = ROOT_MAPPING;
	}

	// override getAuthorizationResponseHandler method
	@Override
	protected AuthorizationResponseHandler getAuthorizationResponseHandler() {
		return new AuthorizationResponseHandler() {

			@Override
			public void onFail(HttpExchange exchange) throws IOException {

				String content = String.format("{message:\'%s\'}", "Forbidden access");
				try {
					new ResponseWriter().write(exchange, Responses.forbidden(content, ContentType.APPLICATION_JSON));
				} catch (ResponseException e) {
					LOGGER.error("Error writting to response " + e.getMessage());
				}
			}
		};
	}

	@RequestMapping
	public Response getAll(Map<String, String> params) throws ServiceException {
		LOGGER.info("Get all users");
		List<UserDTO> users = userService.getAll();
		return Responses.success(users, ContentType.APPLICATION_JSON);
	}

	@RequestMapping(pattern = "/(.*)")
	public Response getUser(String name, Map<String, String> params) throws ServiceException {
		LOGGER.info(String.format("Get user [%s]", name));
		UserDTO user = userService.get(name);
		return Responses.success(user, ContentType.APPLICATION_JSON);
	}

	@PreAuthorize(role = "ADMIN")
	@RequestMapping(method = RequestMethod.POST, payLoad = UserDTO.class, contentType = ContentType.APPLICATION_JSON)
	public Response createUser(UserDTO user, Map<String, String> params) throws ServiceException {
		LOGGER.info(String.format("Create user [%s]", user.getUserName()));
		return Responses.created(userService.save(user), ContentType.APPLICATION_JSON);
	}

	@PreAuthorize(role = "ADMIN")
	@RequestMapping(method = RequestMethod.PUT, pattern = "/(.*)", payLoad = UserDTO.class, contentType = ContentType.APPLICATION_JSON)
	public Response updateUser(String name, UserDTO user, Map<String, String> params) throws ServiceException {
		LOGGER.info(String.format("Update user [%s]", name));
		return Responses.success(userService.save(name, user), ContentType.APPLICATION_JSON);
	}

	@PreAuthorize(role = "ADMIN")
	@RequestMapping(pattern = "/(.*)", method = RequestMethod.DELETE)
	public Response delete(String name, Map<String, String> params) throws ServiceException {
		LOGGER.info(String.format("Delete user [%s]", name));
		userService.remove(name);
		return Responses.success(String.format("User %s deleted", name), ContentType.APPLICATION_JSON);
	}

}
