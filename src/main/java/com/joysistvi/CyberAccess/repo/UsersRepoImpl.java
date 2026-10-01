package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersRepoImpl implements UsersRepo {

    private final DatabaseConnection db =
            new DatabaseConnection();

    private Users map(ResultSet rs)
            throws SQLException {

        Timestamp createdAt =
                rs.getTimestamp("created_at");

        return new Users(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("status"),
                createdAt == null
                        ? null
                        : createdAt.toLocalDateTime()
        );
    }

    @Override
    public List<Users> findAll()
            throws SQLException {

        String sql = """
                SELECT id,
                       full_name,
                       username,
                       password,
                       role,
                       status,
                       created_at
                FROM users
                ORDER BY id
                """;

        List<Users> users =
                new ArrayList<>();

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql);
                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {
                users.add(map(rs));
            }
        }

        return users;
    }

    @Override
    public Optional<Users> findById(int id)
            throws SQLException {

        String sql = """
                SELECT id,
                       full_name,
                       username,
                       password,
                       role,
                       status,
                       created_at
                FROM users
                WHERE id = ?
                """;

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(
                            map(rs)
                    );
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Users> findByUsername(
            String username)
            throws SQLException {

        String sql = """
                SELECT id,
                       full_name,
                       username,
                       password,
                       role,
                       status,
                       created_at
                FROM users
                WHERE username = ?
                """;

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(
                            map(rs)
                    );
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(
            String username)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM users
                WHERE username = ?
                """;

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(1, username);

            try (ResultSet rs =
                         ps.executeQuery()) {

                return rs.next() &&
                        rs.getInt(1) > 0;
            }
        }
    }

    @Override
    public void save(Users user)
            throws SQLException {

        String sql = """
                INSERT INTO users
                (full_name, username, password, role, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(
                    1,
                    user.getFullName()
            );

            ps.setString(
                    2,
                    user.getUsername()
            );

            ps.setString(
                    3,
                    user.getPassword()
            );

            ps.setString(
                    4,
                    user.getRole()
            );

            ps.setString(
                    5,
                    user.getStatus()
            );

            ps.executeUpdate();

            try (ResultSet keys =
                         ps.getGeneratedKeys()) {

                if (keys.next()) {

                    user.setId(
                            keys.getInt(1)
                    );
                }
            }
        }
    }

    @Override
    public boolean update(Users user)
            throws SQLException {

        String sql = """
                UPDATE users
                SET full_name = ?,
                    username = ?,
                    password = ?,
                    role = ?,
                    status = ?
                WHERE id = ?
                """;

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setString(
                    1,
                    user.getFullName()
            );

            ps.setString(
                    2,
                    user.getUsername()
            );

            ps.setString(
                    3,
                    user.getPassword()
            );

            ps.setString(
                    4,
                    user.getRole()
            );

            ps.setString(
                    5,
                    user.getStatus()
            );

            ps.setInt(
                    6,
                    user.getId()
            );

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remove(int id)
            throws SQLException {

        String sql =
                "DELETE FROM users WHERE id = ?";

        try (
                Connection conn = db.connect();
                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }
}
