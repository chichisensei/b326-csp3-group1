package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Services;
import com.joysistvi.CyberAccess.repo.ServicesRepo;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

// Checks service details, then asks the repository to save them
public class ServicesServiceImpl {
    private final ServicesRepo repo;
    public ServicesServiceImpl(ServicesRepo repo) { this.repo = repo; }
    public List<Services> list() throws SQLException { return repo.findAll(); }
    public int create(String name, String description) throws SQLException {
        return repo.create(Validation.text(name, "Name", 100), Validation.text(description, "Description", 255));
    }
    public void update(int id, String name, String description) throws SQLException {
        repo.update(Validation.id(id), Validation.text(name, "Name", 100), Validation.text(description, "Description", 255));
    }
    public void setStatus(int id, String status) throws SQLException {
        repo.setStatus(Validation.id(id), Validation.status(status, Set.of("Active", "Inactive")));
    }
}
