package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.Rates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RatesRepoImpl implements RatesRepo {

    private static final String SELECT =
            "SELECT id, service_id, rate_name, rate_type, price, unit, status FROM rates";

    private final DatabaseConnection db = new DatabaseConnection();

    private Rates map(ResultSet rs) throws SQLException {
        return new Rates(
                rs.getInt("id"),
                rs.getInt("service_id"),
                rs.getString("rate_name"),
                rs.getString("rate_type"),
                rs.getBigDecimal("price"),
                rs.getString("unit"),
                rs.getString("status"));
    }

    private List<Rates> queryList(String sql, String param) throws SQLException {
        List<Rates> list = new ArrayList<>();
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (param != null) {
                ps.setString(1, param);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Rates> findAll() throws SQLException {
        return queryList(SELECT + " ORDER BY id", null);
    }

    @Override
    public Optional<Rates> findById(int id) throws SQLException {
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(SELECT + " WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Rates> findByType(String rateType) throws SQLException {
        return queryList(SELECT + " WHERE rate_type = ? ORDER BY id", rateType);
    }

    @Override
    public List<Rates> findActive() throws SQLException {
        return queryList(SELECT + " WHERE status = ? ORDER BY id", Rates.STATUS_ACTIVE);
    }

    @Override
    public boolean existsByName(String rateName) throws SQLException {
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM rates WHERE rate_name = ?")) {
            ps.setString(1, rateName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void save(Rates rate) throws SQLException {
        String sql = "INSERT INTO rates (service_id, rate_name, rate_type, price, unit, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rate.getServiceId());
            ps.setString(2, rate.getRateName());
            ps.setString(3, rate.getRateType());
            ps.setBigDecimal(4, rate.getPrice());
            ps.setString(5, rate.getUnit());
            ps.setString(6, rate.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    rate.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public boolean update(Rates rate) throws SQLException {
        String sql = "UPDATE rates SET service_id = ?, rate_name = ?, rate_type = ?, "
                + "price = ?, unit = ?, status = ? WHERE id = ?";
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rate.getServiceId());
            ps.setString(2, rate.getRateName());
            ps.setString(3, rate.getRateType());
            ps.setBigDecimal(4, rate.getPrice());
            ps.setString(5, rate.getUnit());
            ps.setString(6, rate.getStatus());
            ps.setInt(7, rate.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int id, String status) throws SQLException {
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE rates SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        try (Connection conn = db.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM rates WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
