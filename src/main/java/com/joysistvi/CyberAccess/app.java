package com.joysistvi.CyberAccess;

import com.joysistvi.CyberAccess.config.DatabaseConnection;

import java.sql.SQLException;

public class app {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        try {
            db.connect();
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }
}
