package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.Users;
import com.joysistvi.CyberAccess.service.UsersService;
import com.joysistvi.CyberAccess.service.UsersServiceImpl;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

public class UsersController {

    private final UsersService service;

    public UsersController() {
        this(new UsersServiceImpl());
    }

    public UsersController(
            UsersService service) {

        this.service = service;
    }

    public List<Users> getAll()
            throws SQLException {

        return service.getAll();
    }

    public Users getById(int id)
            throws SQLException {

        return service.getById(id);
    }

    public Users getByUsername(
            String username)
            throws SQLException {

        return service.getByUsername(username);
    }

    public Users handleLogin(
            String username,
            String password) {

        try {

            if (username == null ||
                    username.isBlank()) {

                return null;
            }

            if (password == null ||
                    password.isBlank()) {

                return null;
            }

            Users user =
                    service.getByUsername(
                            username.trim()
                    );

            if (user == null) {
                return null;
            }

            // Check account status
            if (!Users.STATUS_ACTIVE.equalsIgnoreCase(
                    user.getStatus()
            )) {

                return null;
            }

            String storedPassword =
                    user.getPassword();

            boolean validPassword = false;

            if (storedPassword != null) {

                if (storedPassword.startsWith("$2a$") ||
                        storedPassword.startsWith("$2b$") ||
                        storedPassword.startsWith("$2y$")) {

                    try {

                        validPassword =
                                BCrypt.checkpw(
                                        password,
                                        storedPassword
                                );

                    } catch (IllegalArgumentException e) {

                        validPassword = false;
                    }

                } else {
                    validPassword =
                            password.equals(
                                    storedPassword
                            );
                }
            }

            if (!validPassword) {
                return null;
            }

            if (!isBCryptPassword(
                    storedPassword
            )) {

                user.setPassword(
                        BCrypt.hashpw(
                                password,
                                BCrypt.gensalt()
                        )
                );

                service.update(user);
            }

            return user;

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            return null;
        }
    }

    public boolean handleRegister(
            String fullName,
            String username,
            String password) {

        try {

            if (fullName == null ||
                    fullName.isBlank()) {

                System.out.println(
                        "Full name cannot be empty."
                );

                return false;
            }

            if (username == null ||
                    username.isBlank()) {

                System.out.println(
                        "Username cannot be empty."
                );

                return false;
            }

            if (password == null ||
                    password.isBlank()) {

                System.out.println(
                        "Password cannot be empty."
                );

                return false;
            }

            Users existingUser =
                    service.getByUsername(
                            username.trim()
                    );

            if (existingUser != null) {

                System.out.println(
                        "Username already exists."
                );

                return false;
            }

            String hashedPassword =
                    BCrypt.hashpw(
                            password,
                            BCrypt.gensalt()
                    );

            Users user =
                    new Users(
                            fullName.trim(),
                            username.trim(),
                            hashedPassword,
                            Users.ROLE_USER,
                            Users.STATUS_ACTIVE
                    );

            service.add(user);

            return true;

        } catch (IllegalArgumentException e) {

            System.out.println(
                    e.getMessage()
            );

            return false;

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " +
                            e.getMessage()
            );

            return false;
        }
    }

    public void add(Users user)
            throws SQLException {

        service.add(user);
    }

    public void update(Users user)
            throws SQLException {

        service.update(user);
    }

    public boolean remove(int id)
            throws SQLException {

        return service.remove(id);
    }

    private boolean isBCryptPassword(
            String password) {

        if (password == null) {
            return false;
        }

        return password.startsWith("$2a$") ||
                password.startsWith("$2b$") ||
                password.startsWith("$2y$");
    }
}
