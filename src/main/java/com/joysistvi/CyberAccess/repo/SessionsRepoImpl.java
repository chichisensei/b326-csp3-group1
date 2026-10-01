package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Sessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionsRepoImpl implements SessionsRepo {

    private final DatabaseConnection db = new DatabaseConnection();

    private Sessions map(ResultSet rs) throws SQLException {
        return new Sessions(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("computer_id"),
                getNullableInteger(rs, "rate_id"),
                getLocalDateTime(rs, "start_time"),
                getLocalDateTime(rs, "end_time"),
                getNullableInteger(rs, "duration_minutes"),
                rs.getString("status"),
                getNullableInteger(rs, "duration_seconds")
        );
    }

    @Override
    public List<Sessions> findAll() throws SQLException {

        String sql = """
                SELECT id, user_id, computer_id, rate_id,
                       start_time, end_time, duration_minutes,
                       status, duration_seconds
                FROM sessions
                ORDER BY id
                """;

        List<Sessions> list = new ArrayList<>();

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        }

        return list;
    }

    @Override
    public Optional<Sessions> findById(int id) throws SQLException {

        String sql = """
                SELECT id, user_id, computer_id, rate_id,
                       start_time, end_time, duration_minutes,
                       status, duration_seconds
                FROM sessions
                WHERE id = ?
                """;

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void save(Sessions session) throws SQLException {

        String sql = """
                INSERT INTO sessions
                (user_id, computer_id, rate_id, start_time,
                 end_time, duration_minutes, status, duration_seconds)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = db.connect();
             PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, session.getUserId());
            ps.setInt(2, session.getComputerId());

            if (session.getRateId() != null) {
                ps.setInt(3, session.getRateId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (session.getStartTime() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(session.getStartTime())
                );
            } else {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(LocalDateTime.now())
                );
            }

            if (session.getEndTime() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(session.getEndTime())
                );
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            if (session.getDurationMinutes() != null) {
                ps.setInt(6, session.getDurationMinutes());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setString(7, session.getStatus());

            if (session.getDurationSeconds() != null) {
                ps.setInt(8, session.getDurationSeconds());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    session.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public boolean update(Sessions session) throws SQLException {

        String sql = """
                UPDATE sessions
                SET user_id = ?,
                    computer_id = ?,
                    rate_id = ?,
                    start_time = ?,
                    end_time = ?,
                    duration_minutes = ?,
                    status = ?,
                    duration_seconds = ?
                WHERE id = ?
                """;

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, session.getUserId());
            ps.setInt(2, session.getComputerId());

            if (session.getRateId() != null) {
                ps.setInt(3, session.getRateId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (session.getStartTime() != null) {
                ps.setTimestamp(
                        4,
                        Timestamp.valueOf(session.getStartTime())
                );
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            if (session.getEndTime() != null) {
                ps.setTimestamp(
                        5,
                        Timestamp.valueOf(session.getEndTime())
                );
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            if (session.getDurationMinutes() != null) {
                ps.setInt(6, session.getDurationMinutes());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            ps.setString(7, session.getStatus());

            if (session.getDurationSeconds() != null) {
                ps.setInt(8, session.getDurationSeconds());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setInt(9, session.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM sessions WHERE id = ?";

        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    private Integer getNullableInteger(
            ResultSet rs,
            String column) throws SQLException {

        int value = rs.getInt(column);

        if (rs.wasNull()) {
            return null;
        }

        return value;
    }

    private LocalDateTime getLocalDateTime(
            ResultSet rs,
            String column) throws SQLException {

        Timestamp timestamp = rs.getTimestamp(column);

        if (timestamp == null) {
            return null;
        }

        return timestamp.toLocalDateTime();
    }
}