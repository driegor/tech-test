package com.company.solution.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.company.db.DataBase;
import com.company.mvc.security.auth.IAuthService;
import com.company.mvc.security.filter.PropagateArgumentsFilter;
import com.company.mvc.security.filter.SessionAuthenticationFilter;
import com.company.mvc.security.session.data.SessionData;
import com.company.solution.controller.api.UserRestController;
import com.company.solution.controller.page.LoginController;
import com.company.solution.controller.page.PageController;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.impl.UserRepository;
import com.company.solution.service.IUserService;
import com.company.solution.service.impl.AuthService;
import com.company.solution.service.impl.UserService;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class Server {
	private static final Logger LOGGER = Logger.getLogger(Server.class);

	private static final int PORT = 8080;

	private Server() {
	}

	public static void main(String[] args) throws IOException {

		int port = (args != null && args.length > 0) ? Integer.valueOf(args[0]) : PORT;

		HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

		// load beans context
		Map<Object, Object> context = loadContext();

		// create controllers
		createUserRestController(context, httpServer);
		createLoginController(context, httpServer);
		createPageController(context, httpServer);

		httpServer.setExecutor(null);
		httpServer.start();

		LOGGER.info("Server listening at port " + port);
	}

	private static void createUserRestController(Map<Object, Object> context, HttpServer server) {

		IUserService userService = (IUserService) context.get(IUserService.class);
		HttpContext httpPageContext = server.createContext(UserRestController.ROOT_MAPPING,
				new UserRestController(userService));

		// add filters

	}

	private static void createLoginController(Map<Object, Object> context, HttpServer server) {

		IAuthService authService = (IAuthService) context.get(IAuthService.class);
		HttpContext httpPageContext = server.createContext(LoginController.ROOT_MAPPING,
				new LoginController(authService));

		// add filters
		httpPageContext.getFilters().add(new PropagateArgumentsFilter());
		httpPageContext.getFilters().add(new SessionAuthenticationFilter(authService));

	}

	private static void createPageController(Map<Object, Object> context, HttpServer server) {
		IAuthService authService = (IAuthService) context.get(IAuthService.class);
		HttpContext httpPageContext = server.createContext(PageController.ROOT_MAPPING, new PageController());

		// add filters
		httpPageContext.getFilters().add(new PropagateArgumentsFilter());
		httpPageContext.getFilters().add(new SessionAuthenticationFilter(authService));
	}

	private static Map<Object, Object> loadContext() {
		Map<Object, Object> context = new HashMap<>();

		// create database
		DataBase dataBase = new DataBase();
		dataBase.init();

		// create mapper
		Mapper mapper = new Mapper();

		// create repositories
		UserRepository userRepository = UserRepository.getInstance();
		userRepository.init(dataBase);

		// create services
		UserService userService = UserService.getInstance();
		userService.init(userRepository, mapper);
		context.put(IUserService.class, userService);

		AuthService authService = AuthService.getInstance();
		authService.init(userService, new SessionData());
		context.put(IAuthService.class, authService);
		return context;
	}
}
