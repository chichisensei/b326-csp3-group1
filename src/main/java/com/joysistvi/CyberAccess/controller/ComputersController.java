package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.model.Computers;
import com.joysistvi.CyberAccess.service.ComputersService;
import com.joysistvi.CyberAccess.service.ComputersServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class ComputersController {

    private final ComputersService service;

    public ComputersController() {
        this(new ComputersServiceImpl());
    }

    public ComputersController(ComputersService service) {
        this.service = service;
    }

    public List<Computers> getAll() throws SQLException {
        return service.getAll();
    }

    public Computers getById(int id) throws SQLException {
        return service.getById(id);
    }

    public List<Computers> getByStatus(String status) throws SQLException {
        return service.getByStatus(status);
    }

    public Computers add(String computerName, String status) throws SQLException {
        return service.add(computerName, status);
    }

    public void update(int id, String computerName, String status) throws SQLException {
        service.update(id, computerName, status);
    }

    public void changeStatus(int id, String status) throws SQLException {
        service.changeStatus(id, status);
    }

    public void delete(int id) throws SQLException {
        service.delete(id);
    }
}
