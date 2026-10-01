package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.ServiceRate;
import com.joysistvi.CyberAccess.service.TransactionsServiceImpl;
import com.joysistvi.CyberAccess.model.Transactions;
import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;

// Connects the transactions menu to its service logic.
public class TransactionsController {
    private final TransactionsServiceImpl service;
    public TransactionsController(TransactionsServiceImpl service) { this.service = service; }
    public int create(int userId) throws SQLException { return service.create(userId); }
    public List<Transactions> history(Integer userId) throws SQLException { return service.history(userId); }
    public int addCharge(int id, String description, BigDecimal quantity, String unit, BigDecimal price) throws SQLException {
        return service.addCharge(id, description, quantity, unit, price);
    }
    public int addService(int id, int serviceId, BigDecimal quantity, String unit, BigDecimal price) throws SQLException {
        return service.addService(id, serviceId, quantity, unit, price);
    }
    public List<ServiceRate> activeRates() throws SQLException { return service.activeRates(); }
    public int addRate(int id, int rateId, BigDecimal quantity) throws SQLException { return service.addRate(id, rateId, quantity); }
    public void removeItem(int id, int itemId) throws SQLException { service.removeItem(id, itemId); }
    public void close(int id) throws SQLException { service.close(id); }
    public void cancel(int id) throws SQLException { service.cancel(id); }
}
