package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Sessions;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SessionsRepo {

    List<Sessions> findAll() throws SQLException;

    Optional<Sessions> findById(int id) throws SQLException;

    void save(Sessions session) throws SQLException;

    boolean update(Sessions session) throws SQLException;

    boolean delete(int id) throws SQLException;
}