package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Bill;
import com.joysistvi.CyberAccess.repo.BillRepo;
import java.sql.SQLException;

// Checks the transaction ID before retrieving its bill
public class BillServiceImpl {
    private final BillRepo repo;
    public BillServiceImpl(BillRepo repo) { this.repo = repo; }
    public Bill getBill(int transactionId) throws SQLException { return repo.findByTransactionId(Validation.id(transactionId)); }
}
