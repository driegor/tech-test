package com.company.solution.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.company.common.utils.JsonUtils;
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

	public <T> T mapHash(Map<String, String> map, Class<T> clazz) {
		String json = JsonUtils.writeToJson(map);
		return JsonUtils.readFromJson(json, clazz);

	}

}
