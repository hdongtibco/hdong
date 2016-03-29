package com.tibco.tct.fom.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnectionTester {
	
	public static Connection getConnection(String driver, String url, Properties info) throws Exception {
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, info);

		return conn;
	}
}
