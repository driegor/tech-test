package com.company.db;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class DataBaseTest {

	private DataBase dataBase;

	@Before
	public void setup() {
		dataBase = new DataBase();
		dataBase.init();
	}

	@Test
	public void testGetAllData() throws SQLException {
		List<Map<String, String>> users = dataBase.executeQuery("select * from USERS");
		assertTrue(users.size() == 4);

		List<Map<String, String>> roles = dataBase.executeQuery("select * from ROLES");
		assertTrue(roles.size() == 4);
	}

}
