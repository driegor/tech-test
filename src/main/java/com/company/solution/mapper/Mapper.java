package com.company.solution.mapper;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.common.utils.JsonUtils;
import com.company.mvc.RequestUtils;
import com.company.mvc.enums.ContentType;
import com.google.gson.Gson;

//I would use dozer instead of my own mapper implementation
public class Mapper {

	public <A, B> B map(A a, Class<B> clazzB) {
		Gson gson = new Gson();
		String tmp = gson.toJson(a);
		return gson.fromJson(tmp, clazzB);
	}

	public <A, B> List<B> mapList(List<A> list, Class<B> clazzB) {
		return list.stream().map(a -> map(a, clazzB)).collect(Collectors.toList());
	}

	public <T> T map2class(Map<String, String> map, Class<T> clazz) {
		String json = JsonUtils.writeToJson(map);
		return JsonUtils.readFromJson(json, clazz);

	}

	public <T> T string2class(String input, Class<T> clazz, ContentType contentType)
			throws UnsupportedEncodingException {
		// content type is html
		if (ContentType.TEXT_HTML.equals(contentType)) {
			Map<String, String> map = RequestUtils.getParameters(input);
			return map2class(map, clazz);
		}
		// Json content
		return map(input, clazz);
	}
}
