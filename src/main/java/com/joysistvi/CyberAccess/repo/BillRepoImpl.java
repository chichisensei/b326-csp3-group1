package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.config.DatabaseConnection;
import com.joysistvi.CyberAccess.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// Reads the transaction and its charges together
public class BillRepoImpl implements BillRepo{
    private final DatabaseConnection db;
    public BillRepoImpl(DatabaseConnection db) { this.db = db; }
    public Bill findByTransactionId(int transactionId) throws SQLException {
        // One query keeps the header and items in the same database snapshot.
        String sql = "SELECT t.id, t.user_id, t.created_at, t.status, i.id AS item_id, "
                + "i.service_id, i.description, i.quantity, i.unit, i.unit_price "
                + "FROM transactions t LEFT JOIN transaction_items i ON i.transaction_id=t.id "
                + "WHERE t.id=? ORDER BY i.id";
        try (Connection c = db.connect(); PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, transactionId);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) throw new IllegalArgumentException("Transaction not found.");
                int userId = r.getInt("user_id");
                var createdAt = r.getTimestamp("created_at").toLocalDateTime();
                String status = r.getString("status");
                List<TransactionItem> items = new ArrayList<>();
                do {
                    int itemId = r.getInt("item_id");
                    if (!r.wasNull()) {
                        int source = r.getInt("service_id");
                        Integer serviceId = r.wasNull() ? null : source;
                        items.add(new TransactionItem(itemId, transactionId, serviceId, r.getString("description"),
                                r.getBigDecimal("quantity"), r.getString("unit"), r.getBigDecimal("unit_price")));
                    }
                } while (r.next());
                BigDecimal total = items.stream().map(TransactionItem::subtotal).reduce(new BigDecimal("0.00"), BigDecimal::add);
                return new Bill(new Transactions(transactionId, userId, createdAt, status, total), items);
            }
        }
    }
}
