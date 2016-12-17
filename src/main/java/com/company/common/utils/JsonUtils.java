
package com.company.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

	public static String writeToJson(Object content) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(content);
	}

	public static <T> T readFromJson(String content, Class<T> clazz) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.fromJson(content, clazz);
	}
}
