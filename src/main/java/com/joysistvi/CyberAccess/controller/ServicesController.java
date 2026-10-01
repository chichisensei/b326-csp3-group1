package com.joysistvi.CyberAccess.controller;

import com.joysistvi.CyberAccess.service.ServicesServiceImpl;
import com.joysistvi.CyberAccess.model.Services;
import java.util.List;
import java.sql.SQLException;

// Connects the services menu to its service logic
public class ServicesController {

    private final ServicesServiceImpl service;
    public ServicesController(ServicesServiceImpl service) { this.service = service; }
    public List<Services> list() throws SQLException { return service.list(); }
    public int create(String name, String description) throws SQLException { return service.create(name, description); }
    public void update(int id, String name, String description) throws SQLException { service.update(id, name, description); }
    public void setStatus(int id, String status) throws SQLException { service.setStatus(id, status); }
}
