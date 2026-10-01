package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Rates;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RatesRepo {

    List<Rates> findAll() throws SQLException;

    Optional<Rates> findById(int id) throws SQLException;

    List<Rates> findByType(String rateType) throws SQLException;

    List<Rates> findActive() throws SQLException;

    boolean existsByName(String rateName) throws SQLException;

    // Inserts the record and sets the generated id on the passed object
    void save(Rates rate) throws SQLException;

    boolean update(Rates rate) throws SQLException;

    boolean updateStatus(int id, String status) throws SQLException;

    boolean delete(int id) throws SQLException;
}
