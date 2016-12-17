package com.company.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.company.common.utils.CoreUtils;

public class CoreUtilsTest {

	@Test
	public void testMatchAndGroup() {

		String value = "dummyValue";
		String pathPattern = "^/api/dummy/([a-zA-Z0-9]+)$";
		String path = String.format("/api/dummy/%s", value);
		Pattern pattern = Pattern.compile(pathPattern);
		Matcher matcher = pattern.matcher(path);
		assertTrue(matcher.find());
		String firstGroup = CoreUtils.getFirstMatch(matcher);
		assertNotNull(firstGroup);
		assertEquals(value, firstGroup);
	}

	@Test
	public void testDoesntMatch() {

		String value = "dummyValue";
		String pathPattern = "^/api/dummy/([a-zA-Z0-9]+)$";
		String path = String.format("/api/other-dummy/%s", value);
		Pattern pattern = Pattern.compile(pathPattern);
		Matcher matcher = pattern.matcher(path);
		assertFalse(matcher.find());

	}

	@Test
	public void testMatchWithoutGroup() {

		String pathPattern = "^/api/dummy/$";
		String path = "/api/dummy/";
		Pattern pattern = Pattern.compile(pathPattern);
		Matcher matcher = pattern.matcher(path);
		assertTrue(matcher.find());
		String firstGroup = CoreUtils.getFirstMatch(matcher);
		assertNull(firstGroup);
	}
}
