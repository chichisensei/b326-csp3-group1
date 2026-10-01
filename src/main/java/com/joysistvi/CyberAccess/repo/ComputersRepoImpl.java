package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Computers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComputersRepoImpl implements ComputersRepo {

    private final DatabaseConnection db = new DatabaseConnection();

    private Computers map(ResultSet rs) throws SQLException {
        return new Computers(
                rs.getInt("id"),
                rs.getString("computer_name"),
                rs.getString("status"));
    }

    @Override
    public List<Computers> findAll() throws SQLException {
        String sql = "SELECT id, computer_name, status FROM computers ORDER BY id";
        List<Computers> list = new ArrayList<>();
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
    public Optional<Computers> findById(int id) throws SQLException {
        String sql = "SELECT id, computer_name, status FROM computers WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Computers> findByStatus(String status) throws SQLException {
        String sql = "SELECT id, computer_name, status FROM computers WHERE status = ? ORDER BY id";
        List<Computers> list = new ArrayList<>();
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean existsByName(String computerName) throws SQLException {
        String sql = "SELECT 1 FROM computers WHERE computer_name = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computerName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void save(Computers computer) throws SQLException {
        String sql = "INSERT INTO computers (computer_name, status) VALUES (?, ?)";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, computer.getComputerName());
            ps.setString(2, computer.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    computer.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public boolean update(Computers computer) throws SQLException {
        String sql = "UPDATE computers SET computer_name = ?, status = ? WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computer.getComputerName());
            ps.setString(2, computer.getStatus());
            ps.setInt(3, computer.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE computers SET status = ? WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM computers WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
