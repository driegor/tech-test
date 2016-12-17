package com.company.db;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataBaseTest {

	private DataBase db;

	@Before
	public void setup() {
		db = new DataBase();
		db.init();
	}

	@After
	public void cleanUp() throws SQLException {
		// db.shutDown();
	}

	@Test
	public void testGetAllData() throws SQLException {
		List<Map<String, String>> users = db.executeQuery("select * from USERS");
		assertTrue(users.size() == 4);

		List<Map<String, String>> roles = db.executeQuery("select * from ROLES");
		assertTrue(roles.size() == 4);
	}

}
