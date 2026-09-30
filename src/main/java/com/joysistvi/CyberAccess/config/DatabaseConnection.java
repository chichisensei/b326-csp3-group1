package com.joysistvi.CyberAccess.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database connection parameters
    private final static String URL = "jdbc:mysql://localhost:3306/cyber_access";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";

    // docking exception
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }


}
