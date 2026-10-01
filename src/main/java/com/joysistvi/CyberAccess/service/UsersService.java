package com.joysistvi.CyberAccess.service;

import com.joysistvi.CyberAccess.model.Users;

import java.sql.SQLException;
import java.util.List;

public interface UsersService {

    List<Users> getAll()
            throws SQLException;

    Users getById(int id)
            throws SQLException;

    Users getByUsername(String username)
            throws SQLException;

    void add(Users user)
            throws SQLException;

    void update(Users user)
            throws SQLException;

    boolean remove(int id)
            throws SQLException;
}
