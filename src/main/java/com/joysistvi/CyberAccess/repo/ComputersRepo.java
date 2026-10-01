package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Computers;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ComputersRepo {

    List<Computers> findAll() throws SQLException;

    Optional<Computers> findById(int id) throws SQLException;

    List<Computers> findByStatus(String status) throws SQLException;

    boolean existsByName(String computerName) throws SQLException;

    // Inserts the record and sets the generated id on the passed object
    void save(Computers computer) throws SQLException;

    boolean update(Computers computer) throws SQLException;

    boolean updateStatus(int id, String status) throws SQLException;

    boolean delete(int id) throws SQLException;
}
