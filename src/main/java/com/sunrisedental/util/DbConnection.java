package com.sunrisedental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {

    private static DbConnection instance;

    private static final String URL =
            "jdbc:mysql://localhost:3306/sunrise_dental?useSSL=false&serverTimezone=UTC";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "";

    private DbConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "MySQL JDBC driver was not found.",
                    exception
            );
        }
    }

    public static synchronized DbConnection getInstance() {

        if (instance == null) {
            instance = new DbConnection();
        }

        return instance;
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}
