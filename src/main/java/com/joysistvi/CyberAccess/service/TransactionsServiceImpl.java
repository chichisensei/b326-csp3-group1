package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.ServiceRate;
import com.joysistvi.CyberAccess.model.Transactions;
import com.joysistvi.CyberAccess.repo.TransactionsRepo;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

// Checks transaction input, then calls the database operations
public class TransactionsServiceImpl {
    private final TransactionsRepo repo;
    public TransactionsServiceImpl(TransactionsRepo repo) { this.repo = repo; }
    public int create(int userId) throws SQLException { return repo.create(Validation.id(userId)); }
    public List<Transactions> history(Integer userId) throws SQLException {
        if (userId != null) Validation.id(userId);
        return repo.history(userId);
    }
    public int addCharge(int transactionId, String description, BigDecimal quantity, String unit, BigDecimal price) throws SQLException {
        return repo.addItem(Validation.id(transactionId), null, Validation.text(description, "Description", 255),
                Validation.decimal(quantity, "Quantity", true), Validation.text(unit, "Unit", 20), Validation.decimal(price, "Price", false));
    }
    public int addService(int transactionId, int serviceId, BigDecimal quantity, String unit, BigDecimal price) throws SQLException {
        return repo.addItem(Validation.id(transactionId), Validation.id(serviceId), null,
                Validation.decimal(quantity, "Quantity", true), Validation.text(unit, "Unit", 20), Validation.decimal(price, "Price", false));
    }
    public List<ServiceRate> activeRates() throws SQLException { return repo.activeRates(); }
    public int addRate(int transactionId, int rateId, BigDecimal quantity) throws SQLException {
        return repo.addRate(Validation.id(transactionId), Validation.id(rateId), Validation.decimal(quantity, "Quantity", true));
    }
    public void removeItem(int transactionId, int itemId) throws SQLException { repo.removeItem(Validation.id(transactionId), Validation.id(itemId)); }
    public void close(int id) throws SQLException { repo.finish(Validation.id(id), "Closed"); }
    public void cancel(int id) throws SQLException { repo.finish(Validation.id(id), "Cancelled"); }
}
