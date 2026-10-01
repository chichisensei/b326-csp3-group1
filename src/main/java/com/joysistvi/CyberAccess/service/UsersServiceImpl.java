package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Users;
import com.joysistvi.CyberAccess.repo.UsersRepo;
import com.joysistvi.CyberAccess.repo.UsersRepoImpl;

import java.sql.SQLException;
import java.util.List;

public class UsersServiceImpl
        implements UsersService {

    private final UsersRepo repo;

    public UsersServiceImpl() {
        this(new UsersRepoImpl());
    }

    public UsersServiceImpl(
            UsersRepo repo) {

        this.repo = repo;
    }

    @Override
    public List<Users> getAll()
            throws SQLException {

        return repo.findAll();
    }

    @Override
    public Users getById(int id)
            throws SQLException {

        return repo.findById(id)
                .orElse(null);
    }

    @Override
    public Users getByUsername(
            String username)
            throws SQLException {

        return repo.findByUsername(
                username
        ).orElse(null);
    }

    @Override
    public void add(Users user)
            throws SQLException {

        if (user == null) {

            throw new IllegalArgumentException(
                    "User cannot be null."
            );
        }

        if (user.getFullName() == null ||
                user.getFullName().isBlank()) {

            throw new IllegalArgumentException(
                    "Full name cannot be empty."
            );
        }

        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username cannot be empty."
            );
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        if (user.getRole() == null ||
                user.getRole().isBlank()) {

            throw new IllegalArgumentException(
                    "Role cannot be empty."
            );
        }

        if (!Users.ROLE_ADMIN.equalsIgnoreCase(
                user.getRole()
        ) &&
                !Users.ROLE_USER.equalsIgnoreCase(
                        user.getRole()
                )) {

            throw new IllegalArgumentException(
                    "Role must be ADMIN or USER."
            );
        }

        if (user.getStatus() == null ||
                user.getStatus().isBlank()) {

            user.setStatus(
                    Users.STATUS_ACTIVE
            );
        }

        if (!Users.STATUS_ACTIVE.equalsIgnoreCase(
                user.getStatus()
        ) &&
                !Users.STATUS_INACTIVE.equalsIgnoreCase(
                        user.getStatus()
                )) {

            throw new IllegalArgumentException(
                    "Status must be ACTIVE or INACTIVE."
            );
        }

        String username =
                user.getUsername().trim();

        if (repo.existsByUsername(username)) {

            throw new IllegalArgumentException(
                    "Username already exists."
            );
        }

        user.setFullName(
                user.getFullName().trim()
        );

        user.setUsername(username);

        user.setRole(
                user.getRole().toUpperCase()
        );

        user.setStatus(
                user.getStatus().toUpperCase()
        );

        repo.save(user);
    }

    @Override
    public void update(Users user)
            throws SQLException {

        if (user == null) {

            throw new IllegalArgumentException(
                    "User cannot be null."
            );
        }

        if (user.getId() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        if (user.getFullName() == null ||
                user.getFullName().isBlank()) {

            throw new IllegalArgumentException(
                    "Full name cannot be empty."
            );
        }

        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username cannot be empty."
            );
        }

        if (!Users.ROLE_ADMIN.equalsIgnoreCase(
                user.getRole()
        ) &&
                !Users.ROLE_USER.equalsIgnoreCase(
                        user.getRole()
                )) {

            throw new IllegalArgumentException(
                    "Role must be ADMIN or USER."
            );
        }

        if (!Users.STATUS_ACTIVE.equalsIgnoreCase(
                user.getStatus()
        ) &&
                !Users.STATUS_INACTIVE.equalsIgnoreCase(
                        user.getStatus()
                )) {

            throw new IllegalArgumentException(
                    "Status must be ACTIVE or INACTIVE."
            );
        }

        user.setFullName(
                user.getFullName().trim()
        );

        user.setUsername(
                user.getUsername().trim()
        );

        user.setRole(
                user.getRole().toUpperCase()
        );

        user.setStatus(
                user.getStatus().toUpperCase()
        );

        repo.update(user);
    }

    @Override
    public boolean remove(int id)
            throws SQLException {

        if (id <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        return repo.remove(id);
    }
}
