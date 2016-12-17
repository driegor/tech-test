package com.company.solution.service.impl;

import com.company.solution.service.IAuthService;

public class AuthService implements IAuthService {

	private static volatile AuthService service;

	// private constructor
	private AuthService() {
	}

	// static method to get instance
	public static AuthService getInstance() {
		if (service == null) {
			synchronized (AuthService.class) {
				if (service == null) {
					service = new AuthService();
				}
			}
		}
		return service;
	}

}
