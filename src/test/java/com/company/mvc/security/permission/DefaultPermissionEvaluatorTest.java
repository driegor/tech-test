package com.company.mvc.security.permission;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mock;

import com.company.mockito.MockitoTest;
import com.company.mvc.security.annotations.PreAuthorize;
import com.company.mvc.security.auth.Principal;
import com.company.solution.common.dto.GlobalConst;
import com.sun.net.httpserver.HttpExchange;

public class DefaultPermissionEvaluatorTest extends MockitoTest {

	@Mock
	HttpExchange exchange;

	@Test
	public void testPermissionEvaluatorWithoutPreAuthorize() {
		PermissionEvaluator permissionEvaluator = new DefaultPermissionEvaluator();
		boolean hasPermission = permissionEvaluator.hasPermission(null, exchange, "Test1");
		assertTrue(hasPermission);
	}

	@Test
	public void testPermissionEvaluatorWithBinding() {
		PermissionEvaluator permissionEvaluator = new DefaultPermissionEvaluator();

		PreAuthorize preAuthorize = new PreAuthorize() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return PreAuthorize.class;
			}

			@Override
			public boolean useBindingValue() {
				return true;
			}

			@Override
			public String role() {
				return "Role %s";
			}
		};

		Principal principal = new Principal("dummy", Arrays.asList("Role Test1", "Role Test2"));
		when(exchange.getAttribute(GlobalConst.PRINCIPAL)).thenReturn(principal);

		boolean hasPermission = permissionEvaluator.hasPermission(preAuthorize, exchange, "Test1");

		assertTrue(hasPermission);

	}

	@Test
	public void testPermissionEvaluatorWithBindingInvalidRole() {
		PermissionEvaluator permissionEvaluator = new DefaultPermissionEvaluator();

		PreAuthorize preAuthorize = new PreAuthorize() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return PreAuthorize.class;
			}

			@Override
			public boolean useBindingValue() {
				return true;
			}

			@Override
			public String role() {
				return "Role %s";
			}
		};

		Principal principal = new Principal("dummy", Arrays.asList("Role Test1", "Role Test2"));
		when(exchange.getAttribute(GlobalConst.PRINCIPAL)).thenReturn(principal);

		boolean hasPermission = permissionEvaluator.hasPermission(preAuthorize, exchange, "Test");

		assertFalse(hasPermission);

	}

	@Test
	public void testPermissionEvaluatorWithoutBinding() {
		PermissionEvaluator permissionEvaluator = new DefaultPermissionEvaluator();

		PreAuthorize preAuthorize = new PreAuthorize() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return PreAuthorize.class;
			}

			@Override
			public boolean useBindingValue() {
				return false;
			}

			@Override
			public String role() {
				return "Role Test1";
			}
		};

		Principal principal = new Principal("dummy", Arrays.asList("Role Test1", "Role Test2"));
		when(exchange.getAttribute(GlobalConst.PRINCIPAL)).thenReturn(principal);

		boolean hasPermission = permissionEvaluator.hasPermission(preAuthorize, exchange, "Test1");

		assertTrue(hasPermission);

	}

	@Test
	public void testPermissionEvaluatorWithoutPrincipal() {
		PermissionEvaluator permissionEvaluator = new DefaultPermissionEvaluator();

		PreAuthorize preAuthorize = new PreAuthorize() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return PreAuthorize.class;
			}

			@Override
			public boolean useBindingValue() {
				return true;
			}

			@Override
			public String role() {
				return "Role %s";
			}
		};

		boolean hasPermission = permissionEvaluator.hasPermission(preAuthorize, exchange, "Test1");

		assertFalse(hasPermission);

	}

}
