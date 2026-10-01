package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Computers;

import java.sql.SQLException;
import java.util.List;

public interface ComputersService {

    List<Computers> getAll() throws SQLException;

    Computers getById(int id) throws SQLException;

    List<Computers> getByStatus(String status) throws SQLException;

    Computers add(String computerName, String status) throws SQLException;

    void update(int id, String computerName, String status) throws SQLException;

    void changeStatus(int id, String status) throws SQLException;

    void delete(int id) throws SQLException;
}
