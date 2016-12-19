package com.company.solution.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.company.data.DummyPostData;
import com.company.mvc.enums.ContentType;
import com.company.solution.form.LoginForm;

public class MapperTest {

	@Test
	public void testMapToClass() {
		Mapper mapper = new Mapper();

		Map<String, String> map = new HashMap<>();
		String userName = "userName";
		String password = "12323232";
		String postLogin = "/page/Page 1";

		map.put("userName", userName);
		map.put("password", password);
		map.put("postLogin", postLogin);
		LoginForm form = mapper.map2class(map, LoginForm.class);

		assertNotNull(form);
		assertEquals(userName, form.getUserName());
		assertEquals(password, form.getPassword());
		assertEquals(postLogin, form.getPostLogin());
	}

	@Test
	public void testStringToClass() throws UnsupportedEncodingException {

		String query = "userName=%s&password=%s&postLogin=%s";
		Mapper mapper = new Mapper();

		String userName = "userName";
		String password = "12323232";
		String postLogin = "/pages/Page 1";

		LoginForm form = mapper.string2class(String.format(query, userName, password, postLogin), LoginForm.class,
				ContentType.TEXT_HTML);

		assertNotNull(form);
		assertEquals(userName, form.getUserName());
		assertEquals(password, form.getPassword());
		assertEquals(postLogin, form.getPostLogin());
	}

	@Test
	public void testStringToClassEncodedParams() throws UnsupportedEncodingException {

		String query = "userName=%s&password=%s&postLogin=%s";
		Mapper mapper = new Mapper();

		String userName = "userName";
		String password = "12323232";
		String postLogin = "%2Fpages%2FPage+1";
		String postLoginDecoded = "/pages/Page 1";

		LoginForm form = mapper.string2class(String.format(query, userName, password, postLogin), LoginForm.class,
				ContentType.TEXT_HTML);

		assertNotNull(form);
		assertEquals(userName, form.getUserName());
		assertEquals(password, form.getPassword());
		assertEquals(postLoginDecoded, form.getPostLogin());
	}

	@Test
	public void testStringToClassJson() throws UnsupportedEncodingException {

		String postData = "{\"mail\":\"driegor\",\"name\":\"dani\"}";
		Mapper mapper = new Mapper();

		String mail = "driegor";
		String name = "dani";

		DummyPostData data = mapper.string2class(postData, DummyPostData.class, ContentType.APPLICATION_JSON);

		assertNotNull(data);
		assertEquals(mail, data.getMail());
		assertEquals(name, data.getName());

	}

}
