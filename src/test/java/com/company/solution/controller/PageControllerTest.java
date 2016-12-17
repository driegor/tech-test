package com.company.solution.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.company.mvc.enums.ContentType;
import com.company.mvc.enums.HttpStatus;
import com.company.mvc.response.Response;

public class PageControllerTest {

	@Test
	public void testRenderPage() {
		PageController pageController = new PageController();
		Response response = pageController.getPage(String.valueOf(1));

		assertEquals(ContentType.TEXT_HTML, response.getContentType());
		assertEquals(HttpStatus.OK, response.getStatus());
		assertNotNull(response.getContent());

	}

}
