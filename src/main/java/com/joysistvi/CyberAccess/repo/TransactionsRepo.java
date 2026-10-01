package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.ServiceRate;
import com.joysistvi.CyberAccess.model.Transactions;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

// Lists the database actions a transaction repository must provide.
public interface TransactionsRepo {
    int create(int userId) throws SQLException;
    List<Transactions> history(Integer userId) throws SQLException;
    int addItem(int transactionId, Integer serviceId, String description,
                BigDecimal quantity, String unit, BigDecimal price) throws SQLException;
    List<ServiceRate> activeRates() throws SQLException;
    int addRate(int transactionId, int rateId, BigDecimal quantity) throws SQLException;
    void removeItem(int transactionId, int itemId) throws SQLException;
    void finish(int transactionId, String status) throws SQLException;
}
