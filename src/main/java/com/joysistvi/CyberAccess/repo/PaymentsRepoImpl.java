package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Payments;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PaymentsRepoImpl {

  List<Payments> findAll() throws SQLException;

    Optional<Payments> findById(int id) throws SQLException;

    List<Payments> findByTransactionId(int transactionId) throws SQLException;

    List<Payments> findByUserId(int userId) throws SQLException;

    List<Payments> findByStatus(String status) throws SQLException;

    boolean existsById(int id) throws SQLException;

    // Inserts the record and sets the generated id on the passed object
    void save(Payments payment) throws SQLException;

    boolean update(Payments payment) throws SQLException;

    boolean updateStatus(int id, String status) throws SQLException;

    boolean delete(int id) throws SQLException;
}
}
