package com.kgl.KglServices.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MssqlConnection {
	private static final Logger logger = LoggerFactory.getLogger(MssqlConnection.class);
	
	public static Connection getCon(String dbURL, String userName, String passWord) {
		

		// String dbURL =
		// "jdbc:sqlserver://172.19.27.162:1433;databaseName=KLF_S3G_LIVE";
		// String userName = "s3g";
		// String passWord = "Cfem6u2mf6@Te";//"Cfem6u2mf6@T"

		// local
		// String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=S3G_TEST";
		// String userName = "s3g";
		// String passWord = "!venkey2023";// s3g2024 (recently changed)

		// 10.10.1.12
		// String dbURL =
		// "jdbc:sqlserver://202.53.81.10:1433;databaseName=KLF_S3G_LIVE";
		// String userName = "sa";
		// String passWord = "Cfem6u2mf6@T";//"!venkey2023";//"Cfem6u2mf6@T"
		// Cfem6u2mf6@Te

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbURL, userName, passWord);
			if (conn != null) {
				logger.info("Connected");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		System.out.println(conn);
		return conn;

	}
}
