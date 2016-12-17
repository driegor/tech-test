package com.company.common.utils;

import java.lang.reflect.Field;

public class SecurityUtils {

	public static String getFieldValue(Object object, String fieldName)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = object.getClass().getDeclaredField(fieldName);
		f.setAccessible(true);// Very important, this allows the setting to//
								// work.
		return (String) f.get(object);
	}

}
