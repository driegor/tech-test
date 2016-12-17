package com.company.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class CoreUtils {

	public static <A extends Annotation> Collection<Method> getAnnotatedMethods(Class<A> annotationClass,
			Object object) {

		if (object == null) {
			return new ArrayList<>();
		}

		// get the list of methods of this object that match the annotation
		return Arrays.stream(object.getClass().getMethods())
				.filter(method -> method.getAnnotation(annotationClass) != null).collect(Collectors.toList());
	}

	// get first match
	public static String getFirstMatch(Matcher matcher) {

		String firstGroup = null;
		if (matcher.groupCount() > 0) {
			firstGroup = matcher.group(1);
		}
		return firstGroup;
	}

	// get string from stream
	public static String fromStream(InputStream input) throws IOException {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			return buffer.lines().collect(Collectors.joining("\n"));
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T requestBodyToPostData(String requestBody, Class<T> requestBodyClass) {
		// class extends from String , return the requesBody without any
		// transformation
		if (String.class.isAssignableFrom(requestBodyClass)) {
			return (T) requestBody;
		}
		// Json content
		return JsonUtils.readFromJson(requestBody, requestBodyClass);
	}
}
