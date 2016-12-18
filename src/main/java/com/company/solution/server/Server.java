package com.company.solution.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.company.db.DataBase;
import com.company.mvc.security.auth.IAuthService;
import com.company.solution.controller.page.LoginController;
import com.company.solution.controller.page.PageController;
import com.company.solution.mapper.Mapper;
import com.company.solution.repository.impl.UserRepository;
import com.company.solution.service.IUserService;
import com.company.solution.service.impl.AuthService;
import com.company.solution.service.impl.UserService;
import com.sun.net.httpserver.HttpServer;

public class Server {
	private static final Logger LOGGER = Logger.getLogger(Server.class);

	private static final int PORT = 8080;

	private Server() {
	}

	public static void main(String[] args) throws IOException {

		Map<Object, Object> context = loadContext();

		int port = (args != null && args.length > 0) ? Integer.valueOf(args[0]) : PORT;

		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

		// create controllers and put it in the context
		server.createContext(PageController.ROOT_MAPPING,
				new PageController((IAuthService) context.get(IAuthService.class)));
		server.createContext(LoginController.ROOT_MAPPING,
				new LoginController((IAuthService) context.get(IAuthService.class)));

		server.setExecutor(Executors.newCachedThreadPool());

		server.start();
		LOGGER.info("Score server listening at port " + port);
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
		authService.init(userService);
		context.put(IAuthService.class, authService);
		return context;

	}

}
