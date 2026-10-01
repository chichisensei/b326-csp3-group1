package com.joysistvi.CyberAccess.repo;

import com.joysistvi.CyberAccess.model.Services;
import java.sql.SQLException;
import java.util.List;

// Lists the database actions a service repository must provide
public interface ServicesRepo {
    List<Services> findAll() throws SQLException;
    int create(String name, String description) throws SQLException;
    void update(int id, String name, String description) throws SQLException;
    void setStatus(int id, String status) throws SQLException;
}
