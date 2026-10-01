package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Computers;
import com.joysistvi.CyberAccess.repo.ComputersRepo;
import com.joysistvi.CyberAccess.repo.ComputersRepoImpl;

import java.sql.SQLException;
import java.util.List;

public class ComputersServiceImpl implements ComputersService {

    private static final int NAME_MAX = 50;

    private final ComputersRepo repo;

    public ComputersServiceImpl() {
        this(new ComputersRepoImpl());
    }

    public ComputersServiceImpl(ComputersRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Computers> getAll() throws SQLException {
        return repo.findAll();
    }

    @Override
    public Computers getById(int id) throws SQLException {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No computer found with ID " + id + "."));
    }

    @Override
    public List<Computers> getByStatus(String status) throws SQLException {
        return repo.findByStatus(validStatus(status));
    }

    @Override
    public Computers add(String computerName, String status) throws SQLException {
        String name = validName(computerName);
        String st = validStatus(status);

        if (repo.existsByName(name)) {
            throw new IllegalArgumentException("A computer named \"" + name + "\" already exists.");
        }

        Computers computer = new Computers(name, st);
        repo.save(computer);
        return computer;
    }

    @Override
    public void update(int id, String computerName, String status) throws SQLException {
        Computers current = getById(id);
        String name = validName(computerName);
        String st = validStatus(status);

        if (!name.equalsIgnoreCase(current.getComputerName()) && repo.existsByName(name)) {
            throw new IllegalArgumentException("A computer named \"" + name + "\" already exists.");
        }

        current.setComputerName(name);
        current.setStatus(st);
        repo.update(current);
    }

    @Override
    public void changeStatus(int id, String status) throws SQLException {
        String st = validStatus(status);
        getById(id); // makes sure it exists
        repo.updateStatus(id, st);
    }

    @Override
    public void delete(int id) throws SQLException {
        Computers current = getById(id);
        if (Computers.STATUS_IN_USE.equals(current.getStatus())) {
            throw new IllegalArgumentException(
                    current.getComputerName() + " is currently in use and cannot be deleted.");
        }
        repo.delete(id);
    }

    private String validName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Computer name cannot be empty.");
        }
        String trimmed = name.trim();
        if (trimmed.length() > NAME_MAX) {
            throw new IllegalArgumentException("Computer name must be at most " + NAME_MAX + " characters.");
        }
        return trimmed;
    }

    private String validStatus(String status) {
        if (status != null) {
            for (String allowed : new String[]{
                    Computers.STATUS_AVAILABLE, Computers.STATUS_IN_USE, Computers.STATUS_MAINTENANCE}) {
                if (allowed.equalsIgnoreCase(status.trim())) {
                    return allowed; // normalise the casing
                }
            }
        }
        throw new IllegalArgumentException("Status must be Available, In Use, or Maintenance.");
    }
}
