package com.joysistvi.CyberAccess;

import com.joysistvi.CyberAccess.cliview.ComputersView;
import com.joysistvi.CyberAccess.cliview.RatesView;
import com.joysistvi.CyberAccess.cliview.SessionsView;
import com.joysistvi.CyberAccess.cliview.UsersView;
import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.controller.ComputersController;
import com.joysistvi.CyberAccess.controller.RatesController;
import com.joysistvi.CyberAccess.controller.SessionsController;
import com.joysistvi.CyberAccess.controller.UsersController;
import com.joysistvi.CyberAccess.model.Users;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class app {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DatabaseConnection db = new DatabaseConnection();

        try (Connection conn = db.connect()) {
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println("Cannot connect to the database: " + e.getMessage());
            return;
        }

        ComputersController computersController = new ComputersController();
        RatesController ratesController = new RatesController();
        SessionsController sessionsController = new SessionsController();
        UsersController usersController = new UsersController();

        ComputersView computersView = new ComputersView(computersController, scanner);
        RatesView ratesView = new RatesView(ratesController, scanner);
        SessionsView sessionsView = new SessionsView(scanner);
        UsersView usersView = new UsersView(usersController, sessionsView, scanner);

        boolean running = true;

        while (running) {

            printWelcomeMenu();

            int choice = readInt(scanner);

            switch (choice) {

                case 1 -> {
                    Users loggedInUser = handleLogin(scanner, usersController);

                    if (loggedInUser != null) {

                        if (loggedInUser.getRole().equalsIgnoreCase(Users.ROLE_ADMIN)) {

                            runAdminDashboard(
                                    scanner,
                                    computersView,
                                    ratesView,
                                    sessionsView,
                                    usersView,
                                    loggedInUser
                            );

                        } else if (loggedInUser.getRole().equalsIgnoreCase(Users.ROLE_USER)) {

                            usersView.showUserMenu(loggedInUser);

                        } else {

                            System.out.println();
                            System.out.println("Invalid account role.");
                            pause(scanner);
                        }
                    }
                }

                case 2 -> handleRegister(scanner, usersController);

                case 0 -> {
                    System.out.println();
                    System.out.println("Exiting Cyber Access. Goodbye!");
                    running = false;
                }

                default -> {
                    System.out.println("Invalid choice. Try again.");
                    pause(scanner);
                }
            }
        }

        scanner.close();
    }

    private static void printWelcomeMenu() {

        clearScreen();

        System.out.println();
        System.out.println("================================");
        System.out.println("          CYBER ACCESS");
        System.out.println("================================");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.println("================================");
        System.out.print("Choice: ");
    }

    private static Users handleLogin(
            Scanner scanner,
            UsersController usersController) {

        clearScreen();

        System.out.println();
        System.out.println("================================");
        System.out.println("             LOGIN");
        System.out.println("================================");

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        Users user = usersController.handleLogin(username, password);

        if (user != null) {

            System.out.println();
            System.out.println("Login successful!");
            System.out.println("Welcome, " + user.getFullName());
            System.out.println("Role: " + user.getRole());

        } else {

            System.out.println();
            System.out.println("Invalid username or password.");
        }

        pause(scanner);

        return user;
    }

    private static void handleRegister(
            Scanner scanner,
            UsersController usersController) {

        clearScreen();

        System.out.println();
        System.out.println("================================");
        System.out.println("           REGISTER");
        System.out.println("================================");

        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {

            System.out.println();
            System.out.println("Passwords do not match.");
            pause(scanner);

            return;
        }

        boolean success = usersController.handleRegister(fullName, username, password);

        System.out.println();

        if (success) {
            System.out.println("Registered successfully!");
            System.out.println("You can now log in.");
        } else {
            System.out.println("Failed to register.");
        }

        pause(scanner);
    }

    private static void runAdminDashboard(
            Scanner scanner,
            ComputersView computersView,
            RatesView ratesView,
            SessionsView sessionsView,
            UsersView usersView,
            Users loggedInUser) {

        int choice;

        do {

            printAdminMenu(loggedInUser);

            choice = readInt(scanner);

            switch (choice) {

                case 1 -> usersView.show();

                case 2 -> computersView.show();

                case 3 -> ratesView.show();

                case 4 -> sessionsView.showMenu();

                case 0 -> {
                    System.out.println();
                    System.out.println("Logging out...");
                }

                default -> {
                    System.out.println("Invalid choice. Try again.");
                    pause(scanner);
                }
            }

        } while (choice != 0);
    }

    private static void printAdminMenu(Users loggedInUser) {

        clearScreen();

        System.out.println();
        System.out.println("================================");
        System.out.println("          ADMIN DASHBOARD");
        System.out.println("================================");
        System.out.println("Welcome, " + loggedInUser.getFullName());
        System.out.println();
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Computers");
        System.out.println("3. Manage Rates");
        System.out.println("4. Manage Sessions");
        System.out.println("0. Logout");
        System.out.println("================================");
        System.out.print("Choice: ");
    }

    private static int readInt(Scanner scanner) {

        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }

        int value = scanner.nextInt();
        scanner.nextLine();

        return value;
    }

    private static void pause(Scanner scanner) {

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void clearScreen() {

        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
