package com.joysistvi.CyberAccess;

import com.joysistvi.CyberAccess.cliview.ComputersView;
import com.joysistvi.CyberAccess.cliview.RatesView;
import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.controller.ComputersController;
import com.joysistvi.CyberAccess.controller.RatesController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class app {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        // Check the database first
        try (Connection conn = db.connect()) {
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println("Cannot connect to the database: " + e.getMessage());
            return;
        }

        // One Scanner shared by all views
        Scanner scanner = new Scanner(System.in);
        ComputersView computersView = new ComputersView(new ComputersController(), scanner);
        RatesView ratesView = new RatesView(new RatesController(), scanner);

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== CYBER ACCESS =====");
            System.out.println("1. Computers");
            System.out.println("2. Rates");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> computersView.show();
                case "2" -> ratesView.show();
                case "0" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}