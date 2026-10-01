package com.joysistvi.CyberAccess.cliview;

import com.joysistvi.CyberAccess.controller.UsersController;
import com.joysistvi.CyberAccess.model.Sessions;
import com.joysistvi.CyberAccess.model.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UsersView {

    private final UsersController usersController;
    private final SessionsView sessionsView;
    private final Scanner scanner;

    public UsersView(
            UsersController usersController,
            SessionsView sessionsView,
            Scanner scanner) {

        this.usersController = usersController;
        this.sessionsView = sessionsView;
        this.scanner = scanner;
    }


    public void show() {

        boolean running = true;

        while (running) {

            clearScreen();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          USER MANAGEMENT"
            );
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "1. Add User"
            );
            System.out.println(
                    "2. View Users"
            );
            System.out.println(
                    "3. Edit User"
            );
            System.out.println(
                    "4. Remove User"
            );
            System.out.println(
                    "0. Return"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Select: "
            );

            String choice =
                    scanner.nextLine().trim();

            switch (choice) {

                case "1" ->
                        addUser();

                case "2" ->
                        viewUsers();

                case "3" ->
                        editUser();

                case "4" ->
                        removeUser();

                case "0" ->
                        running = false;

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }

    // USER DASHBOARD

    public void showUserMenu(
            Users loggedInUser) {

        boolean running = true;

        while (running) {

            clearScreen();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "            USER MENU"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "Welcome, " +
                            loggedInUser.getFullName()
            );

            System.out.println();
            System.out.println(
                    "1. View Account Information"
            );
            System.out.println(
                    "2. View Available Computers"
            );
            System.out.println(
                    "3. Start Computer Session"
            );

            System.out.println(
                    "0. Logout"
            );

            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Select: "
            );

            String choice =
                    scanner.nextLine().trim();

            switch (choice) {

                case "1" ->
                        viewAccountInformation(
                                loggedInUser
                        );

                case "2" ->
                        viewAvailableComputers();

                case "3" ->
                        startComputerSession(
                                loggedInUser
                        );

                case "0" -> {

                    System.out.println();
                    System.out.println(
                            "Logging out..."
                    );

                    running = false;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }

    // ADD USER

    private void addUser() {

        try {

            clearScreen();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "            ADD USER"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Full Name: "
            );

            String fullName =
                    scanner.nextLine().trim();

            if (fullName.isEmpty()) {

                System.out.println(
                        "Full name cannot be empty."
                );

                pause();

                return;
            }

            System.out.print(
                    "Username: "
            );

            String username =
                    scanner.nextLine().trim();

            if (username.isEmpty()) {

                System.out.println(
                        "Username cannot be empty."
                );

                pause();

                return;
            }

            System.out.print(
                    "Password: "
            );

            String password =
                    scanner.nextLine();

            if (password.isBlank()) {

                System.out.println(
                        "Password cannot be empty."
                );

                pause();

                return;
            }

            System.out.print(
                    "Role (ADMIN/USER): "
            );

            String role =
                    scanner.nextLine().trim();

            if (role.equalsIgnoreCase("ADMIN")) {

                role =
                        Users.ROLE_ADMIN;

            } else if (
                    role.equalsIgnoreCase("USER")) {

                role =
                        Users.ROLE_USER;

            } else {

                System.out.println(
                        "Invalid role. Use ADMIN or USER."
                );

                pause();

                return;
            }

            Users user =
                    new Users(
                            fullName,
                            username,
                            password,
                            role,
                            Users.STATUS_ACTIVE
                    );

            usersController.add(user);

            System.out.println();
            System.out.println(
                    "User added successfully."
            );

            System.out.println(
                    "User ID: " +
                            user.getId()
            );

        } catch (IllegalArgumentException e) {

            System.out.println();
            System.out.println(
                    e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }

    // VIEW USERS

    private void viewUsers() {

        try {

            clearScreen();

            List<Users> users =
                    usersController.getAll();

            System.out.println();
            System.out.println(
                    "================================================================"
            );
            System.out.println(
                    "                         ALL USERS"
            );
            System.out.println(
                    "================================================================"
            );

            if (users.isEmpty()) {

                System.out.println(
                        "No users found."
                );

                System.out.println(
                        "================================================================"
                );

                pause();

                return;
            }

            System.out.printf(
                    "%-5s %-25s %-18s %-10s %-10s%n",
                    "ID",
                    "Full Name",
                    "Username",
                    "Role",
                    "Status"
            );

            System.out.println(
                    "----------------------------------------------------------------"
            );

            for (Users user : users) {

                System.out.printf(
                        "%-5d %-25s %-18s %-10s %-10s%n",
                        user.getId(),
                        user.getFullName(),
                        user.getUsername(),
                        user.getRole(),
                        user.getStatus()
                );
            }

            System.out.println(
                    "================================================================"
            );

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }

    // EDIT USER

    private void editUser() {

        try {

            clearScreen();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "            EDIT USER"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Enter User ID: "
            );

            int userId =
                    Integer.parseInt(
                            scanner.nextLine().trim()
                    );

            Users user =
                    usersController.getById(
                            userId
                    );

            if (user == null) {

                System.out.println(
                        "User not found."
                );

                pause();

                return;
            }

            System.out.println();
            System.out.println(
                    "Current Full Name: " +
                            user.getFullName()
            );

            System.out.println(
                    "Current Username: " +
                            user.getUsername()
            );

            System.out.println(
                    "Current Role: " +
                            user.getRole()
            );

            System.out.println(
                    "Current Status: " +
                            user.getStatus()
            );

            System.out.println();

            System.out.print(
                    "New Full Name [Enter to keep]: "
            );

            String fullName =
                    scanner.nextLine().trim();

            if (!fullName.isEmpty()) {
                user.setFullName(fullName);
            }

            System.out.print(
                    "New Username [Enter to keep]: "
            );

            String username =
                    scanner.nextLine().trim();

            if (!username.isEmpty()) {
                user.setUsername(username);
            }

            System.out.print(
                    "New Password [Enter to keep]: "
            );

            String password =
                    scanner.nextLine();

            if (!password.isBlank()) {
                user.setPassword(password);
            }

            System.out.print(
                    "New Role [ADMIN/USER, Enter to keep]: "
            );

            String role =
                    scanner.nextLine().trim();

            if (!role.isEmpty()) {

                if (role.equalsIgnoreCase("ADMIN")) {

                    user.setRole(
                            Users.ROLE_ADMIN
                    );

                } else if (
                        role.equalsIgnoreCase("USER")) {

                    user.setRole(
                            Users.ROLE_USER
                    );

                } else {

                    System.out.println(
                            "Invalid role."
                    );

                    pause();

                    return;
                }
            }

            System.out.print(
                    "New Status [ACTIVE/INACTIVE, Enter to keep]: "
            );

            String status =
                    scanner.nextLine().trim();

            if (!status.isEmpty()) {

                if (status.equalsIgnoreCase("ACTIVE")) {

                    user.setStatus(
                            Users.STATUS_ACTIVE
                    );

                } else if (
                        status.equalsIgnoreCase("INACTIVE")) {

                    user.setStatus(
                            Users.STATUS_INACTIVE
                    );

                } else {

                    System.out.println(
                            "Invalid status."
                    );

                    pause();

                    return;
                }
            }

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          NEW INFORMATION"
            );
            System.out.println(
                    "================================"
            );

            System.out.println(
                    "ID: " +
                            user.getId()
            );

            System.out.println(
                    "Full Name: " +
                            user.getFullName()
            );

            System.out.println(
                    "Username: " +
                            user.getUsername()
            );

            System.out.println(
                    "Role: " +
                            user.getRole()
            );

            System.out.println(
                    "Status: " +
                            user.getStatus()
            );

            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Save changes? (Y/N): "
            );

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("Y")) {

                System.out.println(
                        "Edit cancelled."
                );

                pause();

                return;
            }

            usersController.update(user);

            System.out.println();
            System.out.println(
                    "User edited successfully."
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid User ID."
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    e.getMessage()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }

    // REMOVE USER

    private void removeUser() {

        try {

            clearScreen();

            System.out.println();
            System.out.println(
                    "================================"
            );
            System.out.println(
                    "          REMOVE USER"
            );
            System.out.println(
                    "================================"
            );

            System.out.print(
                    "Enter User ID: "
            );

            int userId =
                    Integer.parseInt(
                            scanner.nextLine().trim()
                    );

            Users user =
                    usersController.getById(
                            userId
                    );

            if (user == null) {

                System.out.println(
                        "User not found."
                );

                pause();

                return;
            }

            System.out.println();
            System.out.println(
                    "User: " +
                            user.getFullName()
            );

            System.out.println(
                    "Username: " +
                            user.getUsername()
            );

            System.out.println(
                    "Role: " +
                            user.getRole()
            );

            System.out.println(
                    "Status: " +
                            user.getStatus()
            );

            System.out.println();

            System.out.print(
                    "Are you sure you want to " +
                            "remove this user? (Y/N): "
            );

            String confirmation =
                    scanner.nextLine().trim();

            if (!confirmation.equalsIgnoreCase("Y")) {

                System.out.println(
                        "Remove cancelled."
                );

                pause();

                return;
            }

            boolean removed =
                    usersController.remove(
                            userId
                    );

            System.out.println();

            if (removed) {

                System.out.println(
                        "User removed successfully."
                );

            } else {

                System.out.println(
                        "User could not be removed."
                );
            }

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid User ID."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );
        }

        pause();
    }

    // VIEW ACCOUNT INFORMATION

    private void viewAccountInformation(
            Users loggedInUser) {

        clearScreen();

        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "      ACCOUNT INFORMATION"
        );
        System.out.println(
                "================================"
        );

        System.out.println(
                "User ID: " +
                        loggedInUser.getId()
        );

        System.out.println(
                "Full Name: " +
                        loggedInUser.getFullName()
        );

        System.out.println(
                "Username: " +
                        loggedInUser.getUsername()
        );

        System.out.println(
                "Role: " +
                        loggedInUser.getRole()
        );

        System.out.println(
                "Status: " +
                        loggedInUser.getStatus()
        );

        if (loggedInUser.getCreatedAt() != null) {

            System.out.println(
                    "Created At: " +
                            loggedInUser.getCreatedAt()
            );
        }

        System.out.println(
                "================================"
        );

        pause();
    }

    // VIEW AVAILABLE COMPUTERS

    private void viewAvailableComputers() {

        clearScreen();

        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "       AVAILABLE COMPUTERS"
        );
        System.out.println(
                "================================"
        );

        /*
         di pa tapos
         */


        System.out.println(
                "================================"
        );

        pause();
    }

    // START COMPUTER SESSION

    private void startComputerSession(
            Users loggedInUser) {

        try {


            // CHECK IF USER ALREADY HAS AN ONGOING SESSION

            List<Sessions> sessions =
                    sessionsView.getAllSessions();

            for (Sessions session : sessions) {

                if (session.getUserId() ==
                        loggedInUser.getId()
                        &&
                        "Ongoing".equalsIgnoreCase(
                                session.getStatus()
                        )) {

                    clearScreen();

                    System.out.println();
                    System.out.println(
                            "================================"
                    );
                    System.out.println(
                            "       SESSION ALREADY ACTIVE"
                    );
                    System.out.println(
                            "================================"
                    );

                    System.out.println(
                            "You already have an ongoing session."
                    );

                    System.out.println(
                            "Session ID: " +
                                    session.getId()
                    );

                    System.out.println(
                            "Computer ID: " +
                                    session.getComputerId()
                    );

                    System.out.println();
                    System.out.println(
                            "You must finish your current "
                                    + "session before starting"
                    );

                    System.out.println(
                            "another computer session."
                    );

                    System.out.println(
                            "================================"
                    );

                    pause();

                    return;
                }
            }

            // START NEW SESSION

            sessionsView.startUserSession(
                    loggedInUser
            );

        } catch (SQLException e) {

            System.out.println();
            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            pause();
        }
    }

    // LOGOUT COMPUTER SESSION

    private void logoutComputerSession(
            Users loggedInUser) {

        clearScreen();

        System.out.println();
        System.out.println(
                "================================"
        );
        System.out.println(
                "     LOGOUT COMPUTER SESSION"
        );
        System.out.println(
                "================================"
        );

        System.out.println(
                "Your active session is controlled "
                        + "by the session countdown."
        );

        System.out.println();
        System.out.println(
                "During the countdown:"
        );

        System.out.println(
                "Type L + ENTER"
        );

        System.out.println(
                "Then choose Y or N."
        );

        System.out.println(
                "================================"
        );

        pause();
    }

    // PAUSE

    private void pause() {

        System.out.print(
                "\nPress Enter to continue..."
        );

        scanner.nextLine();
    }

    // CLEAR SCREEN

    private void clearScreen() {

        System.out.print(
                "\033[H\033[2J"
        );

        System.out.flush();
    }
}
