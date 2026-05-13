package com.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/portfolio_db?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "CHANGE_ME";

    private static final String URL = getEnvOrDefault("PMS_DB_URL", DEFAULT_URL);
    private static final String USER = getEnvOrDefault("PMS_DB_USER", DEFAULT_USER);
    private static final String PASSWORD = getEnvOrDefault("PMS_DB_PASSWORD", DEFAULT_PASSWORD);

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        if ("CHANGE_ME".equals(PASSWORD)) {
            throw new SQLException("Set PMS_DB_PASSWORD environment variable before starting the app");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
