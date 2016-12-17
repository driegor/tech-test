package com.company.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;

public class DataBase {

	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String SCRIPT_FILE_NAME = "script.sql";
	private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final Logger LOGGER = Logger.getLogger(DataBase.class.getName());

	private JdbcConnectionPool getPool() throws ClassNotFoundException {
		Class.forName(DB_DRIVER);
		return JdbcConnectionPool.create(DB_URL, "", "");
	}

	public void init() {
		JdbcConnectionPool cp = null;
		Connection conn = null;

		try (InputStream in = ClassLoader.getSystemResourceAsStream(SCRIPT_FILE_NAME)) {
			cp = getPool();
			conn = cp.getConnection();
			RunScript.execute(conn, new InputStreamReader(in));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			LOGGER.severe("Error initializing DB. " + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.severe("Error closing connection" + e);
			}
			if (cp != null) {
				cp.dispose();
			}
		}
	}

	public void shutDown() {
		executeUpdate("SHUTDOWN");
	}

	public List<Map<String, String>> executeQuery(String query, String... params) {
		JdbcConnectionPool cp = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<Map<String, String>> list = new ArrayList<>();
		try {
			cp = getPool();
			conn = cp.getConnection();

			ps = conn.prepareStatement(query);
			if (params != null && params.length > 0) {
				for (int i = 0; i < params.length; i++) {
					ps.setString(i + 1, params[i]);
				}
			}

			rs = ps.executeQuery();

			list = getMapList(rs);

		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.severe("Error executing query" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ps != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.severe("Error closing connection" + e);
			}
			if (cp != null) {
				cp.dispose();
			}
		}
		return list;

	}

	public List<Integer> executeUpdate(String... queries) {
		JdbcConnectionPool cp = null;
		Connection conn = null;
		PreparedStatement ps = null;
		List<Integer> ints = new ArrayList<>();
		try {
			cp = getPool();
			conn = cp.getConnection();

			for (String query : queries) {
				ps = conn.prepareStatement(query);
				ints.add(ps.executeUpdate());
			}

		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.severe("Error executing query" + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ps != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.severe("Error closing connection" + e);
			}
			if (cp != null) {
				cp.dispose();
			}
		}
		return ints;
	}

	private List<Map<String, String>> getMapList(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		Set<String> names = new HashSet<>();

		// The column count starts from 1
		IntStream.rangeClosed(1, columnCount).forEach(i -> {
			try {
				names.add(rsmd.getColumnLabel(i));

			} catch (SQLException e) {
				LOGGER.severe("Error retrieving resulset column name" + e);
			}
		});

		List<Map<String, String>> mapList = new ArrayList<>();

		while (rs.next()) {

			Map<String, String> map = new HashMap<>();
			names.stream().forEach(name -> {
				try {

					String value = rs.getString(name);
					map.put(name, value);

					LOGGER.fine(map.toString());

				} catch (SQLException e) {
					LOGGER.severe("Error retrieving resulset column value" + e);
				}
			});
			mapList.add(map);
		}
		return mapList;
	}

}
