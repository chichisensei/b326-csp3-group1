package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Runs SQL to read and save services.
public class ServicesRepoImpl implements ServicesRepo{
    private final DatabaseConnection db;
    public ServicesRepoImpl(DatabaseConnection db) { this.db = db; }
    // Read all saved services, including inactive ones.
    public List<Services> findAll() throws SQLException {
        List<Services> result = new ArrayList<>();
        try (Connection c = db.connect(); PreparedStatement s = c.prepareStatement(
                "SELECT id, name, description, status FROM services ORDER BY id"); ResultSet r = s.executeQuery()) {
            while (r.next()) result.add(new Services(r.getInt("id"), r.getString("name"),
                    r.getString("description"), r.getString("status")));
        }
        return result;
    }
    // Save a new service and return its generated ID.
    public int create(String name, String description) throws SQLException {
        try (Connection c = db.connect(); PreparedStatement s = c.prepareStatement(
                "INSERT INTO services(name, description, status) VALUES (?, ?, 'Active')", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name); s.setString(2, description); s.executeUpdate();
            try (ResultSet r = s.getGeneratedKeys()) {
                if (!r.next()) throw new SQLException("No service ID returned.");
                return r.getInt(1);
            }
        }
    }
    // Edit the name and description of the selected service.
    public void update(int id, String name, String description) throws SQLException {
        try (Connection c = db.connect(); PreparedStatement s = c.prepareStatement(
                "UPDATE services SET name=?, description=? WHERE id=?")) {
            s.setString(1, name); s.setString(2, description); s.setInt(3, id);
            requireFound(s.executeUpdate());
        }
    }
    // Activate or deactivate a service without deleting its history.
    public void setStatus(int id, String status) throws SQLException {
        try (Connection c = db.connect(); PreparedStatement s = c.prepareStatement(
                "UPDATE services SET status=? WHERE id=?")) {
            s.setString(1, status); s.setInt(2, id); requireFound(s.executeUpdate());
        }
    }
    private void requireFound(int count) {
        if (count == 0) throw new IllegalArgumentException("Service not found (or no change with this JDBC configuration).");
    }
}
