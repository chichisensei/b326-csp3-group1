package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Bill;
import java.sql.SQLException;

// Defines how to retrieve a bill from the database
public interface BillRepo {
    Bill findByTransactionId(int transactionId) throws SQLException;
}
